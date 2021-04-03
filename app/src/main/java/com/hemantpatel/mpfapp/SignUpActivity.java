package com.hemantpatel.mpfapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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


public class SignUpActivity extends AppCompatActivity {
    TextInputEditText mEmail, mPassword, mConfermPassword;
    Button signUpBtn;
    TextView logInText;
    TextWatcher mTextWatcher;
    ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
        logInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftInputFromWindow();
                if (checkData()) {
                    SignUpUser(mEmail.getText().toString(), mPassword.getText().toString());
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mConfermPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mConfermPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
                mConfermPassword.setError(null);
            }
        };

        mEmail.addTextChangedListener(mTextWatcher);
        mPassword.addTextChangedListener(mTextWatcher);
        mConfermPassword.addTextChangedListener(mTextWatcher);
    }

    private void hideSoftInputFromWindow() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean checkData() {
        if (!mEmail.getText().toString().trim().equals("") && !mPassword.getText().toString().trim().equals("")) {
            if (mPassword.getText().toString().equals(mConfermPassword.getText().toString())) {
                return true;
            } else {
                mConfermPassword.setError("your password is no matched!");
                return false;
            }

        } else {
            if (mEmail.getText().toString().equals("")) {
                mEmail.setError("please write your email");
            }
            if (mPassword.getText().toString().equals("")) {
                mPassword.setError("please write your Password");
            }
            if (mConfermPassword.getText().toString().equals("")) {
                mConfermPassword.setError("please write your Conferm Password");
            }
            return false;
        }
    }

    private void SignUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
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

    public void initViews() {
        mAuth = FirebaseAuth.getInstance();

        mProgressBar = findViewById(R.id.progressBar2);
        mProgressBar.setVisibility(View.GONE);

        mEmail = findViewById(R.id.signup_email);
        mPassword = findViewById(R.id.signup_password);
        mConfermPassword = findViewById(R.id.signup_password_conferm);

        logInText = findViewById(R.id.login_text);
        signUpBtn = findViewById(R.id.signup_btn);
        mSwitch = findViewById(R.id.switch12);
    }
}