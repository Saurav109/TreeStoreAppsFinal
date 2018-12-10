package com.example.ironman.treestoreappsfinal.user_management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ironman.treestoreappsfinal.MainActivity;
import com.example.ironman.treestoreappsfinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    EditText pass;
    EditText pass2;
    EditText email;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        email=findViewById(R.id.sign_email);
        pass=findViewById(R.id.sign_pass);
        pass2=findViewById(R.id.sign_pass2);
        progressDialog=new ProgressDialog(this);
        mAuth =FirebaseAuth.getInstance();
    }

    public void sign_up(View view) {
        String email=this.email.getText().toString()
                ,pass=this.pass.getText().toString()
                ,pass2=this.pass2.getText().toString();
        //enter all field
        if(!email.isEmpty() && !pass.isEmpty() &&!pass2.isEmpty()){
        //pass match
            if(pass.equals(pass2)){
                signUpToServer(email,pass);
            }
        //pass not match
            else {
                Toast.makeText(this,"Password Didn't match",Toast.LENGTH_LONG).show();
            }
        //not entered all field
        }else {
            Toast.makeText(this,"Enter all the field",Toast.LENGTH_LONG).show();
        }


    }

    void signUpToServer(String email,String pass){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Sign up successful",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignUp.this,MainActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Sign up failed, Check if this email have a account already",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
