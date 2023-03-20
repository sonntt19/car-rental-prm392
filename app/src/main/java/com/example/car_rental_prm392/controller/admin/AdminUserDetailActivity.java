package com.example.car_rental_prm392.controller.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.car_rental_prm392.controller.common.LoginActivity;
import com.example.car_rental_prm392.controller.data_local.DataLocalManager;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Location;
import com.example.car_rental_prm392.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminUserDetailActivity extends AppCompatActivity {
    private TextView tvId, tvEmail, tvName, tvPhone, tvAddress, tvRole;
    private CircleImageView cImg;
    private Button btnChange;
    private User admin = DataLocalManager.getUser();
    DBManager dbManager = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_detail);

//        Get user detail from intent
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        User user = (User) bundle.get("user");

        init();

//        Set information for user detail
        tvId.setText(user.getUserId() + "");
        tvEmail.setText(user.getEmail());
        tvName.setText(user.getFullName());
        tvPhone.setText(user.getPhoneNumber());
        if (user.getAddress() != null) {
            tvAddress.setText(user.getAddress());
        }
        if (user.getRoleId() == 0) {
            tvRole.setText("Customer");
        } else {
            tvRole.setText("Admin");
        }
        if (user.getAvatar() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
            cImg.setImageBitmap(bitmap);
        }

//        Click to change role
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRole(user);
            }
        });

    }

    public void init() {
        tvId = findViewById(R.id.admin_user_detail_id);
        tvEmail = findViewById(R.id.admin_user_detail_email);
        tvName = findViewById(R.id.admin_user_detail_name);
        tvPhone = findViewById(R.id.admin_user_detail_phone);
        tvAddress = findViewById(R.id.admin_user_detail_address);
        tvRole = findViewById(R.id.admin_user_detail_role);
        cImg = findViewById(R.id.admin_user_detail_img);
        btnChange = findViewById(R.id.admin_user_change_role);

    }

    private void changeRole(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Role");
        builder.setMessage("Do you want change role this user?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dbManager.changeRoleById(user.getRoleId(), user.getUserId());
                Toast.makeText(getApplicationContext(), "Change your role successful!", Toast.LENGTH_LONG).show();
                if (admin.getUserId() == user.getUserId()) {
                    Intent intent = new Intent(AdminUserDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(AdminUserDetailActivity.this, AdminManagerActivity.class);
                    startActivity(intent);
                }

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
}