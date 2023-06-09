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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.controller.admin.AdminLocationDetailActivity;
import com.example.car_rental_prm392.model.Location;

import java.util.List;

public class AdminLocationAdapter extends RecyclerView.Adapter<AdminLocationAdapter.LocationViewHolder> {
    private Context context;
    private List<Location> listLocations;

    public AdminLocationAdapter(Context context, List<Location> listLocations) {
        this.context = context;
        this.listLocations = listLocations;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = listLocations.get(position);
        if (location == null) {
            return;
        }

//        Set information for location
        holder.tvId.setText(location.getId() + "");
        holder.tvName.setText(location.getName());
        if (location.getDescription()!=null){
            holder.tvDescription.setText(location.getDescription());
        }

        if (location.getImage() != null) {
            byte[] image = location.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.img.setImageBitmap(bitmap);
        }

//        Click to detail location
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byBundle(location);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listLocations != null)
            return listLocations.size();
        return 0;
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvId, tvName, tvDescription;
        private CardView cardView;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.admin_location_img);
            tvId = itemView.findViewById(R.id.admin_location_id);
            tvName = itemView.findViewById(R.id.admin_location_name);
            tvDescription = itemView.findViewById(R.id.admin_location_description);
            cardView = itemView.findViewById(R.id.admin_location_item);
        }
    }

    //    Get location by bundle
    public void byBundle(Location location) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, AdminLocationDetailActivity.class);
        bundle.putSerializable("location", location);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
