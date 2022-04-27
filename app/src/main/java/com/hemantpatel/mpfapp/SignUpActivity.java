package com.hemantpatel.mpfapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {
    TextInputEditText mEmail, mPassword, mConfirmPassword;
    Button signUpBtn;
    TextView logInText;
    TextWatcher mTextWatcher;
    ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
        logInText.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
        });

        signUpBtn.setOnClickListener(view -> {
            hideSoftInputFromWindow();
            String email = Objects.requireNonNull(mEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(mPassword.getText()).toString().trim();
            String re_password = Objects.requireNonNull(mConfirmPassword.getText()).toString().trim();
            if (checkData(email, password, re_password)) {
                SignUpUser(email, password);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mEmail.setError(null);
                mPassword.setError(null);
                mConfirmPassword.setError(null);
            }
        };

        mEmail.addTextChangedListener(mTextWatcher);
        mPassword.addTextChangedListener(mTextWatcher);
        mConfirmPassword.addTextChangedListener(mTextWatcher);
    }

    private void hideSoftInputFromWindow() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean checkData(String email, String password, String re_password) {

        if (!email.equals("") && !password.equals("")) {
            if (password.equals(re_password)) {
                return true;
            } else {
                mConfirmPassword.setError("your password is no matched!");
                return false;
            }

        } else {
            if (email.equals("")) {
                mEmail.setError("please write your email");
            }
            if (password.equals("")) {
                mPassword.setError("please write your Password");
            }
            if (re_password.equals("")) {
                mConfirmPassword.setError("please write your Confirm Password");
            }
            return false;
        }
    }

    private void SignUpUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            mProgressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
    }

    public void initViews() {
        mAuth = FirebaseAuth.getInstance();

        mProgressBar = findViewById(R.id.progressBar2);
        mProgressBar.setVisibility(View.GONE);

        mEmail = findViewById(R.id.signup_email);
        mPassword = findViewById(R.id.signup_password);
        mConfirmPassword = findViewById(R.id.signup_password_conferm);

        logInText = findViewById(R.id.login_text);
        signUpBtn = findViewById(R.id.signup_btn);
        mSwitch = findViewById(R.id.switch12);
    }

}