package com.hemantpatel.mpfapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import Adapter.PhotoListAdapter;
import Models.ImgDetailFile;
import Models.MissingPersonData;

import static Constants.Params.DATABASE_ROOT_KEY;
import static Constants.Params.STORAGE_ROOT_KEY;

public class FormActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 456;
    private RecyclerView mRecyclerView;
    private PhotoListAdapter mAdapter;
    private Button mFileChooserBtn, mUploadBtn, cancelBtn;
    private ProgressBar mDialogProgress;

    private ArrayList<ImgDetailFile> imgList;
    private TextInputEditText name, age, address, contact, missingDate, prize, description;
    private RadioGroup mGroup;
    private ArrayList<String> photo_urls;
    private Uri mImageUri;

    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;

    private final String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private StorageTask mUploadTask;
    private int taskIndex = 0;

    private Dialog mAlertDialog;
    private TextView dialogText;
    private LinearProgressIndicator dialogProgress;
    private ImageView dialogImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // init all views with these Ids
        initViews();


        mDatabaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_ROOT_KEY);
        mStorageReference = FirebaseStorage.getInstance().getReference(STORAGE_ROOT_KEY);

        mFileChooserBtn = findViewById(R.id.select_btn);
        mFileChooserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        mUploadBtn = findViewById(R.id.upload_btn);
        mUploadBtn.setEnabled(false);
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    uploadFile();
                    mAlertDialog.show();
                }
            }
        });

        mRecyclerView = findViewById(R.id.photo_list);
        imgList = new ArrayList<>();

        mAdapter = new PhotoListAdapter(FormActivity.this, imgList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(FormActivity.this, RecyclerView.HORIZONTAL, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        mAlertDialog = new Dialog(FormActivity.this);
        mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlertDialog.setContentView(R.layout.upload_dialog_layout);
        mAlertDialog.setTitle("Title");
        mAlertDialog.setCancelable(false);

        dialogText = mAlertDialog.findViewById(R.id.dialog_text);
        dialogProgress = mAlertDialog.findViewById(R.id.dialog_progress);
        dialogImg = mAlertDialog.findViewById(R.id.dialog_img);
        mDialogProgress = mAlertDialog.findViewById(R.id.progressDialog);
        mDialogProgress.setVisibility(View.GONE);
        cancelBtn = mAlertDialog.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask.isInProgress()) {
                    mUploadTask.cancel();
                    dialogText.setText("Canceling....");
                    mDialogProgress.setVisibility(View.VISIBLE);
                }
            }
        });

        name = findViewById(R.id.msg_text_box);
        age = findViewById(R.id.missing_person_age);
        address = findViewById(R.id.missing_person_address);
        contact = findViewById(R.id.missing_person_contact_number);
        missingDate = findViewById(R.id.missing_person_missing_date);
        prize = findViewById(R.id.prize);
        description = findViewById(R.id.description);
        mGroup = findViewById(R.id.radioGroup);
        photo_urls = new ArrayList<>();

// scrolling not work in EditText inside Scrollview //EditText not scrollable inside ScrollView
// disable parent interaction when editText is touched or Scroll
        description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });

    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void addImgFile(String name, Uri uri) {
        ImgDetailFile file = new ImgDetailFile(name, uri);
        imgList.add(file);
    }

    private void uploadFile() {
        if (imgList.size() == taskIndex) {
            publishData();
            //mDialog.dismiss();
            mAlertDialog.dismiss();
            finish();
            return;
        }

        dialogText.setText((taskIndex + 1) + "/" + imgList.size() + " image is uploading ....");
        Glide.with(FormActivity.this).load(imgList.get(taskIndex).getImageUri()).into(dialogImg);

        StorageReference fileRef = mStorageReference.child(USER_ID).child(name.getText().toString()).child(name.getText().toString() + taskIndex + "." + getFileExtension(imgList.get(taskIndex).getImageUri()));
        mUploadTask = fileRef.putFile(imgList.get(taskIndex).getImageUri())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                photo_urls.add(uri.toString());
                                taskIndex++;
                                uploadFile();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        int progress = (int) (200.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        dialogProgress.setProgress((int) progress);
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        mDialogProgress.setVisibility(View.GONE);
                        mAlertDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && resultCode == RESULT_OK && data.getData() != null) {

            mImageUri = data.getData();
            mUploadBtn.setEnabled(true);
            addImgFile("name", mImageUri);

            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(imgList.size() - 1);
                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(getContentResolver().getType(uri));
    }

    private boolean checkData() {
        int x = 0;

        if (name.getText().toString().trim().equals("")) {
            name.setError("Please write your name");
            x++;
        }
        if (age.getText().toString().trim().equals("")) {
            age.setError("Please write your age");
            x++;
        }
        if (address.getText().toString().trim().equals("")) {
            address.setError("Please write your address");
            x++;
        }
        if (contact.getText().toString().trim().equals("")) {
            contact.setError("Please write your contact");
            x++;
        }
        if (missingDate.getText().toString().trim().equals("")) {
            missingDate.setError("Please write your missing date");
            x++;
        }
        if (description.getText().toString().trim().equals("")) {
            description.setError("Please write your description");
            x++;
        }

        if (mGroup.getCheckedRadioButtonId() != R.id.rdo_male && mGroup.getCheckedRadioButtonId() != R.id.rdo_female && mGroup.getCheckedRadioButtonId() != R.id.rdo_other) {
            Toast.makeText(FormActivity.this, "Please Select Gender!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (x > 0) {
            return false;
        } else {
            return true;
        }
    }

    private void publishData() {
        String prizeValue;
        if (prize.getText().toString().trim().equals("")) {
            prizeValue = "00.00";
        } else {
            prizeValue = prize.getText().toString();
        }
        RadioButton gender = findViewById(mGroup.getCheckedRadioButtonId());
        MissingPersonData data = new MissingPersonData(USER_ID,
                name.getText().toString(),
                age.getText().toString(),
                gender.getText().toString(),
                address.getText().toString(),
                missingDate.getText().toString(),
                prize.getText().toString(),
                contact.getText().toString() + "\n" + FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                photo_urls,
                description.getText().toString());

        mDatabaseReference.child(USER_ID).child(data.getName()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(FormActivity.this, "Uploaded Successful!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}