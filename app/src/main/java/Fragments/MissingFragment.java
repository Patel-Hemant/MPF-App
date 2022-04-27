package Fragments;

import static Constants.Params.DATABASE_ROOT_KEY;

import android.annotation.SuppressLint;
import android.app.Fragment;
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
    MissingListAdapter mAdapter;
    ArrayList<MissingPersonData> mList;
    ProgressBar mProgressBar;
    ValueEventListener mListener;
    DatabaseReference mDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.missing_fragment_layout, container, false);
        mProgressBar = mView.findViewById(R.id.loading);

        mList = new ArrayList<>();
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mAdapter = new MissingListAdapter(getActivity(), mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_ROOT_KEY);
        if (mListener == null) mListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    for (DataSnapshot person : snap.getChildren()) {
                        MissingPersonData data = person.getValue(MissingPersonData.class);
                        mList.add(data);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        mDatabaseReference.addValueEventListener(mListener);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDatabaseReference.removeEventListener(mListener);
    }
}
