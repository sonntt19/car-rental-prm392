package com.example.car_rental_prm392.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import com.example.car_rental_prm392.model.Car;
import com.example.car_rental_prm392.model.Location;
import com.example.car_rental_prm392.model.User;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "car_rental_manager_v6";
    private static final String USER_TABLE_NAME = "users";
    private static final String USER_ID = "id";
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";
    private static final String USER_FULL_NAME = "fullName";
    private static final String USER_PHONE_NUMBER = "phoneNumber";
    private static final String USER_ADDRESS = "address";
    private static final String USER_AVATAR = "avatar";
    private static final String USER_ROLE_ID = "roleId";

    private static final String LOCATION_TABLE_NAME = "locations";
    private static final String LOCATION_ID = "locationId";
    private static final String LOCATION_NAME = "name";
    private static final String LOCATION_DESCRIPTION = "description";
    private static final String LOCATION_AVATAR = "image";

    private static final String CAR_TABLE_NAME = "cars";
    private static final String CAR_ID = "locationId";
    private static final String CAR_NAME = "name";
    private static final String CAR_DESCRIPTION = "description";
    private static final String CAR_PRICE = "price";
    private static final String CAR_IMAGE = "image";
    private static final String CAR_STATUS = "status";
    private static final String CAR_LOCATION_ID = "locationIdCar";

    private static int VERSION = 3;

    private Context context;
    //    Create table
    private String SQLCreateUser = "CREATE TABLE "+ USER_TABLE_NAME +" ("+
            USER_ID +" INTEGER primary key, " +
            USER_EMAIL+" TEXT, " +
            USER_PASSWORD+" TEXT, " +
            USER_FULL_NAME+" TEXT, " +
            USER_PHONE_NUMBER+" TEXT, " +
            USER_ADDRESS+" TEXT," +
            USER_AVATAR+" BLOB," +
            USER_ROLE_ID+" INTEGER) ";


    private String SQLCreateLocation ="CREATE TABLE "+ LOCATION_TABLE_NAME +" (" +
            LOCATION_ID+" INTEGER primary key, " +
            LOCATION_NAME+" TEXT, " +
            LOCATION_DESCRIPTION+" TEXT," +
            LOCATION_AVATAR+" BLOB) " ;

    private String SQLCreateCar = "CREATE TABLE "+CAR_TABLE_NAME+" (" +
            CAR_ID+" INTEGER primary key, " +
            CAR_NAME+" TEXT, " +
            CAR_DESCRIPTION+" TEXT, " +
            CAR_PRICE+" REAL, " +
            CAR_IMAGE+" BLOB, " +
            CAR_STATUS+" INTEGER," +
            CAR_LOCATION_ID+" INTEGER)";


    public DBManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLCreateLocation);
        db.execSQL(SQLCreateCar);
        db.execSQL(SQLCreateUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //    User DAO
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("fullName", user.getFullName());
        values.put("phoneNumber", user.getPhoneNumber());
        values.put("roleId", user.getRoleId());
        db.insert("users", null, values);
        db.close();
    }

    public User checkUserByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM users WHERE email = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email});
        if (cursor.moveToFirst()) {
            do {
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
            } while (cursor.moveToNext());
        }
        db.close();
        return null;
    }

    public User checkUserByPhoneNumber(String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM users WHERE phoneNumber = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{phoneNumber});
        if (cursor.moveToFirst()) {
            do {
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
            } while (cursor.moveToNext());
        }
        db.close();
        return null;
    }

    public User checkUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM users WHERE email = ? and password = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email, password});
        if (cursor.moveToFirst()) {
            do {
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
            } while (cursor.moveToNext());
        }
        db.close();
        return null;
    }

    //    Location DAO
    public void addLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", location.getName());
        values.put("description", location.getDescription());
        values.put("image", location.getImage());
        db.insert("locations", null, values);
        db.close();
    }

    public ArrayList<Location> getAllLocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Location> listLocation = new ArrayList<>();
        String selectQuery = "SELECT * FROM locations";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Location location = new Location();
                location.setId(cursor.getInt(0));
                location.setName(cursor.getString(1));
                location.setDescription(cursor.getString(2));
                location.setImage(cursor.getBlob(3));
                listLocation.add(location);
            } while (cursor.moveToNext());
        }
        db.close();
        return listLocation;
    }
    public int deleteLocationById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int check = db.delete(LOCATION_TABLE_NAME, LOCATION_ID+"=?", new String[]{id+""});
        return check;
    }

    public int updateLocation(Location location, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOCATION_NAME,location.getName());
        values.put(LOCATION_DESCRIPTION,location.getDescription());
        values.put(LOCATION_AVATAR,location.getImage());

        int check = db.update(LOCATION_TABLE_NAME, values, LOCATION_ID+"=?", new String[]{id+""});
        db.close();
        return check;
    }

    public Location checkLocationByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM locations WHERE name = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{name});
        if (cursor.moveToFirst()) {
            do {
                Location location = new Location();
                location.setId(cursor.getInt(0));
                location.setName(cursor.getString(1));
                location.setDescription(cursor.getString(2));
                location.setImage(cursor.getBlob(3));
                return location;
            } while (cursor.moveToNext());
        }
        db.close();
        return null;
    }


//        Location Car
    public void addCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAR_NAME, car.getName());
        values.put(CAR_DESCRIPTION, car.getDescription());
        values.put(CAR_PRICE, car.getPrice());
        values.put(CAR_IMAGE, car.getImage());
        values.put(CAR_STATUS, car.getStatus());
        values.put(CAR_LOCATION_ID, car.getLocationId());
        db.insert(CAR_TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Car> getAllCar() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Car> listCar = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+CAR_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Car car = new Car();
                car.setId(cursor.getInt(0));
                car.setName(cursor.getString(1));
                car.setDescription(cursor.getString(2));
                car.setPrice(cursor.getDouble(3));
                car.setImage(cursor.getBlob(4));
                car.setStatus(cursor.getInt(5));
                car.setLocationId(cursor.getInt(6));
                listCar.add(car);
            } while (cursor.moveToNext());
        }
        db.close();
        return listCar;
    }
    public int deleteCarById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int check = db.delete(CAR_TABLE_NAME, CAR_ID+"=?", new String[]{id+""});
        return check;
    }

    public int updateCar(Car car, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAR_NAME,car.getName());
        values.put(CAR_DESCRIPTION,car.getDescription());
        values.put(CAR_PRICE,car.getPrice());
        values.put(CAR_IMAGE,car.getImage());
        values.put(CAR_LOCATION_ID,car.getLocationId());

        int check = db.update(CAR_TABLE_NAME, values, CAR_ID+"=?", new String[]{id+""});
        db.close();
        return check;
    }
}
