package com.endra.shareex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class RegisterActivity extends AppCompatActivity {
    EditText rName, rEmail, rPassword;
    Button RegBtn;
    ImageButton profilePicBtn;
    private FirebaseAuth rAuth;
    private DatabaseReference rDatabaseUser;
    private ProgressDialog rProgress;
    private StorageReference rStorageUser;
    Uri profilePic=null;
    String  uploadUri;
    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rName = (EditText) findViewById(R.id.nameField);
        rEmail = (EditText) findViewById(R.id.emailField);
        rPassword = (EditText) findViewById(R.id.passwordField);
        profilePicBtn = (ImageButton) findViewById(R.id.profilepicBtn);
        RegBtn = (Button) findViewById(R.id.registerBtn);

        rAuth = FirebaseAuth.getInstance();
        rDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users");
        rDatabaseUser.keepSynced(true);
        rStorageUser = FirebaseStorage.getInstance().getReference();
        rProgress = new ProgressDialog(this);

        profilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_GET_CONTENT);
                openGallery.setType("image/*");
                startActivityForResult(openGallery, GALLERY_REQUEST);
            }
        });

        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            profilePic = data.getData();
            CropImage.activity(profilePic).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    profilePic = result.getUri();
                    profilePicBtn.setImageURI(profilePic);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
    }

    private void startRegister() {
        final String name = rName.getText().toString().trim();
        String email = rEmail.getText().toString().trim();
        String password = rPassword.getText().toString().trim();
        if(profilePic==null){
            uploadUri="default";
        }
        else{
            uploadPic();
        }
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            rProgress.setMessage("Registering..");
            rProgress.show();
            rAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        String user_id = rAuth.getCurrentUser().getUid();
                        DatabaseReference current_user = rDatabaseUser.child(user_id);
                        current_user.child("name").setValue(name);
                        current_user.child("image").setValue(uploadUri);

                        rProgress.dismiss();
                        Intent tologinIntent = new Intent(RegisterActivity.this, LogInActivity.class);
                        tologinIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(tologinIntent);
                    }
                }
            });
        }
    }

    private void uploadPic() {
        StorageReference picPath = rStorageUser.child("Profile_pic").child(profilePic.getLastPathSegment());
        picPath.putFile(profilePic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              uploadUri = taskSnapshot.getDownloadUrl().toString();
            }
        });
    }
}
