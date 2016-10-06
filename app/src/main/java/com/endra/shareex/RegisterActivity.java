package com.endra.shareex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText rName,rEmail,rPassword;
    Button RegBtn;
    private FirebaseAuth rAuth;
    private DatabaseReference rDatabaseUser;
    private ProgressDialog rProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rName= (EditText) findViewById(R.id.nameField);
        rEmail= (EditText) findViewById(R.id.emailField);
        rPassword= (EditText) findViewById(R.id.passwordField);
        RegBtn= (Button) findViewById(R.id.registerBtn);

        rAuth=FirebaseAuth.getInstance();
        rDatabaseUser= FirebaseDatabase.getInstance().getReference().child("users");
        rProgress=new ProgressDialog(this);

        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

    }

    private void startRegister() {
        final String name=rName.getText().toString().trim();
        String email=rEmail.getText().toString().trim();
        String password=rPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            rProgress.setMessage("Registering..");
            rProgress.show();

            rAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                            String user_id=rAuth.getCurrentUser().getUid();
                            DatabaseReference current_user = rDatabaseUser.child(user_id);
                            current_user.child("name").setValue(name);
                            current_user.child("image").setValue("default");
                            rProgress.dismiss();
                            Intent tologinIntent = new Intent(RegisterActivity.this, LogInActivity.class);
                            tologinIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(tologinIntent);
                    }
                }
            });
        }
    }
}
