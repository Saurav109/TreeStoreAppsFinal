package com.example.ironman.treestoreappsfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ironman.treestoreappsfinal.product.ProductValueHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    EditText bkashCode;
    EditText address;
    EditText contact;

//    String productNameString;
    String contactNoString;
    String bkashCodeString;

    Intent intent;

    SharedPreferences bundle;
    ProductValueHolder productValueHolder;

    public String name;
    public String decs="";
    public String imageUrl;
    public double price;
    public String seasonalad="";
    public String uses="";

    TextView textView;

    @Override
    protected void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.payment);
        textView=findViewById(R.id.buying_pro);


        bundle=getSharedPreferences("state",0);
        intent=getIntent();
//        if(!sharedPreferences.getBoolean("buymode",false)){
//            intent=getIntent();
//            productValueHolder=new ProductValueHolder(
//                    intent.getStringExtra("name"),
//                    intent.getStringExtra("dec"),
//                    intent.getStringExtra("imageUrl"),
//                    intent.getDoubleExtra("double_price",0),
//                    intent.getStringExtra("advice"),
//                    intent.getStringExtra("use")
//            );
//            changeMode(true);
//        }

        if(intent.getBooleanExtra("fromAc",false)){
            productValueHolder=new ProductValueHolder(
                    intent.getStringExtra("name"),
                    intent.getStringExtra("dec"),
                    intent.getStringExtra("imageUrl"),
                    intent.getDoubleExtra("double_price",0),
                    intent.getStringExtra("advice"),
                    intent.getStringExtra("use")
            );

            setSaveInstance(intent.getStringExtra("name"),
                    intent.getStringExtra("dec"),
                    intent.getStringExtra("imageUrl"),
                    intent.getDoubleExtra("double_price",0),
                    intent.getStringExtra("advice"),
                    intent.getStringExtra("use"));
            textView.setText(intent.getStringExtra("name"));
            changeMode(true);
        }else {

            name=bundle.getString("name","");
            decs=bundle.getString("dec","");
            imageUrl=bundle.getString("imageUrl","");
            price=Double.valueOf(bundle.getString("price",""));
            seasonalad=bundle.getString("seasonal","");
            uses=bundle.getString("use","");

            productValueHolder=new ProductValueHolder(name,decs,imageUrl,price,seasonalad,uses);

            textView.setText(name);

        }




        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        user=firebaseAuth.getCurrentUser();

        address=findViewById(R.id.adress);
        contact=findViewById(R.id.contactno);
        bkashCode=findViewById(R.id.bkash_code);

//        Intent thisIntent=getIntent();
//        productNameString=thisIntent.getStringExtra("name");

    }


    public void send_bkash_code(View view) {
        if(!address.getText().toString().isEmpty() &&
                !contact.getText().toString().isEmpty() &&
                !bkashCode.getText().toString().isEmpty()){


//            ProductValueHolder productValueHolder=new ProductValueHolder(
//                    intent.getStringExtra("name"),
//                    intent.getStringExtra("dec"),
//                    intent.getStringExtra("imageUrl"),
//                    intent.getDoubleExtra("double_price",0),
//                    intent.getStringExtra("advice"),
//                    intent.getStringExtra("use")
//            );
            databaseReference.child("users").child(user.getUid()).child("cart").push().setValue(productValueHolder);
//            databaseReference.child("Admin").child("new_order").push().child(firebaseAuth.getUid()).child("order").setValue(productValueHolder);
            HashMap orderInfo=new HashMap();
            orderInfo.put("adress",address.getText().toString());
            orderInfo.put("phoneNumber",contact.getText().toString());
            orderInfo.put("bkashCode",bkashCode.getText().toString());

            HashMap allInfo=new HashMap();
            allInfo.put("orderInfo",orderInfo);
            allInfo.put("productInfo",productValueHolder);
            databaseReference.child("Admin").child("new_order").push().child(firebaseAuth.getUid()).setValue(allInfo);

            changeMode(false);
            SharedPreferences preferences=getSharedPreferences("state",0);
            SharedPreferences.Editor outState=preferences.edit();
            outState.clear();
            outState.apply();
            Toast.makeText(this,"Request Confirmed, Your plants will be in your door ;) ",Toast.LENGTH_LONG).show();
            finish();
        }else {
            Toast.makeText(this,"Require all fields ",Toast.LENGTH_LONG).show();
        }
    }



    void changeMode(boolean mode){
        SharedPreferences sharedPreferences=getSharedPreferences("buymode",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("buymode",mode);
        editor.apply();
    }

    void setSaveInstance(String name, String decs, String imageUrl, double price, String seasonalad, String uses){
        SharedPreferences preferences=getSharedPreferences("state",0);
        SharedPreferences.Editor outState=preferences.edit();
        outState.putString("name",name);
        outState.putString("dec",decs);
        outState.putString("imageUrl",imageUrl);
        outState.putString("seasonal",seasonalad);
        outState.putString("use",uses);
        outState.putString("price", String.valueOf(price));
        outState.apply();
    }
    public void cancelBuy(View view) {
        SharedPreferences preferences=getSharedPreferences("state",0);
        SharedPreferences.Editor outState=preferences.edit();
        outState.clear();
        outState.apply();
        changeMode(false);
        finish();
        startActivity(new Intent(Payment.this,MainActivity.class));
    }


}
