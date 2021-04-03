package Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hemantpatel.mpfapp.FormActivity;
import com.hemantpatel.mpfapp.R;

public class AddFragment extends Fragment {
    View mView;
    Button form_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.add_fragment_layout, container, false);
        form_btn = mView.findViewById(R.id.form_btn);
        form_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FormActivity.class));
            }
        });
        return mView;
    }
}
