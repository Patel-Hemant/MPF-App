package com.hemantpatel.mpfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity {
    TextInputEditText mEmail, mPassword;
    Button signUpBtn;
    TextView logInText;

    ProgressBar mProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        mProgressBar = findViewById(R.id.progressBar2);
        mProgressBar.setVisibility(View.GONE);

        mEmail = findViewById(R.id.signup_email);
        mPassword = findViewById(R.id.signup_password);

        logInText = findViewById(R.id.login_text);
        logInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            }
        });

        signUpBtn = findViewById(R.id.signup_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpUser(mEmail.getText().toString(), mPassword.getText().toString());
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void SignUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    Log.d("hemu", user.getEmail());
                    Toast.makeText(SignUpActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));

                } else {
                    Log.d("hemu", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
    }


}