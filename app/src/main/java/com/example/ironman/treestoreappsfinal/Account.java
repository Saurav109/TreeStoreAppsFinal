package com.example.ironman.treestoreappsfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ironman.treestoreappsfinal.user_management.ResetPass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Account extends AppCompatActivity {

    TextView email;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        email=findViewById(R.id.profile_email);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        email.setText(user.getEmail());


    }

    public void goToRestPassword(View view) {
        startActivity(new Intent(this,ResetPass.class));
    }

    public void logout_from_app(View view) {
        auth.signOut();
        finish();
    }
}
