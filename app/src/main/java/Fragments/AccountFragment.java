package Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hemantpatel.mpfapp.LogInActivity;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

import Adapter.MyMissingPersonListAdapter;
import Models.MissingPersonData;

import static Constants.Params.DATABASE_ROOT_KEY;

public class AccountFragment extends Fragment {
    View mView;
    TextView userEmail, yourListTitle;
    Button logOutBtn;
    ImageView posterImg;

    DatabaseReference mReference;

    RecyclerView mRecyclerView;
    MyMissingPersonListAdapter mAdapter;
    ArrayList<MissingPersonData> mList;
    ValueEventListener mListener;
    DatabaseReference.CompletionListener mCompletionListener;

    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.account_fragment_layout, container, false);
        initViews(getContext());

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LogInActivity.class));
            }
        });

        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mList.size() != 0) {
                    mList.clear();
                }
                for (DataSnapshot data : snapshot.getChildren()) {
                    MissingPersonData personData = data.getValue(MissingPersonData.class);
                    mList.add(personData);
                }
                mAdapter.notifyDataSetChanged();

                // set visibility of list and title according data have or not
                if (mList.size() == 0) {
                    mRecyclerView.setVisibility(View.GONE);
                    yourListTitle.setVisibility(View.GONE);
                    posterImg.setVisibility(View.VISIBLE);
                } else {
                    posterImg.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    yourListTitle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        };
        mReference.addValueEventListener(mListener);

        mCompletionListener = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                mAdapter.notifyDataSetChanged();
            }
        };

        return mView;
    }

    public void initViews(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference().child(DATABASE_ROOT_KEY).child(mAuth.getCurrentUser().getUid());

        userEmail = mView.findViewById(R.id.user_email);
        userEmail.setText(mAuth.getCurrentUser().getEmail());
        yourListTitle = mView.findViewById(R.id.your_list_title);
        posterImg = mView.findViewById(R.id.posterImg);
        posterImg.setVisibility(View.GONE);
        logOutBtn = mView.findViewById(R.id.logout_btn);

        // set RecyclerView
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mList = new ArrayList<>();
        mAdapter = new MyMissingPersonListAdapter(context, mList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mReference.removeEventListener(mListener);
    }


    public void deleteData(int position) {
        mReference.child(mList.get(position).getName()).removeValue();
        mList.remove(position);
    }
}
