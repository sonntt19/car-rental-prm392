package com.example.car_rental_prm392.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.User;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    Button btnSignUp, btnLogin, btnForget;
    ImageView imgLogo;
    TextView tvLogoText;
    TextInputLayout editEmail, editPassword;
    DBManager dbManager = new DBManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getEditText().getText().toString();
                String password = editPassword.getEditText().getText().toString();
                User user = dbManager.checkUser(email,password);
                if (user!=null){
                    Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Email or Password incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
            }
        });
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init() {
        editEmail = findViewById(R.id.login_email);
        editPassword = findViewById(R.id.login_password);
        btnSignUp = findViewById(R.id.login_signup_screen);
        btnLogin = findViewById(R.id.login);
        btnForget = findViewById(R.id.forget);
        imgLogo = findViewById(R.id.logo);
        tvLogoText = findViewById(R.id.logo_text);
    }
}