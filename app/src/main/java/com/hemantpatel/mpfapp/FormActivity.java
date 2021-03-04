package com.hemantpatel.mpfapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import Adapter.PhotoListAdapter;
import Models.ImgDetailFile;

public class FormActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 456;
    private RecyclerView mRecyclerView;
    private ArrayList<String> urlList;
    private PhotoListAdapter mAdapter;
    private Uri mImageUri;
    private Button mFileChooserBtn, mUploadBtn;
    private ArrayList<ImgDetailFile> imgList;
    ImgDetailFile file;

    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");

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

                    }
                });

        mRecyclerView = findViewById(R.id.photo_list);
        imgList = new ArrayList<>();

        urlList = new ArrayList<>();

        mAdapter = new PhotoListAdapter(FormActivity.this,imgList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(FormActivity.this,RecyclerView.HORIZONTAL,false));


    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void addImgFile(String name,Uri uri) {
        ImgDetailFile file = new ImgDetailFile(name,uri);
        imgList.add(file);
    }






  /*
   private void uploadFile() {
        StorageReference fileRef = mStorageReference.child(mEditTextFileName.getText().toString() + "." + getFileExtension(mImageUri));
        mUploadTask = fileRef.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(FormActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                        mProgressBar.setProgress(0);
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                ImgDetailFile imgDetailFile = new ImgDetailFile(mEditTextFileName.getText().toString(), uri.toString());
                                //  String uploadId = mDatabaseReference.push().getKey();
                                mDatabaseReference.child(imgDetailFile.getName()).setValue(imgDetailFile);
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
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        mProgressBar.setProgress((int) progress);
                    }
                });
    }

*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && resultCode == RESULT_OK && data.getData() != null) {

            mImageUri = data.getData();
            mUploadBtn.setEnabled(true);

            addImgFile("name",mImageUri);

            mRecyclerView.post(new Runnable()
            {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

    }

}