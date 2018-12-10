package com.example.ironman.treestoreappsfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ironman.treestoreappsfinal.product.ProductValueHolder;
import com.example.ironman.treestoreappsfinal.user_management.Login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProductViewActivity extends AppCompatActivity {

    TextView tname;
    TextView tprice;
    WebView tdec;
    ImageView timageView;
    Button buy_button;

    ProductValueHolder productValueHolder;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView decs,use,season;

    String decsString,useString,adviceString,name,price,imageUrl;
    private StorageReference storageReference;
    double doublePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view_activity);

        tname=findViewById(R.id.name);
        tprice=findViewById(R.id.price);
        tdec=findViewById(R.id.dec);
        timageView=findViewById(R.id.product_image);
        buy_button=findViewById(R.id.buy_button);

        decs=findViewById(R.id.decs_button);
        season=findViewById(R.id.seaon_button);
        use=findViewById(R.id.use_button);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        user=firebaseAuth.getCurrentUser();

        Intent intent=getIntent();

        name=intent.getStringExtra("name");
//        String dec=intent.getStringExtra("dec");
        price=intent.getStringExtra("price");
        doublePrice= intent.getDoubleExtra("double_price",0);
        imageUrl=intent.getStringExtra("image_url");

        decsString=intent.getStringExtra("decs");
        adviceString=intent.getStringExtra("advice");
        useString=intent.getStringExtra("usage");


        storageReference=FirebaseStorage.getInstance().getReference("/image");

        tname.setText(name);
        tdec.loadData(decsString, "text/html; charset=utf-8", "UTF-8");
        tprice.setText(price);

        productValueHolder=new ProductValueHolder();

//        Picasso.get().load(imageUrl).into(timageView);


        storageReference.child(imageUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.get().load(uri.toString()).into(timageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfSigndIn()){
                    buyThisItem();
                }else {
                    goToSignIn();
                }
            }
        });
    }

    boolean checkIfSigndIn(){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        return auth.getCurrentUser() != null;
    }
    void goToSignIn(){
        Toast.makeText(this,"Please Login to buy plants",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,Login.class));
    }
    void buyThisItem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Add to cart?");

        builder.setPositiveButton("Add to cart", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                addToCart();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void addToCart() {
//        public String name;
//        public String decs="";
//        public String imageUrl;
//        public double price;
//
//        public String seasonalad="";
//        public String uses="";


        Intent intent=new Intent(this,Payment.class);
        intent.putExtra("name",name);
        intent.putExtra("double_price",doublePrice);
        intent.putExtra("imageUrl",imageUrl);
        intent.putExtra("dec",decsString);
        intent.putExtra("use",useString);
        intent.putExtra("advice",adviceString);

        intent.putExtra("fromAc",true);

        startActivity(intent);

        //databaseReference.child("users").child(user.getUid()).child("cart").push().setValue(productValueHolder);
        //databaseReference.child("Admin").child("new_order").push().child(firebaseAuth.getUid()).setValue(productValueHolder);
    }

    public void seeDecs(View view) {
        decs.setTextColor(getResources().getColor(R.color.white));
        decs.setTextSize(22);
        use.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        use.setTextSize(17);
        season.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        season.setTextSize(17);

        tdec.loadData(decsString,"text/html; charset=utf-8", "UTF-8");

    }

    public void seeAdvices(View view) {
        season.setTextColor(getResources().getColor(R.color.white));
        season.setTextSize(22);
        use.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        use.setTextSize(17);
        decs.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        decs.setTextSize(17);

        tdec.loadData(adviceString,"text/html; charset=utf-8", "UTF-8");
    }

    public void seeUsefulness(View view) {
        use.setTextColor(getResources().getColor(R.color.white));
        use.setTextSize(22);
        decs.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        decs.setTextSize(17);
        season.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        season.setTextSize(17);

        tdec.loadData(useString,"text/html; charset=utf-8", "UTF-8");

    }
}
