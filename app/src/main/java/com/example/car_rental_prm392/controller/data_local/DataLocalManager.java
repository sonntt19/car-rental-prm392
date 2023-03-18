package com.example.car_rental_prm392.controller.data_local;

import android.content.Context;

import com.example.car_rental_prm392.model.User;
import com.google.gson.Gson;

public class DataLocalManager {
    private static final String OBJECT_USER = "OBJECT_USER";
    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance(){
        if(instance==null){
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setUser(User user){
        Gson gson = new Gson();
        String strJsonUser = gson.toJson(user);
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(OBJECT_USER, strJsonUser);
    }

    public static User getUser(){
        String strJsonUser = DataLocalManager.getInstance().mySharedPreferences.getStringValue(OBJECT_USER);
        Gson gson = new Gson();
        User user = gson.fromJson(strJsonUser, User.class);
        return user;
    }

    public static void removeUser(){
        DataLocalManager.getInstance().mySharedPreferences.removeStringValue(OBJECT_USER);
    }
}
