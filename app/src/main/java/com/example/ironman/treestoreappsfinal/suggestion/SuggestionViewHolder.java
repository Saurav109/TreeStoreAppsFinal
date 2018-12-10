package com.example.ironman.treestoreappsfinal.suggestion;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ironman.treestoreappsfinal.R;

public class SuggestionViewHolder extends RecyclerView.ViewHolder {
    TextView suggestionText;
    ImageView imageView;
    public SuggestionViewHolder(@NonNull View itemView) {
        super(itemView);
        suggestionText=itemView.findViewById(R.id.suggestion_text);
        imageView=itemView.findViewById(R.id.suggestion_image);
    }

}
