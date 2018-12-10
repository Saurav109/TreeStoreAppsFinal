package com.example.ironman.treestoreappsfinal.suggestion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ironman.treestoreappsfinal.R;
import com.example.ironman.treestoreappsfinal.blog.SuggestionBlog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionViewHolder> {
    private ArrayList<SuggestionsValueHolder> suggestionsValueHolderArrayList;
    private Context context;
    private int VIEW;
    private StorageReference storageReference;

    public SuggestionAdapter(Context context,ArrayList<SuggestionsValueHolder> suggestionsValueHolderArrayList,int VIEW){
        this.suggestionsValueHolderArrayList=suggestionsValueHolderArrayList;
        this.context=context;
        this.VIEW=VIEW;
        storageReference=FirebaseStorage.getInstance().getReference("/image");
    }


    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.suggestion_item,viewGroup,false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SuggestionViewHolder suggestionViewHolder, int i) {
        final String suggestion=suggestionsValueHolderArrayList.get(i).getText();
        final String image_url=suggestionsValueHolderArrayList.get(i).getImageUrl();
        final String header=suggestionsValueHolderArrayList.get(i).getHeader();
        final String parent=suggestionsValueHolderArrayList.get(i).getParentNode();

        suggestionViewHolder.suggestionText.setText(header);
//        Picasso.get().load(image_url).into(suggestionViewHolder.imageView);

        storageReference.child(image_url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.get().load(uri.toString()).into(suggestionViewHolder.imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        suggestionViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( context,SuggestionBlog.class);
                intent.putExtra("decs",suggestion);
                intent.putExtra("image_url",image_url);
                intent.putExtra("header",header);
                intent.putExtra("parent",parent);
                intent.putExtra("VIEW",VIEW);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestionsValueHolderArrayList.size();
    }
}
