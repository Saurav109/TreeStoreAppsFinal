package com.example.ironman.treestoreappsfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class Splash extends AppCompatActivity {
    Handler handler;
    Runnable runnable;
    SharedPreferences sharedPreferences;

    //TreeStoreDatabase00@gmail.com
    //treestore#*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler=new Handler();
        sharedPreferences =getSharedPreferences("buymode",0);
        if(sharedPreferences.getBoolean("buymode",false)){
            startActivity(new Intent(this, Payment.class));
            finish();
        }else {

            runnable=new Runnable() {
                @Override
                public void run() {
                    initActivity();
                    finish();
                }
            };
            handler.postDelayed(runnable,2000);
        }
        setContentView(R.layout.splash);

    }


    void initActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
