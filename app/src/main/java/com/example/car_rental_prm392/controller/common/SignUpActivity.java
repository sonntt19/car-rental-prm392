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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = createUser();
                if (user!=null){
                    dbManager.addUser(user);
                    Toast.makeText(getApplicationContext(), "Sign Up Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
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
    private Boolean validateName(){
        String val = editFullName.getEditText().getText().toString();
        String namePattern = "^[0-9-+()]*$";
        if (val.isEmpty()){
            editFullName.setError("Full name cannot be empty");
            return false;
        }
        else if(val.contains(namePattern)){
            editEmail.setError("Invalid name");
            return false;
        }
        else {
            editFullName.setError(null);
            editFullName.setErrorEnabled(false);
            return true;
        }
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
    private Boolean validatePhone(){
        String val = editPhoneNumber.getEditText().getText().toString();
        String phonePattern = "^(84|0[3|5|7|8|9])+([0-9]{8})\\b";
        if (val.isEmpty()){
            editPhoneNumber.setError("Phone cannot be empty");
            return false;
        }
        else if(!val.matches(phonePattern)){
            editPhoneNumber.setError("Invalid phone");
            return false;
        }
        else {
            editPhoneNumber.setError(null);
            editPhoneNumber.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword1(){
        String val = editPassword1.getEditText().getText().toString();
        if (val.isEmpty()){
            editPassword1.setError("Password cannot be empty");
            return false;
        }
        else if(val.length() <= 10){
            editPassword1.setError("Password too long");
            return false;
        }
        else {
            editPassword1.setError(null);
            editPassword1.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword2(){
        String val = editPassword2.getEditText().getText().toString();
        if (val.isEmpty()){
            editPassword2.setError("Password cannot be empty");
            return false;
        }
        else {
            editPassword2.setError(null);
            editPassword2.setErrorEnabled(false);
            return true;
        }
    }
    public User createUser(){
        if(!validateName() | !validateEmail() | !validatePhone() | !validatePassword1() | !validatePassword2()){
            return null;
        }
        String fullName = editFullName.getEditText().getText().toString();
        String email = editEmail.getEditText().getText().toString();
        String phoneNumber = editPhoneNumber.getEditText().getText().toString();
        String password1 = editPassword1.getEditText().getText().toString();
        String password2 = editPassword2.getEditText().getText().toString();
        User user = new User(email, password1, fullName, phoneNumber, 0);
        if(!password1.equals(password2)){
            Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_LONG).show();
            return null;
        }
        User userCheck = dbManager.checkUserByEmail(email);
        if (userCheck!=null){
            Toast.makeText(getApplicationContext(), "Email Existed", Toast.LENGTH_LONG).show();
            return null;
        }
        userCheck = dbManager.checkUserByPhoneNumber(phoneNumber);
        if (userCheck!=null){
            Toast.makeText(getApplicationContext(), "Phone number Existed", Toast.LENGTH_LONG).show();
            return null;
        }
        return user;
    }


}