package Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.hemantpatel.mpfapp.LogInActivity;
import com.hemantpatel.mpfapp.R;

import org.w3c.dom.Text;

public class SettingFragment extends Fragment {
    View mView;
    TextView userEmail;
    Button logOutBtn;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.setting_fragment_layout, container, false);

        mAuth = FirebaseAuth.getInstance();

        userEmail = mView.findViewById(R.id.user_email);
        userEmail.setText(mAuth.getCurrentUser().getEmail());

        logOutBtn = mView.findViewById(R.id.logout_btn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LogInActivity.class));
            }
        });

        return mView;
    }
}
