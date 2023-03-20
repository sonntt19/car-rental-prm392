package com.example.car_rental_prm392.controller.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.controller.admin.AdminManagerActivity;
import com.example.car_rental_prm392.controller.data_local.DataLocalManager;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Car;
import com.example.car_rental_prm392.model.Location;
import com.example.car_rental_prm392.model.Rental;
import com.example.car_rental_prm392.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DetailHistoryActivity extends AppCompatActivity {
    private TextView tvId, tvCustomer, tvCar, tvStartDate, tvEndDate, tvTotal, tvStatus;
    private Button btnCancel, btnDeal, btnFinish;
    private User user = DataLocalManager.getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);
        DBManager dbManager = new DBManager(this);
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        Rental rental = (Rental) bundle.get("rental");
        init();
        User customer = dbManager.getUserById(rental.getCustomerId());
        Car car = dbManager.getCarById(rental.getCarId());;
        String status = "";
        tvId.setText(rental.getId()+"");
        tvCustomer.setText(customer.getFullName());
        tvCar.setText(car.getName());
        if(rental.getStartDate()!=null){
            tvStartDate.setText(rental.getStartDate());
        }
        if(rental.getEndDate()!=null){
            tvEndDate.setText(rental.getEndDate());
        }
        tvTotal.setText(rental.getTotalCost()+"");

        if (rental.getStatus()==1)
            status = "Waiting";
        else if (rental.getStatus()==2)
            status = "Renting";
        else if (rental.getStatus()==3)
            status = "End";
        else if (rental.getStatus()==4)
            status = "Cancel";
        tvStatus.setText(status);
        if (rental.getStatus()!=1){
            btnCancel.setVisibility(View.GONE);
        }
        if (user.getRoleId()==1 && rental.getStatus()==1){
            btnDeal.setVisibility(View.VISIBLE);
        }
        if (user.getRoleId()==1 && rental.getStatus()==2){
            btnDeal.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);
        }


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.cancelRetal(rental.getId());
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                if (user.getRoleId()==0){
                    Intent intent = new Intent(DetailHistoryActivity.this, HistoryActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(DetailHistoryActivity.this, AdminManagerActivity.class);
                    startActivity(intent);
                }

            }
        });

        btnDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.dealRetal(rental);
                Toast.makeText(getApplicationContext(), "Deal", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DetailHistoryActivity.this, AdminManagerActivity.class);
                startActivity(intent);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDateTime now = LocalDateTime.now();

                String start = rental.getStartDate();
                String end = dtf.format(now);
                int diff=1+daysBetweenDates(start,end);
                Double total = car.getPrice() * diff;

                dbManager.finishRetal(rental.getId(),end, total);
                Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DetailHistoryActivity.this, AdminManagerActivity.class);
                startActivity(intent);
            }
        });


    }

    public void init(){
        tvId = findViewById(R.id.history_detail_id);
        tvCustomer = findViewById(R.id.history_detail_customer);
        tvCar = findViewById(R.id.history_detail_car);
        tvStartDate = findViewById(R.id.history_detail_startDate);
        tvEndDate = findViewById(R.id.history_detail_endDate);
        tvTotal = findViewById(R.id.history_detail_total);
        tvStatus = findViewById(R.id.history_detail_status);
        btnCancel = findViewById(R.id.btn_cancel);
        btnDeal = findViewById(R.id.btn_deal);
        btnFinish = findViewById(R.id.btn_finish);
    }

    public static int daysBetweenDates(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date start = null;
        Date end = null;
        try {
            start = dateFormat.parse(startDate);
            end = dateFormat.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = end.getTime() - start.getTime();
        return (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
    }


}