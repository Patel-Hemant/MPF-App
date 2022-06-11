package com.hemantpatel.mpfapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.AddressListAdapter;

public class AddressSelectActivity extends AppCompatActivity {
    EditText addressTV;
    List<Address> addressList;
    RecyclerView recyclerView;
    AddressListAdapter mAdapter;

    // Used handler runnable for optimize the address call :)
    Handler handler;
    Runnable runnable;
    int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }

        addressList = new ArrayList<>();
        addressTV = findViewById(R.id.address_text_box);
        addressTV.requestFocus();

        mAdapter = new AddressListAdapter(addressList, this);
        recyclerView = findViewById(R.id.address_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddressSelectActivity.this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        handler = new Handler();
        runnable = new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                // if time of prev called is less then 1 sec then increase time and when you over more then 1 sec then call for address.
                if (time >= 1) {
                    time = 0;
                    // update list
                    addressList.clear();
                    addressList.addAll(getLocationFromAddress(AddressSelectActivity.this, addressTV.getText().toString()));
                    mAdapter.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();
                } else {
                    time++;
                    handler.postDelayed(this, 1000);
                }
            }
        };

        addressTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // reset the previous call and make a new call
                time = 0;
                handler.removeCallbacks(runnable);
                handler.post(runnable);
            }
        });
    }

    public List<Address> getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address = null;

        // May throw an IOException
        try {
            address = coder.getFromLocationName(strAddress, 5);
        } catch (IOException e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if (address == null || address.isEmpty()) {
            return new ArrayList<>();
        }

        return address;
    }
}