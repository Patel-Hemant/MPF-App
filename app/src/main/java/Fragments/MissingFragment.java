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

import Adapter.MissingListAdapter;
import Models.MissingPersonData;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

public class MissingFragment extends Fragment {
    View mView;
    RecyclerView mRecyclerView;
    MissingListAdapter mAdapter;
    ArrayList<MissingPersonData> mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.missing_fragment_layout,container,false);
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mList = new ArrayList<>();
        populateList();
        mAdapter = new MissingListAdapter(getActivity(),mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);



        return mView;
    }

    private void populateList() {
        for (int i = 0; i < 50; i++) {
           mList.add(new MissingPersonData("Person name"+i,""+i*5,"male","red yellow",i+"-02-2021",i*10+"$",null,null));
        }
    }
}
