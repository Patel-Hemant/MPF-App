package com.hemantpatel.mpfapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import Adapter.DetailTabAdapter;
import Models.MissingPersonData;

public class MissingPersonDetailActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    ImageView mImageView;
    TextView name, age, gender, missing_date;
    public static MissingPersonData mPersonData;

    Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_person_detail);

        mPersonData = (MissingPersonData) getIntent().getSerializableExtra("data");

        mImageView = findViewById(R.id.missing_image);
        Glide.with(MissingPersonDetailActivity.this).load(mPersonData.getPhoto_urls().get(0)).into(mImageView);

        name = findViewById(R.id.missing_name);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        missing_date = findViewById(R.id.missing_date);


        name.setText(mPersonData.getName());
        age.setText(String.format("Age: %s", mPersonData.getAge()));
        gender.setText(String.format("Gender: %s", mPersonData.getGender()));
        missing_date.setText(String.format("Missing Date: %s", mPersonData.getMissing_date()));


        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Detail"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MissingPersonDetailActivity.this,MessageSendActivity.class);
                        intent.putExtra("data",mPersonData);
                        startActivity(intent);
                    }
                });









        final DetailTabAdapter adapter = new DetailTabAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }
}