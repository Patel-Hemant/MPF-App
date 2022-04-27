package Fragments;


import static com.hemantpatel.mpfapp.MissingPersonDetailActivity.mPersonData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

import Adapter.PhotoGridAdapter;

public class PhotosFragment extends Fragment {
    View mView;
    RecyclerView mRecyclerView;

    public PhotosFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.photos_tab_layout, container, false);
        mRecyclerView = mView.findViewById(R.id.grid_list);
        ArrayList<String> list = mPersonData.getPhoto_urls();

        PhotoGridAdapter mAdapter = new PhotoGridAdapter(getContext(), list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        return mView;
    }
}
