package com.example.car_rental_prm392.controller.common;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.controller.admin.AdminLocationDetailActivity;
import com.example.car_rental_prm392.controller.admin.AdminManagerActivity;
import com.example.car_rental_prm392.controller.common.HistoryActivity;
import com.example.car_rental_prm392.controller.data_local.DataLocalManager;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Location;
import com.example.car_rental_prm392.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    private CircleImageView cImg;
    private TextView tvName, tvEmail;
    private EditText editName, editPhone, editAddress;
    private Button btnSave, btnReset;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    User user = DataLocalManager.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        DBManager dbManager = new DBManager(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_bot_profile);

        //        Account Information

        init();
        if (user != null) {
            if (user.getAvatar() != null) {
                byte[] image = user.getAvatar();
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                cImg.setImageBitmap(bitmap);
            }
            tvName.setText(user.getFullName());
            tvEmail.setText(user.getEmail());
            editName.setText(user.getFullName());
            editPhone.setText(user.getPhoneNumber());
            if (user.getAddress() != null) {
                editAddress.setText(user.getAddress());
            }
        }

//        Set Event

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userlast = createUser();

                if (userlast != null) {
                    dbManager.updateUser(userlast, user.getUserId());
                    Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_LONG).show();
                    user = dbManager.checkUserByEmail(user.getEmail());
                    DataLocalManager.removeUser();
                    DataLocalManager.setUser(user);
                    recreate();
                }
            }
        });

//        Click to choose Image
        cImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

//        Click to reset password Screen
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_bot_profile:
                    return true;
                case R.id.nav_bot_home:
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    return true;
                case R.id.nav_bot_history:
                    startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                    finish();
                    return true;
            }
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            cImg.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                cImg.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void chooseImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Avatar");
        builder.setMessage("Choose solution:");

        builder.setPositiveButton("Folder", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });
        builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void init() {
        cImg = findViewById(R.id.user_profile_img);
        tvName = findViewById(R.id.user_profile_name);
        tvEmail = findViewById(R.id.user_profile_email);
        editName = findViewById(R.id.edit_user_profile_name);
        editPhone = findViewById(R.id.edit_user_profile_phone);
        editAddress = findViewById(R.id.edit_user_profile_address);
        btnSave = findViewById(R.id.edit_user_profile_save);
        btnReset = findViewById(R.id.edit_user_profile_reset);
    }

    public User createUser() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) cImg.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] image = byteArray.toByteArray();

        String fullName = editName.getText().toString();
        String phoneNumber = editPhone.getText().toString();
        String address = editAddress.getText().toString();
        User user = new User(fullName, phoneNumber, address, image);
        return user;
    }
}