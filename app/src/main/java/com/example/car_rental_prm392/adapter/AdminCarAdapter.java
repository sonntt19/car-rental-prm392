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
import androidx.recyclerview.widget.RecyclerView;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.controller.admin.AdminCarDetailActivity;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Car;
import com.example.car_rental_prm392.model.Location;

import java.util.ArrayList;
import java.util.List;

public class AdminCarAdapter extends RecyclerView.Adapter<AdminCarAdapter.CarViewHolder> {
    private Context context;
    private List<Car> listCars;

    public AdminCarAdapter(Context context, List<Car> listCars) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = listCars.get(position);
        if (car == null) {
            return;
        }

//        Get list location
        DBManager dbManager = new DBManager(context);
        ArrayList<Location> listLocations = dbManager.getAllLocation();

//        Set location for each car
        String location = "";
        for (Location o :
                listLocations) {
            if (car.getLocationId() == o.getId())
                location = o.getName();
        }

//        Set information for car
        holder.tvName.setText(car.getName());
        holder.tvLocation.setText(location);
        holder.tvPrice.setText(car.getPrice() + "$/day");
        if (car.getImage() != null) {
            byte[] image = car.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.img.setImageBitmap(bitmap);
        }


//        Click detail car
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
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
        private RelativeLayout relativeLayout;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.admin_car_img);
            tvName = itemView.findViewById(R.id.admin_car_name);
            tvLocation = itemView.findViewById(R.id.admin_car_location);
            tvPrice = itemView.findViewById(R.id.admin_car_price);
            relativeLayout = itemView.findViewById(R.id.admin_car_item);
        }
    }

    //    Get car by bundle
    public void byBundle(Car car) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, AdminCarDetailActivity.class);
        bundle.putSerializable("car", car);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
