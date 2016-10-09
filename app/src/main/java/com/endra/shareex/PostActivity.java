package com.endra.shareex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private DatabaseReference rDatabaseUser;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
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
        mDatabase.keepSynced(true);
        //rDatabaseUser=FirebaseDatabase.getInstance().getReference().child("users");
        //rDatabaseUser.keepSynced(true);
        //mAuth=FirebaseAuth.getInstance();
        //mCurrentUser=mAuth.getCurrentUser();
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
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUri=taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost=mDatabase.push();
                    newPost.child("title").setValue(title);
                    newPost.child("desc").setValue(desc);
                    newPost.child("image").setValue(downloadUri.toString());
                    //newPost.child("uid").setValue(mCurrentUser.getUid());
                    /*rDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){


                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"OOPS!! SOMETHING WRONG",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/
                    mProgress.dismiss();

                    Intent mainIntent =new Intent(PostActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });
        }
    }
}
