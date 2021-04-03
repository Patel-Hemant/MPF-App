package com.hemantpatel.mpfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragments.AddFragment;
import Fragments.MissingFragment;
import Fragments.NotificationFragment;
import Fragments.AccountFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new MissingFragment());


        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.missing:
                        loadFragment(new MissingFragment());
                        break;
                    case R.id.add:
                        loadFragment(new AddFragment());
                        break;
                    case R.id.notification:
                        loadFragment(new NotificationFragment());
                        break;
                    case R.id.account:
                        loadFragment(new AccountFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}