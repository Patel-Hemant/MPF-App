package com.hemantpatel.mpfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import static Constants.Params.IMAGE_URL_TRANSFER_KEY;

public class ImagePreviewActivity extends AppCompatActivity {
    ImageView prevImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }
        // image url from intent
        String url = getIntent().getStringExtra(IMAGE_URL_TRANSFER_KEY);
        prevImg = findViewById(R.id.prev_img);
        //show image by Glide
        Glide.with(ImagePreviewActivity.this).load(url).into(prevImg);


    }
}