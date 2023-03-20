package com.example.car_rental_prm392.controller.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.controller.data_local.DataLocalManager;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResetPasswordActivity extends AppCompatActivity {
    private CircleImageView cImg;
    private TextView tvName, tvEmail;
    private EditText editOldPass, editNewPass1, editNewPass2;
    private Button btnReset;
    User user = DataLocalManager.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        DBManager dbManager = new DBManager(this);
        init();

        if (user!=null){
            if(user.getAvatar()!=null){
                byte[] image = user.getAvatar();
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                cImg.setImageBitmap(bitmap);
            }
            tvName.setText(user.getFullName());
            tvEmail.setText(user.getEmail());

        }

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateOldPassword() | !validateNewPassword1() | !validateNewPassword2()){
                    return;
                }
                User userReset = dbManager.getUserById(user.getUserId());
                String oldPass = editOldPass.getText().toString();
                String newPass1 = editNewPass1.getText().toString();
                String newPass2 = editNewPass2.getText().toString();

                if (!oldPass.equals(userReset.getPassword())){
                    Toast.makeText(getApplicationContext(), "Old Password not correct", Toast.LENGTH_LONG).show();
                }else if(!newPass1.equals(newPass2)){
                    Toast.makeText(getApplicationContext(), "New Password not match", Toast.LENGTH_LONG).show();
                }else {
                    dbManager.updatePassword(newPass1, user.getUserId());
                    Toast.makeText(getApplicationContext(), "Reset Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void init(){
        cImg = findViewById(R.id.reset_password_img);
        tvName = findViewById(R.id.reset_password_name);
        tvEmail = findViewById(R.id.reset_password_email);
        editOldPass = findViewById(R.id.reset_password_oldPass);
        editNewPass1 = findViewById(R.id.reset_password_newPass1);
        editNewPass2 = findViewById(R.id.reset_password_newPass2);
        btnReset = findViewById(R.id.btn_reset_password);
    }
    private Boolean validateOldPassword(){
        String val = editOldPass.getText().toString();
        if (val.isEmpty()){
            editOldPass.setError("Old password cannot be empty");
            return false;
        }
        else {
            editOldPass.setError(null);
            return true;
        }
    }
    private Boolean validateNewPassword1(){
        String val = editNewPass1.getText().toString();
        if (val.isEmpty()){
            editNewPass1.setError("New password 1 cannot be empty");
            return false;
        }
        else {
            editNewPass1.setError(null);
            return true;
        }
    }
    private Boolean validateNewPassword2(){
        String val = editOldPass.getText().toString();
        if (val.isEmpty()){
            editNewPass2.setError("New password 2 cannot be empty");
            return false;
        }
        else {
            editNewPass2.setError(null);
            return true;
        }
    }
}