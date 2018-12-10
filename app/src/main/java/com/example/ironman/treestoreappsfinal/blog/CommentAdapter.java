package com.example.ironman.treestoreappsfinal.blog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ironman.treestoreappsfinal.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.commentViewHolder> {

    ArrayList<CommentValueHolder> commentValueHolderArrayList;
    CommentAdapter(ArrayList<CommentValueHolder> commentValueHolderArrayList){
        this.commentValueHolderArrayList=commentValueHolderArrayList;
    }

    @NonNull
    @Override
    public commentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.place_item,viewGroup,false);
        return new commentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull commentViewHolder commentViewHolder, int i) {
        String comment,email;

        comment=commentValueHolderArrayList.get(i).getComment();
        email=commentValueHolderArrayList.get(i).getEmail();

        commentViewHolder.comment.setText(comment);
        commentViewHolder.email.setText(email);
    }

    @Override
    public int getItemCount() {
        return commentValueHolderArrayList.size();
    }

    class commentViewHolder extends RecyclerView.ViewHolder{
        TextView comment;
        TextView email;

        public commentViewHolder(@NonNull View itemView) {
            super(itemView);
            comment=itemView.findViewById(R.id.place_name);
            email=itemView.findViewById(R.id.place_location);
        }
    }
}
