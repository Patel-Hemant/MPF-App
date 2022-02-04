package com.hemantpatel.mpfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText mEmail;
    Button resetButton;
    FirebaseAuth auth;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mEmail = findViewById(R.id.reset_password_email);
        resetButton = findViewById(R.id.reset_password_btn);
        mProgressBar = findViewById(R.id.progressBar3);
        mProgressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = mEmail.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please provide valid email");
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(ResetPasswordActivity.this, "Reset link is sent to your email", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(ResetPasswordActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}