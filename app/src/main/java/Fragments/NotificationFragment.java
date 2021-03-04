package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Adapter.NotificationAdapter;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

import Models.NotificationData;

public class NotificationFragment extends Fragment {
    View mView;
    ArrayList<NotificationData> mList;
    RecyclerView mRecyclerView;
    NotificationAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.notification_fragment_layout, container, false);

        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mList = new ArrayList<>();
        populateList();
        mAdapter = new NotificationAdapter(getActivity(),mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        return mView;
    }

    private void populateList() {
        for (int i = 0; i < 5; i++) {
            mList.add(new NotificationData("user"+i,null,"Hello Friend,i saw your brother in Taj garden , Gandhi chowk Bhilai,you can contact me. is my contact number : 9408273838"));
        }

    }
}
