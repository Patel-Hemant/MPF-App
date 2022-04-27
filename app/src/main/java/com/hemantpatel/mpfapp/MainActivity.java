package com.hemantpatel.mpfapp;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragments.AccountFragment;
import Fragments.AddFragment;
import Fragments.MissingFragment;
import Fragments.NotificationFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MissingFragment mf = new MissingFragment();
        AddFragment af = new AddFragment();
        NotificationFragment nf = new NotificationFragment();
        AccountFragment acf = new AccountFragment();

        // initial load the missing list fragment
        loadFragment(mf);

        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.missing:
                    loadFragment(mf);
                    break;
                case R.id.add:
                    loadFragment(af);
                    break;
                case R.id.notification:
                    loadFragment(nf);
                    break;
                case R.id.account:
                    loadFragment(acf);
                    break;
            }
            return true;
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