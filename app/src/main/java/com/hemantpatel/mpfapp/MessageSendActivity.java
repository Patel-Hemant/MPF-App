package com.hemantpatel.mpfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

import Adapter.MessageListAdapter;
import Models.MessageData;
import Models.MissingPersonData;


public class MessageSendActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MessageListAdapter mAdapter;

    private MissingPersonData mPersonData;
    private ArrayList<MessageData> mMsgList;

    private DatabaseReference mMsgDatabase;
    private TextInputEditText textBox;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_send);
        mPersonData = (MissingPersonData) getIntent().getSerializableExtra("data");



        mMsgDatabase = FirebaseDatabase.getInstance().getReference().child("Missing Person Data").child(mPersonData.getUserId()).child(mPersonData.getName()).child("messages");
        mMsgList = new ArrayList<>();
        readData();


        textBox = findViewById(R.id.msg_text_box);

        mAdapter = new MessageListAdapter(MessageSendActivity.this, mMsgList);

        mRecyclerView = findViewById(R.id.msg_list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MessageSendActivity.this));

        textBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= (textBox.getRight() - textBox.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() - textBox.getPaddingRight())) {
                        // your action here
                        sendMsg(FirebaseAuth.getInstance().getCurrentUser().getEmail(), textBox.getText().toString());
                        textBox.setText("");
                        hideSoftInputFromWindow();
                        textBox.clearFocus();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void hideSoftInputFromWindow() {
// Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void readData() {
        mMsgDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMsgList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MessageData msg = data.getValue(MessageData.class);
                    mMsgList.add(msg);
                }

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void sendMsg(String email, String msg) {
        MessageData message = new MessageData(email, msg);
        mMsgDatabase.push().setValue(message);

    }
}