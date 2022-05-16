package Fragments;


import static com.hemantpatel.mpfapp.MissingPersonDetailActivity.mPersonData;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hemantpatel.mpfapp.R;

import Models.LocationData;

public class DetailFragment extends Fragment {
    TextView prize, contact, address, description;
    View mView;
    FloatingActionButton fab;

    public DetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.detail_tab_layout, container, false);

        prize = mView.findViewById(R.id.detail_tab_prize);
        contact = mView.findViewById(R.id.detail_tab_contact);
        address = mView.findViewById(R.id.detail_tab_address);
        description = mView.findViewById(R.id.detail_tab_description);
        fab = mView.findViewById(R.id.open_in_map_fab);

        fab.setOnClickListener(view -> {
            openInMap();
        });

        prize.setText(String.format(getString(R.string.prize) + "%s", mPersonData.getPrize()));
        contact.setText(String.format(getString(R.string.contacts) + "\n%s", mPersonData.getContacts()));
        address.setText(String.format(getString(R.string.address) + "\n%s", mPersonData.getAddress()));
        description.setText(String.format(getString(R.string.description) + "\n%s", mPersonData.getDescription()));

        return mView;
    }

    private void openInMap() {
        LocationData location = mPersonData.getLocationData();

        // e.g. https://www.google.com/maps/search/?api=1&query=28.6139,77.2090
        StringBuilder url = new StringBuilder("https://www.google.com/maps/search/?api=1&query=");
        url.append(location.getLatitude()).append(',').append(location.getLongitude());

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
        startActivity(browserIntent);
    }
}
