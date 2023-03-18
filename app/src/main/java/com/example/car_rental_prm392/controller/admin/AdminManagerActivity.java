package com.example.car_rental_prm392.controller.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.controller.admin.fragment.CarManagerFragment;
import com.example.car_rental_prm392.controller.admin.fragment.CategoryManagerFragment;
import com.example.car_rental_prm392.controller.admin.fragment.UserManagerFragment;
import com.example.car_rental_prm392.controller.common.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class AdminManagerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager);
        Toolbar toolbar = findViewById(R.id.toolbar_admin_manager);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_admin_manager);
        NavigationView navigationView = findViewById(R.id.nav_view_admin_manager);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarManagerFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_car);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_car:
                setTitle("Car Manager");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarManagerFragment()).commit();
                break;
            case R.id.nav_category:
                setTitle("Category Manager");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryManagerFragment()).commit();
                break;
            case R.id.nav_user:
                setTitle("User Manager");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserManagerFragment()).commit();
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(AdminManagerActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}