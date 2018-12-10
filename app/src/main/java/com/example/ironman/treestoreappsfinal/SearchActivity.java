package com.example.ironman.treestoreappsfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ironman.treestoreappsfinal.product.ProductAdapter;
import com.example.ironman.treestoreappsfinal.product.ProductValueHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    RecyclerView searchResultRyc;
    GridLayoutManager gridLayoutManager;
    ArrayList<ProductValueHolder> searchResultArray;
    ProductAdapter productAdapter;

    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        searchEditText=findViewById(R.id.search_edit_text);
        searchResultRyc =findViewById(R.id.search_result_ryc);
        searchResultArray =new ArrayList<>();
        productAdapter =new ProductAdapter(this, searchResultArray);
        gridLayoutManager=new GridLayoutManager(this,2);
        searchResultRyc.setLayoutManager(gridLayoutManager);
        searchResultRyc.setNestedScrollingEnabled(false);
        searchResultRyc.setAdapter(productAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mainText=searchEditText.getText().toString();
                if(mainText.length()<2){
                    search(mainText.toUpperCase());
                }else {
                    String cap=mainText.substring(0, 1).toUpperCase() + mainText.substring(1);
                    search(cap);
                }
            }
        });


        Intent intent=getIntent();
        if(intent.getStringExtra("search_text")!=null){
            searchEditText.setText(intent.getStringExtra("search_text"));
            search(intent.getStringExtra("search_text"));

        }
    }






    void search(String queryText){
        searchResultArray.clear();

        DatabaseReference myRef;
        FirebaseDatabase database;
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        myRef=myRef.child("products");

        //Firebese search
        Query name=myRef.orderByChild("name").limitToFirst(10).startAt(queryText).endAt(queryText+"\uf8ff");

        name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child:dataSnapshot.getChildren()){
                    searchResultArray.add(child.getValue(ProductValueHolder.class));
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error!!",Toast.LENGTH_LONG).show();
            }
        });
    }


}
