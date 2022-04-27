package com.hemantpatel.mpfapp;

import static Constants.Params.DATABASE_ROOT_KEY;
import static Constants.Params.DATA_TRANSFER_KEY;
import static Constants.Params.USER_ID;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import Models.MissingPersonData;

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

        initView();
        setData();
        updateBtn.setOnClickListener(view -> updateData());
    }

    // update data
    private void updateData() {
        RadioButton gender = findViewById(mGroup.getCheckedRadioButtonId());
        MissingPersonData mUpdateData = new MissingPersonData(USER_ID,
                Objects.requireNonNull(name.getText()).toString(),
                Objects.requireNonNull(age.getText()).toString(),
                gender.getText().toString(),
                Objects.requireNonNull(address.getText()).toString(),
                Objects.requireNonNull(missingDate.getText()).toString(),
                Objects.requireNonNull(prize.getText()).toString(),
                Objects.requireNonNull(contact.getText()).toString() + "\n" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(),
                mOldData.getPhoto_urls(),
                Objects.requireNonNull(description.getText()).toString());

        if (mOldData.getName().equals(mUpdateData.getName())) {
            mDatabaseReference.child(USER_ID).child(mOldData.getName()).setValue(mUpdateData).addOnCompleteListener(task -> {
                Toast.makeText(EditActivity.this, "Your data is updated", Toast.LENGTH_SHORT).show();
                finish();
            });
        } else {
            mDatabaseReference.child(USER_ID).child(mOldData.getName()).removeValue((error, ref) -> mDatabaseReference.child(USER_ID).child(mUpdateData.getName()).setValue(mUpdateData).addOnCompleteListener(task -> {
                Toast.makeText(EditActivity.this, "Your data is updated", Toast.LENGTH_SHORT).show();
                finish();
            }));
        }
    }

    // set data on all views
    private void setData() {
        name.setText(mOldData.getName());
        age.setText(mOldData.getAge());
        address.setText(mOldData.getAddress());
        contact.setText(mOldData.getContacts().replace("\n" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), ""));
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
    private void initView() {
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