package com.example.car_rental_prm392.controller.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Location;
import com.example.car_rental_prm392.model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AdminCreateLocationActivity extends AppCompatActivity {

    private EditText editName, editDescription;
    private Button btnSave;
    private ImageView img;
    private ImageButton ibtnCamera, ibtnFolder;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_location);
        DBManager dbManager = new DBManager(this);

        init();

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

//      Click to save new location
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = createLocation();

                if (location != null) {
                    dbManager.addLocation(location);
                    editName.setText("");
                    editDescription.setText("");
                    img.setImageBitmap(null);
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
        editName = findViewById(R.id.add_location_name);
        editDescription = findViewById(R.id.add_location_description);
        btnSave = findViewById(R.id.add_location_save);
        ibtnCamera = findViewById(R.id.add_location_camera);
        ibtnFolder = findViewById(R.id.add_location_folder);
        img = findViewById((R.id.add_location_img_test));
    }
    private Location createLocation(){
        if(!validateLocationName() | !validateDescription() | !validateImage()){
            return null;
        }


        DBManager dbManager = new DBManager(this);
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

        Location locationCheck = dbManager.checkLocationByName(name);
        if (locationCheck != null) {
            Toast.makeText(getApplicationContext(), "Location name Existed", Toast.LENGTH_LONG).show();
            return null;
        }
        Location location = new Location(name, description, image);
        return location;
    }
    private Boolean validateLocationName(){
        String val = editName.getText().toString();
        if (val.isEmpty()){
            editName.setError("Location name cannot be empty");
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
    private Boolean validateImage(){
        Drawable drawable = img.getDrawable();
        if (drawable == null) {
            Toast.makeText(this, "Image cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}