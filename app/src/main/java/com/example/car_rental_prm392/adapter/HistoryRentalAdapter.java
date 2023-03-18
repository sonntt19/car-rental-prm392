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
import com.example.car_rental_prm392.controller.common.DetailHistoryActivity;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Car;
import com.example.car_rental_prm392.model.Location;
import com.example.car_rental_prm392.model.Rental;
import com.example.car_rental_prm392.model.User;

import java.util.ArrayList;
import java.util.List;

public class HistoryRentalAdapter extends RecyclerView.Adapter<HistoryRentalAdapter.RentalViewHolder> {
    private Context context;
    private List<Rental> listRentals;

    public HistoryRentalAdapter(Context context, List<Rental> listRentals) {
        this.context = context;
        this.listRentals = listRentals;
    }

    @NonNull
    @Override
    public RentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new RentalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalViewHolder holder, int position) {
        Rental rental = listRentals.get(position);
        if (rental == null) {
            return;
        }
        DBManager dbManager = new DBManager(context);
        ArrayList<User> listUser = dbManager.getAllUser();
        String customerName = "";
        for (User o : listUser) {
            if (o.getUserId() == rental.getCustomerId())
                customerName = o.getFullName();
        }
        ArrayList<Car> listCar = dbManager.getAllCar();
        String carName = "";
        for (Car o : listCar) {
            if (o.getId() == rental.getCarId())
                carName = o.getName();
        }
        String status = "";
        if (rental.getStatus()==1)
            status = "Waiting";
        else if (rental.getStatus()==2)
            status = "Renting";
        else if (rental.getStatus()==3)
            status = "End";
        else if (rental.getStatus()==4)
            status = "Cancel";

        holder.tvId.setText(rental.getId() + "");
        holder.tvCustomer.setText(customerName);
        holder.tvCar.setText(carName);

        holder.tvStatus.setText(status);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byBundle(rental);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listRentals != null)
            return listRentals.size();
        return 0;
    }

    public class RentalViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvCustomer, tvCar, tvStatus;
        private CardView cardView;

        public RentalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.item_history_rental_id);
            tvCustomer = itemView.findViewById(R.id.item_history_rental_customer);
            tvCar = itemView.findViewById(R.id.item_history_rental_car);
            tvStatus = itemView.findViewById(R.id.item_history_rental_status);
            cardView = itemView.findViewById(R.id.history_cv_rental);
        }
    }

    public void byBundle(Rental rental) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, DetailHistoryActivity.class);
        bundle.putSerializable("rental", rental);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
