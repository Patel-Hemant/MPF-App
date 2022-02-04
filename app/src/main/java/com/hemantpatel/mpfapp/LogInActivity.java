package com.hemantpatel.mpfapp;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    private TextInputEditText mEmail, mPassword;

    private Button logInBtn;
    private TextView signUpText, resetText;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private Switch mSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initViews();

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

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
            }
        });

        resetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                startActivity(new Intent(LogInActivity.this, ResetPasswordActivity.class));
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    LogInUser(mEmail.getText().toString().trim(), mPassword.getText().toString().trim());
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
                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                    finish();
                } else {
                    Log.d("hemu", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(LogInActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();
        mSwitch = findViewById(R.id.switch12);

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);

        signUpText = findViewById(R.id.signup_text);
        resetText = findViewById(R.id.reset_text_btn);
        logInBtn = findViewById(R.id.login_btn);
    }

}