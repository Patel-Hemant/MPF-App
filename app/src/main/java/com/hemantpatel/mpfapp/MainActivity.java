package com.hemantpatel.mpfapp;


import static Constants.DistanceCalculator.getDistanceFromLatLonInKm;
import static Constants.Params.DATABASE_ROOT_KEY;
import static Constants.Params.LATITUDE_KEY;
import static Constants.Params.LONGITUDE_KEY;
import static Constants.Params.SHARED_PREFERENCES_KEY;
import static Constants.SortList.sortByDistance;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Fragments.AccountFragment;
import Fragments.AddFragment;
import Fragments.MissingFragment;
import Fragments.NotificationFragment;
import Models.MissingPersonData;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView mBottomNavigationView;
    ValueEventListener mListener;
    DatabaseReference mDatabaseReference;

    SharedPreferences sharedPreferences;

    double MyLatitude;
    double MyLongitude;

    public static ArrayList<MissingPersonData> mList;
    public static boolean dataFetched = false;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = new ArrayList<>();

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_APPEND);
        MyLatitude = Double.parseDouble(sharedPreferences.getString(LATITUDE_KEY, "0.0f"));
        MyLongitude = Double.parseDouble(sharedPreferences.getString(LONGITUDE_KEY, "0.0f"));

        MissingFragment mf = new MissingFragment();
        AddFragment af = new AddFragment();
        NotificationFragment nf = new NotificationFragment();
        AccountFragment acf = new AccountFragment();

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

        // initial load the missing list fragment
        loadFragment(mf);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_ROOT_KEY);
        if (mListener == null) mListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    for (DataSnapshot person : snap.getChildren()) {
                        MissingPersonData data = person.getValue(MissingPersonData.class);
                        if (data == null) continue;
                        double dist = getDistanceFromLatLonInKm(MyLatitude, MyLongitude, data.getLocationData().getLatitude(), data.getLocationData().getLongitude());
                        data.getLocationData().setDistance(dist);
                        mList.add(data);
                    }
                }
                sortByDistance(mList);
                mf.mAdapter.notifyDataSetChanged();
                mf.mProgressBar.setVisibility(View.GONE);
                dataFetched = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDatabaseReference.addValueEventListener(mListener);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseReference.removeEventListener(mListener);
    }
}