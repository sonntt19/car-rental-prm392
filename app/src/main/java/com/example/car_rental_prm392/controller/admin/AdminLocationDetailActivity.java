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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Location;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AdminLocationDetailActivity extends AppCompatActivity {
    private TextView tvId;
    private EditText editName, editDescription;
    private ImageView img;
    private ImageButton ibtnCamera, ibtnFolder;
    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;
    private Button btnDelete, btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_location_detail);
        DBManager dbManager = new DBManager(this);
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        Location location = (Location) bundle.get("location");

        init();


        tvId.setText(location.getId()+"");
        editName.setText(location.getName());
        editDescription.setText(location.getDescription());
        if(location.getImage()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(location.getImage(), 0, location.getImage().length);
            img.setImageBitmap(bitmap);
        }



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(location.getId());
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location locationLast = createLocation();

                if (locationLast!= null){
                    dbManager.updateLocation(locationLast,location.getId());
                    Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdminLocationDetailActivity.this, AdminManagerActivity.class);
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
        tvId = findViewById(R.id.detail_location_id);
        editName = findViewById(R.id.detail_location_name);
        editDescription = findViewById(R.id.detail_location_description);
        img = findViewById(R.id.detail_location_img_test);
        ibtnCamera = findViewById(R.id.detail_location_camera);
        ibtnFolder = findViewById(R.id.detail_location_folder);
        btnDelete = findViewById(R.id.detail_location_delete);
        btnUpdate = findViewById(R.id.detail_location_update);

    }
    public void delete(int id){
        DBManager dbManager = new DBManager(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Do you want delete?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbManager.deleteLocationById(id);
                dialog.dismiss();
                AdminLocationDetailActivity.this.recreate();
                Toast.makeText(getApplicationContext(), "Delete  Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdminLocationDetailActivity.this, AdminManagerActivity.class);
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

    private Location createLocation(){
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

        Location location = new Location(name, description,image);
        return location;
    }
}