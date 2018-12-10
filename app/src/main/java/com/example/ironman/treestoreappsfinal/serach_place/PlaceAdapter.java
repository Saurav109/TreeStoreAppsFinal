package com.example.ironman.treestoreappsfinal.serach_place;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ironman.treestoreappsfinal.R;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {


    ArrayList<PlaceValueHolder> placeValueHolderArrayList;
    Context context;

    public PlaceAdapter(Context context, ArrayList<PlaceValueHolder> placeValueHolderArrayList){
        this.placeValueHolderArrayList=placeValueHolderArrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.place_item,viewGroup,false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder placeViewHolder, int i) {
        String name,location;

        name=placeValueHolderArrayList.get(i).getPlaceName();
        location=placeValueHolderArrayList.get(i).getLocation();
        final double lat=placeValueHolderArrayList.get(i).getLat();
        final double lon=placeValueHolderArrayList.get(i).getLon();

        placeViewHolder.name.setText(name);
        placeViewHolder.location.setText(location);

        placeViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Toast.makeText(context,"Clicked",Toast.LENGTH_LONG).show();
                goToGoogleMapApps(lat,lon);
            }
        });
    }

    void goToGoogleMapApps(double lat,double lon){


        Uri.Builder directionsBuilder = new Uri.Builder()
                .scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("destination", lat + "," + lon);

        context.startActivity(new Intent(Intent.ACTION_VIEW, directionsBuilder.build()));


    }

    @Override
    public int getItemCount() {
        return placeValueHolderArrayList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder{
        TextView name,location;
        CardView cardView;
        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.place_name);
            location=itemView.findViewById(R.id.place_location);
            cardView=itemView.findViewById(R.id.place_card);
        }
    }
}
