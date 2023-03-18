package com.example.car_rental_prm392;

import android.app.Application;

import com.example.car_rental_prm392.controller.data_local.DataLocalManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
    }
}
