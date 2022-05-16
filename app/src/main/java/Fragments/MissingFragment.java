package Fragments;

import static android.content.Context.MODE_APPEND;
import static com.hemantpatel.mpfapp.MainActivity.dataFetched;
import static com.hemantpatel.mpfapp.MainActivity.mList;
import static Constants.Params.DATABASE_ROOT_KEY;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

import Adapter.MissingListAdapter;
import Models.MissingPersonData;

public class MissingFragment extends Fragment {
    View mView;
    RecyclerView mRecyclerView;
    public MissingListAdapter mAdapter;
    public ProgressBar mProgressBar;

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.missing_fragment_layout, container, false);

        mProgressBar = mView.findViewById(R.id.loading);
        if (dataFetched) mProgressBar.setVisibility(View.INVISIBLE);

        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mAdapter = new MissingListAdapter(getActivity(), mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        return mView;
    }
}
