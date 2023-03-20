package com.example.car_rental_prm392.controller.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.controller.admin.AdminManagerActivity;
import com.example.car_rental_prm392.controller.data_local.DataLocalManager;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.User;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    Button btnSignUp, btnLogin;
    ImageView imgLogo;
    TextView tvLogoText;
    TextInputLayout editEmail, editPassword;
    RadioGroup radioGroup;
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
                if(!validateEmail() | !validatePassword()){
                    return;
                }
                String email = editEmail.getEditText().getText().toString();
                String password = editPassword.getEditText().getText().toString();
                User user = dbManager.checkUser(email, password);
                if (user != null) {
                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    String selectedValue = selectedRadioButton.getText().toString();
                    if (selectedValue.equalsIgnoreCase("customer") && user.getRoleId() == 0) {
                        DataLocalManager.setUser(user);
                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    if (selectedValue.equalsIgnoreCase("admin") && user.getRoleId() == 1) {
                        DataLocalManager.setUser(user);
                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, AdminManagerActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Email or Password incorrect", Toast.LENGTH_LONG).show();
                    }

                }

                else {
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
    }

    public void init() {
        editEmail = findViewById(R.id.login_email);
        editPassword = findViewById(R.id.login_password);
        btnSignUp = findViewById(R.id.login_signup_screen);
        btnLogin = findViewById(R.id.login);
        imgLogo = findViewById(R.id.logo);
        tvLogoText = findViewById(R.id.logo_text);
        radioGroup = findViewById(R.id.login_radio);
    }
    private Boolean validateEmail(){
        String val = editEmail.getEditText().getText().toString();
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (val.isEmpty()){
            editEmail.setError("Email cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            editEmail.setError("Invalid email");
            return false;
        }
        else {
            editEmail.setError(null);
            editEmail.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword(){
        String val = editPassword.getEditText().getText().toString();
        if (val.isEmpty()){
            editPassword.setError("Password cannot be empty");
            return false;
        }
        else {
            editPassword.setError(null);
            editPassword.setErrorEnabled(false);
            return true;
        }
    }
}