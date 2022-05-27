package com.hemantpatel.mpfapp.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.hemantpatel.mpfapp.R;

public class InternetChecker {
    Context mContext;

    public InternetChecker(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        boolean status = netInfo != null && netInfo.isConnectedOrConnecting();
        if (!status) showCheckInternetDialog();
        return status;
    }

    public void showCheckInternetDialog() {
        // make alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);

        View view = LayoutInflater.from(mContext).inflate(R.layout.check_internet_dialog_layout, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        Button check_btn = view.findViewById(R.id.internet_check_btn);
        check_btn.setOnClickListener(v -> mContext.startActivity(new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS)));

        Button close_btn = view.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(v -> dialog.cancel());

        // show
        dialog.show();
    }

}
