package Fragments;

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

import Adapter.MissingListAdapter;
import Models.MissingPersonData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;
import java.util.Map;

public class MissingFragment extends Fragment {
    View mView;
    RecyclerView mRecyclerView;
    MissingListAdapter mAdapter;
    ArrayList<MissingPersonData> mList;

    ProgressBar mProgressBar;

    private DatabaseReference mDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.missing_fragment_layout,container,false);
        mProgressBar = mView.findViewById(R.id.loading);

        mList = new ArrayList<>();
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mAdapter = new MissingListAdapter(getActivity(),mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Missing Person Data");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for (DataSnapshot snap : snapshot.getChildren()){

                    for (DataSnapshot person : snap.getChildren()){
                        MissingPersonData data = person.getValue(MissingPersonData.class);
                       // MissingPersonData newData = new MissingPersonData(snap.getKey(),data.get("name").toString(),data.get("age").toString(),data.get("gender").toString(),data.get("address").toString(),data.get("missing_date").toString(),data.get("prize").toString(),data.get("contacts").toString(),(ArrayList<String>) data.get("photo_urls"),data.get("description").toString());
                        mList.add(data);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return mView;
    }

}
