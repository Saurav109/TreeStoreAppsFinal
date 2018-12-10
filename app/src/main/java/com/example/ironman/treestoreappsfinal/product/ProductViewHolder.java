package com.example.ironman.treestoreappsfinal.product;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ironman.treestoreappsfinal.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView price;
//    TextView dec;
    ImageView imageView;
    LinearLayout card;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.name);
        price=itemView.findViewById(R.id.price);
//        dec=itemView.findViewById(R.id.dec);
        imageView=itemView.findViewById(R.id.image);
        card =itemView.findViewById(R.id.pro_item);
    }
}
