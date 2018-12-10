package com.example.ironman.treestoreappsfinal.cart;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.ironman.treestoreappsfinal.R;
import com.example.ironman.treestoreappsfinal.product.ProductAdapter;
import com.example.ironman.treestoreappsfinal.product.ProductValueHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    RecyclerView cartRyc;
    GridLayoutManager gridLayoutManager;
    ArrayList<ProductValueHolder> cartArray;
    ProductAdapter productAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference cartData;

    FirebaseAuth auth;
    FirebaseUser user;

    TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        total=findViewById(R.id.total);

        cartRyc =findViewById(R.id.cart_ryc);
        cartRyc.setNestedScrollingEnabled(false);
        cartArray =new ArrayList<>();
        productAdapter =new ProductAdapter(this, cartArray);

        gridLayoutManager=new GridLayoutManager(this,2);
        cartRyc.setLayoutManager(gridLayoutManager);
        cartRyc.setAdapter(productAdapter);

        firebaseDatabase=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        if(user!=null){
            cartData =firebaseDatabase.getReference().child("users").child(user.getUid()).child("cart");
            getCartData();

        }
    }

    void getCartData(){
        cartData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                cartArray.add(dataSnapshot.getValue(ProductValueHolder.class));
                getTotalCount(cartArray);
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    void getTotalCount(    ArrayList<ProductValueHolder> cartArray){
        double price=0.0;
        for(ProductValueHolder productValueHolder:cartArray){
            price=price+productValueHolder.getPrice();
        }

        DecimalFormat df = new DecimalFormat("0.00");
        String display_txt="Total cost: "+df.format(price)+" TK";

       total.setText(display_txt);
    }



}
