package com.example.ironman.treestoreappsfinal.blog;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ironman.treestoreappsfinal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SuggestionBlog extends AppCompatActivity {

    String image_url;
    String decs;
    String header;
    String parentNode;
    int VIEW;

    ImageView imageView;
    WebView webView;
    TextView headerTextView;
    EditText comment;

    DatabaseReference databaseReference;
    RecyclerView placeRyc;
    RecyclerView.LayoutManager linearLayout;
    CommentAdapter commentAdapter;
    ArrayList<CommentValueHolder> commentValueHolderArrayList;
    FirebaseAuth auth;
    FirebaseUser user;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestion_blog);

        Intent intent=getIntent();
        image_url=intent.getStringExtra("image_url");
        decs=intent.getStringExtra("decs");
        header=intent.getStringExtra("header");
        parentNode=intent.getStringExtra("parent");
        VIEW=intent.getIntExtra("VIEW",0);

        storageReference=FirebaseStorage.getInstance().getReference("/image");

        comment=findViewById(R.id.comment);
        imageView=findViewById(R.id.blog_image);
        webView =findViewById(R.id.blog_text);
        webView.getSettings().setJavaScriptEnabled(true);
        headerTextView=findViewById(R.id.header_text_view);

//        Picasso.get().load(image_url).into(imageView);

        storageReference.child(image_url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.get().load(uri.toString()).into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        headerTextView.setText(header);
        webView.loadData(decs, "text/html; charset=utf-8", "UTF-8");

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        if(VIEW==100){
            databaseReference=FirebaseDatabase.getInstance().getReference().child("suggestions").child(parentNode)
                    .child("comments");
        }
        else if(VIEW==200){
            databaseReference=FirebaseDatabase.getInstance().getReference().child("deco_ideas").child(parentNode)
                    .child("comments");
        }


        linearLayout=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        commentValueHolderArrayList =new ArrayList<>();
        commentAdapter =new CommentAdapter(commentValueHolderArrayList);
        placeRyc =findViewById(R.id.comment_ryc);
        placeRyc.setLayoutManager(linearLayout);
        placeRyc.setNestedScrollingEnabled(false);
        placeRyc.setAdapter(commentAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                commentValueHolderArrayList.add(dataSnapshot.getValue(CommentValueHolder.class));
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

    public void addComment(View view) {
        if(!comment.getText().toString().isEmpty()){
            if(user!=null){
                databaseReference.push().setValue(new CommentValueHolder(comment.getText().toString(),user.getEmail()));
            }else {
                databaseReference.push().setValue(new CommentValueHolder(comment.getText().toString(),"Anonymous"));
            }
            comment.setText("");
        }
    }
}
