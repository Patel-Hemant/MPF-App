package com.hemantpatel.mpfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        /* Hiding Title bar of this activity screen */
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        /* Making this activity, full screen */
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mHandler = new Handler();
        mHandler.postDelayed(() -> {
            finish();
            startActivity(new Intent(SplashScreenActivity.this, SignUpActivity.class));
        }, 4000);


    }
}