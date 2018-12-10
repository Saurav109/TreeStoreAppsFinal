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


public class Login extends AppCompatActivity {

    EditText pass;
    EditText email;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email=findViewById(R.id.l_email);
        pass=findViewById(R.id.l_pass);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        mAuth =FirebaseAuth.getInstance();

    }

    public void log_in(View view) {
        progressDialog.show();
        String email=this.email.getText().toString()
                ,pass=this.pass.getText().toString();
        //
        if(!email.isEmpty() && !pass.isEmpty()){
            loginToServer(email,pass);
        }
        //
        else {
            Toast.makeText(this,"Enter all the field",Toast.LENGTH_LONG).show();
        }
    }

    void loginToServer(String email,String pass){
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void goToSignUp(View view) {
        startActivity(new Intent(this,SignUp.class));
    }

    public void goToRestPass(View view) {
        startActivity(new Intent(this,ResetPass.class));
    }
}
