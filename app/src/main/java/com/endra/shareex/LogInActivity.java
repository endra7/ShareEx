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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LogInActivity extends AppCompatActivity {
    EditText lemail,lpassword;
    Button loginBtn,rBtn;
    FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        lemail= (EditText) findViewById(R.id.loginemailField);
        lpassword= (EditText) findViewById(R.id.loginpasswordField);
        loginBtn= (Button) findViewById(R.id.loginBtn);
        rBtn= (Button) findViewById(R.id.toRegisterBtn);

        mProgress=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Logging in");
                mProgress.show();
                checkUserExist();
            }
        });

        rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegIntent=new Intent(LogInActivity.this,RegisterActivity.class);
                toRegIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toRegIntent);
            }
        });
    }

    private void checkUserExist() {
        String email=lemail.getText().toString().trim();
        String password=lpassword.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mProgress.dismiss();
                        Intent toMain=new Intent(LogInActivity.this,MainActivity.class);
                        toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(toMain);
                    }
                    else{
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(),"Invalid Email or Password",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            mProgress.dismiss();
            Toast.makeText(LogInActivity.this,"Please enter Email and Password",Toast.LENGTH_SHORT).show();
        }
    }
}
