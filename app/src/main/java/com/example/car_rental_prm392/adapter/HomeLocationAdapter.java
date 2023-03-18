package com.example.car_rental_prm392.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.controller.admin.AdminLocationDetailActivity;
import com.example.car_rental_prm392.controller.common.ListCarForLocationActivity;
import com.example.car_rental_prm392.model.Location;

import java.util.List;

public class HomeLocationAdapter extends RecyclerView.Adapter<HomeLocationAdapter.LocationViewHolder>{
    private Context context;
    private List<Location> listLocations;

    public HomeLocationAdapter(Context context, List<Location> listLocations) {
        this.context = context;
        this.listLocations = listLocations;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = listLocations.get(position);
        if (location==null){
            return;
        }
        holder.tvName.setText(location.getName());
        byte[] image = location.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.img.setImageBitmap(bitmap);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListCarForLocationActivity.class);
                intent.putExtra("locationId",location.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listLocations!=null)
            return listLocations.size();
        return 0;
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView tvName;
        private CardView cardView;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.home_location_img);
            tvName = itemView.findViewById(R.id.home_location_name);
            cardView = itemView.findViewById(R.id.home_location_item);
        }
    }

}
