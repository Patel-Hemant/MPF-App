package Fragments;

import static Constants.Params.DATABASE_ROOT_KEY;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hemantpatel.mpfapp.LogInActivity;
import com.hemantpatel.mpfapp.R;
import com.hemantpatel.mpfapp.ResetPasswordActivity;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.MyMissingPersonListAdapter;
import Models.MissingPersonData;

public class AccountFragment extends Fragment {
    View mView;
    TextView userEmail, yourListTitle, emailVerifyStatus;
    Button logOutBtn, resetButton, emailVerifyBtn;
    ImageView posterImg;

    AlertDialog verifyDialog;
    Handler mHandler;
    Runnable mRunnable;

    DatabaseReference mReference;

    RecyclerView mRecyclerView;
    MyMissingPersonListAdapter mAdapter;
    ArrayList<MissingPersonData> mList;
    ValueEventListener mListener;
    DatabaseReference.CompletionListener mCompletionListener;

    private FirebaseAuth mAuth;

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.account_fragment_layout, container, false);
        initViews(getContext());

        // update Layout
        if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) makeVerified();

        logOutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            getActivity().finish();
            startActivity(new Intent(getActivity(), LogInActivity.class));
        });

        resetButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), ResetPasswordActivity.class)));

        emailVerifyBtn.setOnClickListener(view -> sendVerificationMail());

        mListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
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
        mCompletionListener = (error, ref) -> mAdapter.notifyDataSetChanged();

        return mView;
    }


    // Email Verification
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendVerificationMail() {
        // send link
        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification()
                .addOnSuccessListener(unused -> showEmailSentDialog())
                .addOnFailureListener(e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // show email sent dialog
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showEmailSentDialog() {
        // run the verification watcher - runnable handler
        mHandler.post(mRunnable);

        // make alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.email_sent_dialog_layout, null);

        builder.setView(view);
        builder.setCancelable(false)
                .setPositiveButton("Cancel", (dialogInterface, i) -> {
                    mHandler.removeCallbacks(mRunnable);
                    if (!Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                        Toast.makeText(getActivity(), "Your email is not verified please check your email and verify!", Toast.LENGTH_SHORT).show();
                    }
                });
        verifyDialog = builder.create();
        verifyDialog.show();
    }

    public void initViews(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference().child(DATABASE_ROOT_KEY).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        userEmail = mView.findViewById(R.id.user_email);
        String email = mAuth.getCurrentUser().getEmail();
        userEmail.setText(email != null ? email.substring(0, email.indexOf("@")) : "Unknown email address");
        yourListTitle = mView.findViewById(R.id.your_list_title);
        posterImg = mView.findViewById(R.id.posterImg);
        posterImg.setVisibility(View.GONE);

        emailVerifyStatus = mView.findViewById(R.id.email_verify_status_text);
        emailVerifyBtn = mView.findViewById(R.id.email_verify_btn_act);

        logOutBtn = mView.findViewById(R.id.logout_btn);
        resetButton = mView.findViewById(R.id.reset_button);

        // set RecyclerView
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mList = new ArrayList<>();
        mAdapter = new MyMissingPersonListAdapter(context, mList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        // runnable handler
        mHandler = new Handler();
        mRunnable = () -> {
            // check verified or not
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Objects.requireNonNull(user).reload();
            if (user.isEmailVerified()) {
                mHandler.removeCallbacks(mRunnable);
                verifyDialog.cancel();

                // update layout
                makeVerified();
            } else {
                // its trigger runnable after 2000 millisecond.
                mHandler.postDelayed(mRunnable, 2000);
            }
        };
    }

    @SuppressLint("SetTextI18n")
    public void makeVerified() {
        emailVerifyStatus.setText("Your email is verified!");
        emailVerifyStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done, 0);
        emailVerifyBtn.setVisibility(View.GONE);
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
