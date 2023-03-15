package com.example.car_rental_prm392.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;


import com.example.car_rental_prm392.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
    private final String TAG = "DBManager";
    private static final String DATABASE_NAME = "car_rental_manager";
    private static int VERSION = 1;

    private Context context;
    private String SQLCreateUser = "CREATE TABLE users ("+
            "userId INTEGER primary key, "+
            "email TEXT, "+
            "password TEXT, "+
            "fullName TEXT, "+
            "phoneNumber TEXT, "+
            "address TEXT,"+
            "avatar BLOB,"+
            "roleId INTEGER)";

    public DBManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
        Log.d(TAG, "DBManager");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLCreateUser);
        Log.d(TAG, "OnCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email",user.getEmail());
        values.put("password",user.getPassword());
        values.put("fullName", user.getFullName());
        values.put("phoneNumber",user.getPhoneNumber());
        values.put("roleId", user.getRoleId());
        db.insert("users", null, values);
        db.close();
        Log.d(TAG, "Add successfully");
    }

    public User checkUserByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM users WHERE email = ?";
        Cursor cursor = db.rawQuery(selectQuery,new String[]{email});
        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setUserId(cursor.getInt(0));
                user.setEmail(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setFullName(cursor.getString(3));
                user.setPhoneNumber(cursor.getString(4));
                user.setAddress(cursor.getString(5));
                user.setAvatar(cursor.getBlob(6));
                user.setRoleId(cursor.getInt(7));
                return user;
            }while (cursor.moveToNext());
        }
        db.close();
        return null;
    }

    public User checkUserByPhoneNumber(String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM users WHERE phoneNumber = ?";
        Cursor cursor = db.rawQuery(selectQuery,new String[]{phoneNumber});
        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setUserId(cursor.getInt(0));
                user.setEmail(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setFullName(cursor.getString(3));
                user.setPhoneNumber(cursor.getString(4));
                user.setAddress(cursor.getString(5));
                user.setAvatar(cursor.getBlob(6));
                user.setRoleId(cursor.getInt(7));
                return user;
            }while (cursor.moveToNext());
        }
        db.close();
        return null;
    }

    public User checkUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM users WHERE email = ? and password = ?";
        Cursor cursor = db.rawQuery(selectQuery,new String[]{email,password});
        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setUserId(cursor.getInt(0));
                user.setEmail(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setFullName(cursor.getString(3));
                user.setPhoneNumber(cursor.getString(4));
                user.setAddress(cursor.getString(5));
                user.setAvatar(cursor.getBlob(6));
                user.setRoleId(cursor.getInt(7));
                return user;
            }while (cursor.moveToNext());
        }
        db.close();
        return null;
    }
}
