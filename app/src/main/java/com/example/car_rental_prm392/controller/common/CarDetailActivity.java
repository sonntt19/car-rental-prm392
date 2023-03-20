package com.example.car_rental_prm392.controller.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.controller.data_local.DataLocalManager;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Car;
import com.example.car_rental_prm392.model.Location;
import com.example.car_rental_prm392.model.Rental;
import com.example.car_rental_prm392.model.User;

import java.util.ArrayList;

public class CarDetailActivity extends AppCompatActivity {
    private TextView tvName, tvPrice, tvLocation, tvDescription;
    private ImageView img;
    private Button btnRent;

    ArrayList<Location> listLocations;
    User user = DataLocalManager.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        DBManager dbManager = new DBManager(this);

//        Get Car detail from intent
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Car car = (Car) bundle.get("car");

//        Get list location for location name
        listLocations = dbManager.getAllLocation();
        String location = "";
        for (Location o :
                listLocations) {
            if (o.getId() == car.getLocationId())
                location = o.getName();
        }

        init();

//        Set information fo Car detail
        tvName.setText(car.getName());
        tvPrice.setText(car.getPrice() + "");
        tvLocation.setText(location);
        tvDescription.setText(car.getDescription());
        if (car.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImage(), 0, car.getImage().length);
            img.setImageBitmap(bitmap);
        }

//        Set visibility of button is none
        Rental rental = dbManager.checkRentalForUser(car.getId(), user.getUserId());
        if (rental != null) {
            btnRent.setVisibility(View.GONE);

        }

//        Click to rent car
        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rental rental = new Rental(user.getUserId(), car.getId(), 0, 1);
                dbManager.addRental(rental);
                Toast.makeText(getApplicationContext(), "Rental Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CarDetailActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init() {
        img = findViewById(R.id.detail_car_img);
        tvName = findViewById(R.id.detail_car_name);
        tvPrice = findViewById(R.id.detail_car_price);
        tvLocation = findViewById(R.id.detail_car_location);
        tvDescription = findViewById(R.id.detail_car_description);
        btnRent = findViewById(R.id.btn_rent_car);

    }
}