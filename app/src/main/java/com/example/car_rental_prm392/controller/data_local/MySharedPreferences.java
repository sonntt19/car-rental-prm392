package com.example.car_rental_prm392.controller.data_local;


import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    private Context context;

    public MySharedPreferences(Context context){
        this.context = context;
    }

    public void putStringValue(String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public void removeStringValue(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public String getStringValue(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

}
