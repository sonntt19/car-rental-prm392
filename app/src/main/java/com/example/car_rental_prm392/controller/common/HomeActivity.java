package com.example.car_rental_prm392.controller.common;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
    CircleImageView cImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_bot_home);
        DBManager dbManager = new DBManager(this);

//        Account Information
        tvName = findViewById(R.id.home_account_name);
        cImg = findViewById(R.id.home_account_img);
        User user = DataLocalManager.getUser();
        if (user!=null){
            tvName.setText("Hello "+user.getFullName());
            if(user.getAvatar()!=null){
                byte[] image = user.getAvatar();
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                cImg.setImageBitmap(bitmap);
            }
        }



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

}
