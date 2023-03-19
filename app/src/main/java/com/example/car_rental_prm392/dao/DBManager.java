package com.example.car_rental_prm392.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import com.example.car_rental_prm392.model.Car;
import com.example.car_rental_prm392.model.Location;
import com.example.car_rental_prm392.model.Rental;
import com.example.car_rental_prm392.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "car_rental_manager_v2";
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
    private static final String CAR_ID = "carId";
    private static final String CAR_NAME = "name";
    private static final String CAR_DESCRIPTION = "description";
    private static final String CAR_PRICE = "price";
    private static final String CAR_IMAGE = "image";
    private static final String CAR_STATUS = "status";
    private static final String CAR_LOCATION_ID = "locationId";

    private static final String RENTAL_TABLE_NAME = "rentals";
    private static final String RENTAL_ID = "rentalId";
    private static final String RENTAL_CUSTOMER_ID = "customerId";
    private static final String RENTAL_CAR_ID = "carId";
    private static final String RENTAL_START_DATE = "startDate";
    private static final String RENTAL_END_DATE = "endDate";
    private static final String RENTAL_TOTAL_COST = "totalCost";
    private static final String RENTAL_STATUS = "status";


    private static int VERSION = 5 ;

    private Context context;
    //    Create table
    private String SQLCreateUser = "CREATE TABLE " + USER_TABLE_NAME + " (" +
            USER_ID + " INTEGER primary key, " +
            USER_EMAIL + " TEXT, " +
            USER_PASSWORD + " TEXT, " +
            USER_FULL_NAME + " TEXT, " +
            USER_PHONE_NUMBER + " TEXT, " +
            USER_ADDRESS + " TEXT," +
            USER_AVATAR + " BLOB," +
            USER_ROLE_ID + " INTEGER) ";


    private String SQLCreateLocation = "CREATE TABLE " + LOCATION_TABLE_NAME + " (" +
            LOCATION_ID + " INTEGER primary key, " +
            LOCATION_NAME + " TEXT, " +
            LOCATION_DESCRIPTION + " TEXT," +
            LOCATION_AVATAR + " BLOB) ";

    private String SQLCreateCar = "CREATE TABLE " + CAR_TABLE_NAME + " (" +
            CAR_ID + " INTEGER primary key, " +
            CAR_NAME + " TEXT, " +
            CAR_DESCRIPTION + " TEXT, " +
            CAR_PRICE + " REAL, " +
            CAR_IMAGE + " BLOB, " +
            CAR_STATUS + " INTEGER," +
            CAR_LOCATION_ID + " INTEGER)";

    private String SQLCreateRental = "CREATE TABLE " + RENTAL_TABLE_NAME + " (" +
            RENTAL_ID + " INTEGER primary key, " +
            RENTAL_CUSTOMER_ID + " INTEGER, " +
            RENTAL_CAR_ID + " INTEGER, " +
            RENTAL_START_DATE + " TEXT, " +
            RENTAL_END_DATE + " TEXT, " +
            RENTAL_TOTAL_COST + " REAL," +
            RENTAL_STATUS + " INTEGER)";


    public DBManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLCreateLocation);
        db.execSQL(SQLCreateCar);
        db.execSQL(SQLCreateUser);
        db.execSQL(SQLCreateRental);
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

    public ArrayList<User> getAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<User> listUser = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + USER_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
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

                listUser.add(user);
            } while (cursor.moveToNext());
        }
        db.close();
        return listUser;
    }

    public User getUserById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + USER_TABLE_NAME +" WHERE "+USER_ID+"="+id;
        Cursor cursor = db.rawQuery(selectQuery, null);
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

    public int changeRoleById(int roleId, int id) {
        if (roleId == 1) {
            roleId = 0;
        } else {
            roleId = 1;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ROLE_ID, roleId);

        int check = db.update(USER_TABLE_NAME, values, USER_ID + "=?", new String[]{id + ""});
        db.close();
        return check;
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

    public int updateUser(User user, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_FULL_NAME, user.getFullName());
        values.put(USER_PHONE_NUMBER, user.getPhoneNumber());
        values.put(USER_ADDRESS, user.getAddress());
        values.put(USER_AVATAR, user.getAvatar());

        int check = db.update(USER_TABLE_NAME, values, USER_ID + "=?", new String[]{id + ""});
        db.close();
        return check;
    }

    public int updatePassword(String password, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_PASSWORD, password);

        int check = db.update(USER_TABLE_NAME, values, USER_ID + "=?", new String[]{id + ""});
        db.close();
        return check;
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
        String selectQuery = "SELECT * FROM "+LOCATION_TABLE_NAME;
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

    public int deleteLocationById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int check = db.delete(LOCATION_TABLE_NAME, LOCATION_ID + "=?", new String[]{id + ""});
        return check;
    }

    public int updateLocation(Location location, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOCATION_NAME, location.getName());
        values.put(LOCATION_DESCRIPTION, location.getDescription());
        values.put(LOCATION_AVATAR, location.getImage());

        int check = db.update(LOCATION_TABLE_NAME, values, LOCATION_ID + "=?", new String[]{id + ""});
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


    //        Car DAO
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
        String selectQuery = "SELECT * FROM " + CAR_TABLE_NAME;
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
    public Car getCarById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + CAR_TABLE_NAME +" WHERE "+CAR_ID+"="+id;
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
                return car;
            } while (cursor.moveToNext());
        }
        db.close();
        return null;
    }

    public ArrayList<Car> getAllCarAvailable() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Car> listCar = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CAR_TABLE_NAME + " WHERE " + CAR_STATUS + "=1";
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

    public ArrayList<Car> getAllCarAvailableForLocation(int idLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Car> listCar = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CAR_TABLE_NAME + " WHERE " + CAR_STATUS + "=1 AND " + CAR_LOCATION_ID + "=" + idLocation;
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

    public int deleteCarById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int check = db.delete(CAR_TABLE_NAME, CAR_ID + "=?", new String[]{id + ""});
        return check;
    }

    public int updateCar(Car car, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAR_NAME, car.getName());
        values.put(CAR_DESCRIPTION, car.getDescription());
        values.put(CAR_PRICE, car.getPrice());
        values.put(CAR_IMAGE, car.getImage());
        values.put(CAR_LOCATION_ID, car.getLocationId());

        int check = db.update(CAR_TABLE_NAME, values, CAR_ID + "=?", new String[]{id + ""});
        db.close();
        return check;
    }

    //    Rental DAO
    public void addRental(Rental rental) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RENTAL_CUSTOMER_ID, rental.getCustomerId());
        values.put(RENTAL_CAR_ID, rental.getCarId());
        values.put(RENTAL_TOTAL_COST, rental.getTotalCost());
        values.put(RENTAL_STATUS, rental.getStatus());
        db.insert(RENTAL_TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Rental> getAllRental() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Rental> listRental = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + RENTAL_TABLE_NAME +" ORDER BY "+RENTAL_STATUS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Rental rental = new Rental();
                rental.setId(cursor.getInt(0));
                rental.setCustomerId(cursor.getInt(1));
                rental.setCarId(cursor.getInt(2));
                rental.setStartDate(cursor.getString(3));
                rental.setEndDate(cursor.getString(4));
                rental.setTotalCost(cursor.getDouble(5));
                rental.setStatus(cursor.getInt(6));
                listRental.add(rental);
            } while (cursor.moveToNext());
        }
        db.close();
        return listRental;
    }
    public ArrayList<Rental> getAllRentalByUserId(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Rental> listRental = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + RENTAL_TABLE_NAME +" WHERE "+RENTAL_CUSTOMER_ID+"="+userId +" ORDER BY "+RENTAL_STATUS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Rental rental = new Rental();
                rental.setId(cursor.getInt(0));
                rental.setCustomerId(cursor.getInt(1));
                rental.setCarId(cursor.getInt(2));
                rental.setStartDate(cursor.getString(3));
                rental.setEndDate(cursor.getString(4));
                rental.setTotalCost(cursor.getDouble(5));
                rental.setStatus(cursor.getInt(6));
                listRental.add(rental);
            } while (cursor.moveToNext());
        }
        db.close();
        return listRental;
    }

    public Rental checkRentalForUser(int carId,int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + RENTAL_TABLE_NAME
                +" WHERE "+RENTAL_CAR_ID +"="+carId+" AND "
                +RENTAL_CUSTOMER_ID+"="+userId
                +" AND "+ RENTAL_STATUS+"=1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Rental rental = new Rental();
                rental.setId(cursor.getInt(0));
                rental.setCustomerId(cursor.getInt(1));
                rental.setCarId(cursor.getInt(2));
                rental.setStartDate(cursor.getString(3));
                rental.setEndDate(cursor.getString(4));
                rental.setTotalCost(cursor.getDouble(5));
                rental.setStatus(cursor.getInt(6));
                return rental;
            } while (cursor.moveToNext());
        }
        db.close();
        return null;
    }

    public int cancelRetal(int retalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RENTAL_STATUS, 4);

        int check = db.update(RENTAL_TABLE_NAME, values, RENTAL_ID + "=?", new String[]{retalId + ""});
        db.close();
        return check;
    }
    public int dealRetal(int retalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        values.put(RENTAL_STATUS, 2);
        values.put(RENTAL_START_DATE,dtf.format(now));
        int check = db.update(RENTAL_TABLE_NAME, values, RENTAL_ID + "=?", new String[]{retalId + ""});
        db.close();
        return check;
    }
    public int finishRetal(int retalId,String end, double total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RENTAL_STATUS, 3);
        values.put(RENTAL_END_DATE, end);
        values.put(RENTAL_TOTAL_COST, total);

        int check = db.update(RENTAL_TABLE_NAME, values, RENTAL_ID + "=?", new String[]{retalId + ""});
        db.close();
        return check;
    }
}
