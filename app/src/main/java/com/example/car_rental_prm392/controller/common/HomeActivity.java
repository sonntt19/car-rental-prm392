package com.example.car_rental_prm392.controller.common;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.adapter.HomeCarAdapter;
import com.example.car_rental_prm392.adapter.HomeLocationAdapter;
import com.example.car_rental_prm392.controller.data_local.DataLocalManager;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Car;
import com.example.car_rental_prm392.model.Location;
import com.example.car_rental_prm392.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    RecyclerView listViewLocation;
    private HomeLocationAdapter homeLocationAdapter;
    private ArrayList<Location> listLocations;

    RecyclerView listViewCar;
    private HomeCarAdapter carAdapter;
    private ArrayList<Car> listcars;

    private TextView tvName;
    CircleImageView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_bot_home);
        DBManager dbManager = new DBManager(this);

//        Account Information
        tvName = findViewById(R.id.home_account_name);
        btnLogout = findViewById(R.id.home_account_logout);
        User user = DataLocalManager.getUser();
        if (user!=null){
            tvName.setText("Hello "+user.getFullName());
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

//        List Location
        listViewLocation  = findViewById(R.id.home_rv_location);
        listLocations = dbManager.getAllLocation();

        homeLocationAdapter = new HomeLocationAdapter(this, listLocations);

        LinearLayoutManager linearLayoutManagerLocation = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        listViewLocation.setLayoutManager(linearLayoutManagerLocation);
        listViewLocation.setAdapter(homeLocationAdapter);

//        List Car

        listViewCar  = findViewById(R.id.home_rv_car);
        listcars = dbManager.getAllCarAvailable();
        carAdapter = new HomeCarAdapter(this, listcars);

        LinearLayoutManager linearLayoutManagerCar = new LinearLayoutManager(this);
        listViewCar.setLayoutManager(linearLayoutManagerCar);
        listViewCar.setAdapter(carAdapter);

//        Set Event
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_bot_home:
                    return true;
                case R.id.nav_bot_history:
                    startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
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

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Do you want to logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DataLocalManager.removeUser();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
