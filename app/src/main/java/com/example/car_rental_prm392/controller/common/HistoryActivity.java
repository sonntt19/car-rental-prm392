package com.example.car_rental_prm392.controller.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.adapter.HistoryRentalAdapter;
import com.example.car_rental_prm392.adapter.HomeLocationAdapter;
import com.example.car_rental_prm392.controller.data_local.DataLocalManager;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Location;
import com.example.car_rental_prm392.model.Rental;
import com.example.car_rental_prm392.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView listViewRental;
    private HistoryRentalAdapter historyRentalAdapter;
    private ArrayList<Rental> listRentals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        DBManager dbManager = new DBManager(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_bot_history);


        //        Account Information
        User user = DataLocalManager.getUser();
        listViewRental  = findViewById(R.id.history_rv_rental);
        listRentals = dbManager.getAllRentalByUserId(user.getUserId());

        historyRentalAdapter = new HistoryRentalAdapter(this, listRentals);

        LinearLayoutManager linearLayoutManagerLocation = new LinearLayoutManager(this);
        listViewRental.setLayoutManager(linearLayoutManagerLocation);
        listViewRental.setAdapter(historyRentalAdapter);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_bot_history:
                    return true;
                case R.id.nav_bot_home:
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    return true;
                case R.id.nav_bot_profile:
                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    finish();
                    return true;
            }
            return false;
        });
    }
}