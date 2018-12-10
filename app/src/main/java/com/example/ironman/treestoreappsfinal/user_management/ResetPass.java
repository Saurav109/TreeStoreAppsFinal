package com.example.ironman.treestoreappsfinal.user_management;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ironman.treestoreappsfinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPass extends AppCompatActivity {

    EditText resetEmail;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_pass);

        progressDialog=new ProgressDialog(this);
        resetEmail=findViewById(R.id.reset_email);
    }

    public void reset_email(View view) {
        String email=resetEmail.getText().toString();
        if(!email.isEmpty()){
            reset(email);
        }else {
            Toast.makeText(getApplicationContext(),"Enter your email first",Toast.LENGTH_LONG).show();
        }
    }

    void reset(String email){
        progressDialog.show();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            finish();
                            Toast.makeText(getApplicationContext(),"Email sent!!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Email sent failed , check internet`q",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
