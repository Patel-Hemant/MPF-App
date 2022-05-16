package com.hemantpatel.mpfapp;

import static Constants.Params.ADDRESS_KEY;
import static Constants.Params.LATITUDE_KEY;
import static Constants.Params.LOCATION_PERMISSION_CODE;
import static Constants.Params.LONGITUDE_KEY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;

public class StoreMyLocationActivity extends AppCompatActivity {
    double lat0 = 0, lon0 = 0;
    String address = "NA";
    LocationManager locationManager;
    LocationListener locationListener;

    TextView addressTextView, addressTitle;
    Button findMyAddressBtn;
    ProgressBar progressBar;

    // Storing data into SharedPreferences
    SharedPreferences sharedPreferences;

    boolean addressFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_my_location);
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        addressTextView = findViewById(R.id.address_text_view);
        addressTitle = findViewById(R.id.addressTitle);
        progressBar = findViewById(R.id.progressBar2);
        findMyAddressBtn = findViewById(R.id.use_my_location_btn);

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_APPEND);
        if (!sharedPreferences.getString(ADDRESS_KEY, "NA").equals("NA")) {
            startActivity(new Intent(StoreMyLocationActivity.this, MainActivity.class));
            finish();
        }

        findMyAddressBtn.setOnClickListener(v -> {
            if (addressFetched) {
                // Save button
                saveUserLocation(lat0, lon0, address);
                // redirect to main activity after 3 sec
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StoreMyLocationActivity.this, lat0 + "\n " + lon0 + "\n " + address, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StoreMyLocationActivity.this, MainActivity.class));
                        finish();
                    }
                }, 2000);

            } else {
                // Fetch button
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    getPermission();
                } else getLocation();
            }
        });

        locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(Location location) {
                double lat1 = location.getLatitude();
                double lon1 = location.getLongitude();

                if (lat0 == 0 && lon0 == 0) {
                    lat0 = lat1;
                    lon0 = lon1;

                    try {
                        Geocoder geocoder = new Geocoder(StoreMyLocationActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(lat0, lon0, 1);
                        address = addresses.get(0).getAddressLine(0);

                        addressTextView.setText(address);
                        addressTitle.setVisibility(View.VISIBLE);
                        addressTextView.setVisibility(View.VISIBLE);

                        offLocationTracking();

                        addressFetched = true;
                        findMyAddressBtn.setText("Save My Location");
                        findMyAddressBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_save, 0);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(StoreMyLocationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } /* else {
                    double distance = getDistanceFromLatLonInKm(lat0, lon0, lat1, lon1) * 1000;
                    StringBuilder sb = new StringBuilder("Distance from the start :\n" + distance + "\n");

                    if (distance <= 5) sb.append("You have reached");
                    else if (distance <= 20) sb.append("Goal is near to you");
                    else if (distance <= 100) sb.append("You are near to the Goal");
                    else if (distance <= 1000) sb.append("Goal is in your area");
                    else if (distance <= 10000) sb.append("Goal is in your city");
                    else if (distance <= 100000) sb.append("Goal is in your State");
                    else sb.append("Keep find");

                    textView_distance.setText(sb.toString());
                }*/
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Toast.makeText(StoreMyLocationActivity.this, "Status changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String s) {
                Toast.makeText(StoreMyLocationActivity.this, "onProviderEnabled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(StoreMyLocationActivity.this, "onProviderDisabled", Toast.LENGTH_SHORT).show();
            }
        };

    }

    //Runtime permissions
    public void getPermission() {
        ActivityCompat.requestPermissions(StoreMyLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
    }

    private void offLocationTracking() {
        progressBar.setVisibility(View.INVISIBLE);
        locationManager.removeUpdates(locationListener);
    }

    private void getLocation() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            addressTitle.setVisibility(View.INVISIBLE);
            addressTextView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void saveUserLocation(double latitude, double longitude, String address) {
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(LATITUDE_KEY, latitude + "");
        editor.putString(LONGITUDE_KEY, longitude + "");
        editor.putString(ADDRESS_KEY, address);

        editor.apply();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
//            else getPermission();
        }
    }
}