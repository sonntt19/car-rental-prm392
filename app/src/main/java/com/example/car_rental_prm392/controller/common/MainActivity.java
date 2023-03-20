package com.example.car_rental_prm392.controller.common;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.User;

public class MainActivity extends AppCompatActivity {
    private static final User ADMIN = new User("admin@gmail.com","123456789","Admin","123456789",1);

    private static int SPLASH_SCREEN = 4000;
    Animation topAnimation, botAnimation;
    ImageView image;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        DBManager dbManager = new DBManager(this);

//        Set admin for first run
        if(dbManager.checkUser("admin@gmail.com","123456789")==null){
            dbManager.addUser(ADMIN);
        }

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        botAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        image = findViewById(R.id.imageViewLogo);
        logo = findViewById(R.id.tvLogo);

        image.setAnimation(topAnimation);
        logo.setAnimation(botAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(image, "logo");
                pairs[1] = new Pair<View, String>(logo, "logo_text");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        },SPLASH_SCREEN);
    }
}