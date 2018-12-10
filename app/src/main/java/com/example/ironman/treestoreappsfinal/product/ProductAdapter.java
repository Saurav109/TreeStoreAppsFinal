package com.example.ironman.treestoreappsfinal.product;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ironman.treestoreappsfinal.ProductViewActivity;
import com.example.ironman.treestoreappsfinal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private ArrayList<ProductValueHolder> productValueModelArrayList;
    private Context context;
    private StorageReference storageReference;

    public ProductAdapter(Context context, ArrayList<ProductValueHolder> productValueModelArrayList){
        this.productValueModelArrayList=productValueModelArrayList;
        this.context=context;
        storageReference=FirebaseStorage.getInstance().getReference().child("image");
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item,viewGroup,false);
        return new ProductViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder productViewModel, int i) {
        final String name= productValueModelArrayList.get(i).getName();
        final String decs=productValueModelArrayList.get(i).getDecs();
        final String advice=productValueModelArrayList.get(i).getSeasonalad();
        final String use=productValueModelArrayList.get(i).getUses();

        final String image_url=productValueModelArrayList.get(i).getImageUrl();
        final double price_double=productValueModelArrayList.get(i).getPrice();
        final String price=price_double+ " Taka";

        productViewModel.name.setText(name);
        //productViewModel.dec.setText(decs);
        productViewModel.price.setText(price);
//        Picasso.get().load(image_url).into(productViewModel.imageView);


//        StorageReference load = getImage(id);

        storageReference.child(image_url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.get().load(uri.toString()).into(productViewModel.imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        productViewModel.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( context,ProductViewActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("decs",decs);
                intent.putExtra("advice",advice);
                intent.putExtra("usage",use);

                intent.putExtra("price",price);
                intent.putExtra("image_url",image_url);
                intent.putExtra("double_price",price_double);

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return productValueModelArrayList.size();
    }
}
