package com.example.car_rental_prm392.controller.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Car;
import com.example.car_rental_prm392.model.Location;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AdminCarDetailActivity extends AppCompatActivity {
    private TextView tvId;
    private EditText editName, editDescription, editPrice;
    private ImageView img;
    private ImageButton ibtnCamera, ibtnFolder;
    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;
    private Button btnDelete, btnUpdate;
    AutoCompleteTextView completeTextView;
    ArrayAdapter<String> adapterItems;
    ArrayList<Location> listLocations;
    int idLocation = 0;
    String selected;
    TextInputLayout textInputLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_car_detail);
        DBManager dbManager = new DBManager(this);
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        Car car = (Car) bundle.get("car");
        listLocations = dbManager.getAllLocation();
        String[] stringArray = new String[listLocations.size()];


        for (int i = 0; i < listLocations.size(); i++) {
            stringArray[i] = listLocations.get(i).getName();
        }
        for (Location o:
                listLocations) {
            if(o.getId()== car.getLocationId())
                selected = o.getName();
        }
        init();
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item,stringArray);
        completeTextView.setAdapter(adapterItems);
        textInputLayout.setHint(selected);
        completeTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Location o:
                        listLocations) {
                    if(o.getName().equalsIgnoreCase(stringArray[position]))
                        idLocation = o.getId();
                }
            }
        });

        tvId.setText(car.getId()+"");
        editName.setText(car.getName());
        editDescription.setText(car.getDescription());
        editPrice.setText(car.getPrice()+"");
        if(car.getImage()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImage(), 0, car.getImage().length);
            img.setImageBitmap(bitmap);
        }


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(car.getId());
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car carLast = createCar();
                if(!validateCarName() | !validateDescription() | !validatePrice()){
                    return;
                }
                if (carLast!= null){
                    dbManager.updateCar(carLast,car.getId());
                    Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdminCarDetailActivity.this, AdminManagerActivity.class);
                    startActivity(intent);
                }
            }
        });
        ibtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        ibtnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
        }
        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init(){
        tvId = findViewById(R.id.detail_car_id);
        editName = findViewById(R.id.detail_car_name);
        editDescription = findViewById(R.id.detail_car_description);
        editPrice= findViewById(R.id.detail_car_price);
        img = findViewById(R.id.detail_car_img_test);
        ibtnCamera = findViewById(R.id.detail_car_camera);
        ibtnFolder = findViewById(R.id.detail_car_folder);
        btnDelete = findViewById(R.id.detail_car_delete);
        btnUpdate = findViewById(R.id.detail_car_update);
        completeTextView = findViewById(R.id.admin_car_auto_complete);
        textInputLayout = findViewById(R.id.admin_car_select);


    }
    private Boolean validateCarName(){
        String val = editName.getText().toString();
        if (val.isEmpty()){
            editName.setError("Car name cannot be empty");
            return false;
        }
        else {
            editName.setError(null);
            return true;
        }
    }
    private Boolean validateDescription(){
        String val = editDescription.getText().toString();
        if (val.isEmpty()){
            editDescription.setError("Description cannot be empty");
            return false;
        }
        else {
            editDescription.setError(null);
            return true;
        }
    }
    private Boolean validatePrice(){
        String val = editPrice.getText().toString();
        if (val.isEmpty()){
            editPrice.setError("Price cannot be empty");
            return false;
        }
        else {
            editPrice.setError(null);
            return true;
        }
    }

    public void delete(int id){
        DBManager dbManager = new DBManager(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Do you want delete?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbManager.deleteCarById(id);
                dialog.dismiss();
                AdminCarDetailActivity.this.recreate();
                Toast.makeText(getApplicationContext(), "Delete  Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdminCarDetailActivity.this, AdminManagerActivity.class);
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

    private Car createCar(){
        byte[] image = null;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArray);
            image = byteArray.toByteArray();
            // sử dụng bitmap
        } else {
            image = null;
        }

        String name = editName.getText().toString();
        String description = editDescription.getText().toString();
        double price = Double.parseDouble(editPrice.getText().toString());

        Car car = new Car(name,description,price,image,1,idLocation);
        return car;
    }
}