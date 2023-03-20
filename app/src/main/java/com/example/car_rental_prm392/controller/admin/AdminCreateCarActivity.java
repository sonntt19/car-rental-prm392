package com.example.car_rental_prm392.controller.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class AdminCreateCarActivity extends AppCompatActivity {
    private EditText editName, editDescription, editPrice;
    private Button btnSave;
    private ImageView img;
    private ImageButton ibtnCamera, ibtnFolder;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    AutoCompleteTextView completeTextView;

    ArrayAdapter<String> adapterItems;
    ArrayList<Location> listLocations;

    int idLocation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_car);
        DBManager dbManager = new DBManager(this);

//        Get list location for Location name
        listLocations = dbManager.getAllLocation();
        String[] stringArray = new String[listLocations.size()];
        for (int i = 0; i < listLocations.size(); i++) {
            stringArray[i] = listLocations.get(i).getName();
        }

        init();

//        Set adapter and list select location
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, stringArray);
        completeTextView.setAdapter(adapterItems);

//        Click to select location for car
        completeTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Location o :
                        listLocations) {
                    if (o.getName().equalsIgnoreCase(stringArray[position]))
                        idLocation = o.getId();
                }
            }
        });

//        Set image
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

//      Click to save new car
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car = createCar();

                if (car != null) {
                    dbManager.addCar(car);
                    editName.setText("");
                    editDescription.setText("");
                    editPrice.setText("");
                    Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
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

    public void init() {
        editName = findViewById(R.id.add_car_name);
        editDescription = findViewById(R.id.add_car_description);
        editPrice = findViewById(R.id.add_car_price);
        btnSave = findViewById(R.id.add_car_save);
        ibtnCamera = findViewById(R.id.add_car_camera);
        ibtnFolder = findViewById(R.id.add_car_folder);
        img = findViewById((R.id.add_car_img_test));
        completeTextView = findViewById(R.id.admin_car_auto_complete);
    }

    private Car createCar() {
        byte[] image = null;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            image = byteArray.toByteArray();
            // use bitmap
        } else {
            image = null;
        }

        String name = editName.getText().toString();
        String description = editDescription.getText().toString();
        double price = Double.parseDouble(editPrice.getText().toString());

        Car car = new Car(name, description, price, image, 1, idLocation);
        return car;
    }
}