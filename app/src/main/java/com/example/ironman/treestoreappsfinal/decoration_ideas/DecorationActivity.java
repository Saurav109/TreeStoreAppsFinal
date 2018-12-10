package com.example.ironman.treestoreappsfinal.decoration_ideas;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ironman.treestoreappsfinal.R;
import com.example.ironman.treestoreappsfinal.product.ProductValueHolder;
import com.example.ironman.treestoreappsfinal.suggestion.SuggestionAdapter;
import com.example.ironman.treestoreappsfinal.suggestion.SuggestionsValueHolder;
import com.example.ironman.treestoreappsfinal.suggestion.dummySuggestionValueHolder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DecorationActivity extends AppCompatActivity {
    RecyclerView decoRyc;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SuggestionsValueHolder> decorationValueArray;
    DatabaseReference decoRef;
    SuggestionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoration);


        decorationValueArray=new ArrayList<>();
        layoutManager=new LinearLayoutManager(this);
        decoRyc=findViewById(R.id.deco_ryc);
        decoRyc.setLayoutManager(layoutManager);
        decoRef =FirebaseDatabase.getInstance().getReference().child("deco_ideas");
        adapter=new SuggestionAdapter(this,decorationValueArray,200);
        decoRyc.setAdapter(adapter);

        getDecoData();
    }

    void getDecoData(){
        decoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                decorationValueArray.add(new SuggestionsValueHolder(
                        dataSnapshot.getValue(dummySuggestionValueHolder.class).getText(),
                        dataSnapshot.getValue(dummySuggestionValueHolder.class).getImageUrl(),
                        dataSnapshot.getValue(dummySuggestionValueHolder.class).getHeader(),
                        dataSnapshot.getKey()
                ));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
