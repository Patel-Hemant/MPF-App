package Fragments;

import static Constants.Params.DATABASE_MESSAGE_KEY;
import static Constants.Params.DATABASE_ROOT_KEY;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import Adapter.NotificationAdapter;
import Models.MessageData;
import Models.MissingPersonData;

public class NotificationFragment extends Fragment {
    View mView;
    RecyclerView mRecyclerView;
    NotificationAdapter mAdapter;

    TextView notAvailableText;
    ProgressBar mProgressBar;

    ArrayList<MissingPersonData> mPersonDataList;
    HashMap<String, MessageData> mMessageData;

    DatabaseReference mDatabase;
    ValueEventListener mListener;
    String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.notification_fragment_layout, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_ROOT_KEY);
        mPersonDataList = new ArrayList<>();
        mMessageData = new HashMap<>();

        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mAdapter = new NotificationAdapter(getActivity(), mPersonDataList, mMessageData);
        notAvailableText = mView.findViewById(R.id.notification_fragment_not_available);
        notAvailableText.setVisibility(View.GONE);
        mProgressBar = mView.findViewById(R.id.notification_load_progress);

        mListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mPersonDataList.size() != 0 && mMessageData.size() != 0) {
                    mPersonDataList.clear();
                    mMessageData.clear();
                }
                if (snapshot.child(userID).getChildrenCount() == 0) {
                    notAvailableText.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    notAvailableText.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                // get data of missing person
                for (DataSnapshot snap : snapshot.child(userID).getChildren()) {
                    MissingPersonData data = snap.getValue(MissingPersonData.class);
                    mPersonDataList.add(data);
                    int i = 1;
                    for (DataSnapshot messages : snap.child(DATABASE_MESSAGE_KEY).getChildren()) {
                        if (i == snap.child(DATABASE_MESSAGE_KEY).getChildrenCount()) {
                            MessageData tempData = messages.getValue(MessageData.class);
                            mMessageData.put(data.getName(), tempData);
                        }
                        i++;
                    }
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDatabase.addValueEventListener(mListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDatabase.removeEventListener(mListener);
    }
}
