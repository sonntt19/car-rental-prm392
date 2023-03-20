package com.example.car_rental_prm392.controller.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.User;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout editFullName, editEmail, editPhoneNumber, editPassword1, editPassword2;
    private Button btnSignUp, btnBack;

    DBManager dbManager = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        init();

//        Click to Sign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = createUser();
                if (user != null) {
                    dbManager.addUser(user);
                    Toast.makeText(getApplicationContext(), "Sign Up Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

//        Click to back to login
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init() {
        editFullName = findViewById(R.id.fullName);
        editEmail = findViewById(R.id.email);
        editPhoneNumber = findViewById(R.id.phoneNumber);
        editPassword1 = findViewById(R.id.password1);
        editPassword2 = findViewById(R.id.password2);
        btnSignUp = findViewById(R.id.signup);
        btnBack = findViewById(R.id.back_signin);
    }

    public User createUser() {
        String fullName = editFullName.getEditText().getText().toString();
        String email = editEmail.getEditText().getText().toString();
        String phoneNumber = editPhoneNumber.getEditText().getText().toString();
        String password1 = editPassword1.getEditText().getText().toString();
        String password2 = editPassword2.getEditText().getText().toString();
        User user = new User(email, password1, fullName, phoneNumber, 0);
        if (!password1.equals(password2)) {
            Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_LONG).show();
            return null;
        }
        User userCheck = dbManager.checkUserByEmail(email);
        if (userCheck != null) {
            Toast.makeText(getApplicationContext(), "Email Existed", Toast.LENGTH_LONG).show();
            return null;
        }
        userCheck = dbManager.checkUserByPhoneNumber(phoneNumber);
        if (userCheck != null) {
            Toast.makeText(getApplicationContext(), "Phone number Existed", Toast.LENGTH_LONG).show();
            return null;
        }
        return user;
    }
}