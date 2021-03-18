package Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hemantpatel.mpfapp.R;

import java.text.MessageFormat;

import static com.hemantpatel.mpfapp.MissingPersonDetailActivity.mPersonData;

public class DetailFragment extends Fragment {
    TextView prize, contact, address, description;
    View mView;

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

        prize.setText(String.format(getString(R.string.prize) + "%s", mPersonData.getPrize()));
        contact.setText(String.format(getString(R.string.contacts) + "\n%s", mPersonData.getContacts()));
        address.setText(String.format(getString(R.string.address) + "\n%s", mPersonData.getAddress()));
        description.setText(String.format(getString(R.string.description) + "\n%s", mPersonData.getDescription()));

        return mView;
    }
}
