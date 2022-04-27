package com.hemantpatel.mpfapp;
import static Constants.Params.DATA_TRANSFER_KEY;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

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

        initViews();

        sendBtn.setOnClickListener(view -> {
            if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).isEmailVerified()) {
                Intent intent = new Intent(MissingPersonDetailActivity.this, MessageSendActivity.class);
                intent.putExtra(DATA_TRANSFER_KEY, mPersonData);
                startActivity(intent);
            } else {
                Toast.makeText(MissingPersonDetailActivity.this, "Please Verify Your Email Address First !", Toast.LENGTH_SHORT).show();
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

    public void initViews() {
        mPersonData = (MissingPersonData) getIntent().getSerializableExtra(DATA_TRANSFER_KEY);

        mImageView = findViewById(R.id.missing_image);
        Glide.with(MissingPersonDetailActivity.this).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_person)).load(mPersonData.getPhoto_urls().get(0)).into(mImageView);

        // init views with these Ids
        name = findViewById(R.id.missing_name);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        missing_date = findViewById(R.id.missing_date);

        // setData on Views
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
    }
}