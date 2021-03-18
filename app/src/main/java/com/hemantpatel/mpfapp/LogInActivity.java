package com.hemantpatel.mpfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    TextInputEditText mEmail, mPassword;

    Button logInBtn;
    TextView signUpText;
    ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    Switch mSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();

        mSwitch = findViewById(R.id.switch1);

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });



        signUpText = findViewById(R.id.signup_text);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
            }
        });

        logInBtn = findViewById(R.id.login_btn);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    LogInUser(mEmail.getText().toString(), mPassword.getText().toString());
                    mProgressBar.setVisibility(View.VISIBLE);
                    hideSoftInputFromWindow();
                } else {
                    Toast.makeText(LogInActivity.this, "please write your email and password", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private boolean checkData() {
        if (!mEmail.getText().toString().trim().equals("") && !mPassword.getText().toString().trim().equals(""))
            return true;
        else return false;
    }

    private void hideSoftInputFromWindow() {
// Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void LogInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    Log.d("hemu", user.getEmail() + "\n ID: " + user.getUid());
                    Toast.makeText(LogInActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                } else {
                    Log.d("hemu", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(LogInActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}