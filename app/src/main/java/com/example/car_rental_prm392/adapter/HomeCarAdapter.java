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
import com.example.car_rental_prm392.controller.admin.AdminCarDetailActivity;
import com.example.car_rental_prm392.controller.common.CarDetailActivity;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Car;
import com.example.car_rental_prm392.model.Location;

import java.util.ArrayList;
import java.util.List;

public class HomeCarAdapter extends RecyclerView.Adapter<HomeCarAdapter.CarViewHolder> {
    private Context context;
    private List<Car> listCars;

    public HomeCarAdapter(Context context, List<Car> listCars) {
        this.context = context;
        this.listCars = listCars;
    }

    public void setFilteredList(List<Car> filteredList) {
        this.listCars = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = listCars.get(position);
        if (car == null) {
            return;
        }

//        Get List location for Location name
        DBManager dbManager = new DBManager(context);
        ArrayList<Location> listLocations = dbManager.getAllLocation();
        String location = "";
        for (Location o :
                listLocations) {
            if (car.getLocationId() == o.getId())
                location = o.getName();
        }

//        Set information for each Car in Home Screen
        holder.tvName.setText(car.getName());
        holder.tvLocation.setText("Location: " + location);
        holder.tvPrice.setText("Price: " + car.getPrice() + "$/day");
        if (car.getImage() != null) {
            byte[] image = car.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.img.setImageBitmap(bitmap);
        }

//      Click to Car detail in Home Screen
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byBundle(car);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listCars != null)
            return listCars.size();
        return 0;
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvName, tvLocation, tvPrice;
        private CardView cardView;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.home_car_img);
            tvName = itemView.findViewById(R.id.home_car_name);
            tvLocation = itemView.findViewById(R.id.home_car_location);
            tvPrice = itemView.findViewById(R.id.home_car_price);
            cardView = itemView.findViewById(R.id.home_car_item);
        }
    }

    //    Get Car by bundle
    public void byBundle(Car car) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, CarDetailActivity.class);
        bundle.putSerializable("car", car);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
