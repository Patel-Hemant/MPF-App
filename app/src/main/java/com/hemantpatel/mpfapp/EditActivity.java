package com.hemantpatel.mpfapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import Models.MissingPersonData;

import static Constants.Params.DATABASE_ROOT_KEY;
import static Constants.Params.DATA_TRANSFER_KEY;
import static Constants.Params.USER_ID;

public class EditActivity extends AppCompatActivity {
    private TextInputEditText name, age, address, contact, missingDate, prize, description;
    private RadioGroup mGroup;
    private Button updateBtn;
    private MissingPersonData mOldData;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_ROOT_KEY);
        mOldData = (MissingPersonData) getIntent().getSerializableExtra(DATA_TRANSFER_KEY);

        initview();
        setData();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }

    // update data
    private void updateData() {
        RadioButton gender = findViewById(mGroup.getCheckedRadioButtonId());
        MissingPersonData mUpdateData = new MissingPersonData(USER_ID,
                name.getText().toString(),
                age.getText().toString(),
                gender.getText().toString(),
                address.getText().toString(),
                missingDate.getText().toString(),
                prize.getText().toString(),
                contact.getText().toString() + "\n" + FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                mOldData.getPhoto_urls(),
                description.getText().toString());

        if (mOldData.getName().equals(mUpdateData.getName())) {
            mDatabaseReference.child(USER_ID).child(mOldData.getName()).setValue(mUpdateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(EditActivity.this, "your data is updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            mDatabaseReference.child(USER_ID).child(mOldData.getName()).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    mDatabaseReference.child(USER_ID).child(mUpdateData.getName()).setValue(mUpdateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditActivity.this, "your data is updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });
        }
    }

    // set data on all views
    private void setData() {
        name.setText(mOldData.getName());
        age.setText(mOldData.getAge());
        address.setText(mOldData.getAddress());
        contact.setText(mOldData.getContacts().replace("\n" + FirebaseAuth.getInstance().getCurrentUser().getEmail(), ""));
        missingDate.setText(mOldData.getMissing_date());
        prize.setText(mOldData.getPrize());
        description.setText(mOldData.getDescription());
        if (mOldData.getGender().equals("Male")) {
            mGroup.check(R.id.male_rdo_btn);
        } else if (mOldData.getGender().equals("Female")) {
            mGroup.check(R.id.female_rdo_btn);
        } else {
            mGroup.check(R.id.other_rdo_btn);
        }
    }

    // init view with ids
    private void initview() {
        name = findViewById(R.id.msg_text_box);
        age = findViewById(R.id.missing_person_age);
        address = findViewById(R.id.missing_person_address);
        contact = findViewById(R.id.missing_person_contact_number);
        missingDate = findViewById(R.id.missing_person_missing_date);
        prize = findViewById(R.id.prize);
        description = findViewById(R.id.description);
        mGroup = findViewById(R.id.radioGroup);
        updateBtn = findViewById(R.id.update_btn);
    }
}