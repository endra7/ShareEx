package com.endra.shareex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

public class PostActivity extends AppCompatActivity {
    private ImageButton mPostImage;
    EditText mPostTitle,mPostDesc;
    private Uri mImageUri;
    Button mPostBtn;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private static final int GALLERY_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mPostTitle= (EditText) findViewById(R.id.titleField);
        mPostImage= (ImageButton) findViewById(R.id.imageSelect);
        mPostDesc= (EditText) findViewById(R.id.descField);
        mPostBtn= (Button) findViewById(R.id.postBtn);
        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        mProgress=new ProgressDialog(this);
        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Posting....");
                mProgress.show();
                startPosting();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_REQUEST && resultCode==RESULT_OK){
            mImageUri=data.getData();
            mPostImage.setImageURI(mImageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startPosting() {
        final String title=mPostTitle.getText().toString().trim();
        final String desc=mPostDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(desc) && mImageUri!= null){
            StorageReference filePath=mStorage.child("Blog_Images").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost=mDatabase.push();
                    newPost.child("title").setValue(title);
                    newPost.child("desc").setValue(desc);
                    newPost.child("image").setValue(downloadUri.toString());
                    mProgress.dismiss();

                    Intent mainIntent =new Intent(PostActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });
        }
    }
}
