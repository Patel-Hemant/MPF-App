package com.hemantpatel.mpfapp;

import static Constants.Params.DATABASE_MESSAGE_KEY;
import static Constants.Params.DATABASE_ROOT_KEY;
import static Constants.Params.DATA_TRANSFER_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.MessageListAdapter;
import Models.MessageData;
import Models.MissingPersonData;


public class MessageSendActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MessageListAdapter mAdapter;

    private ArrayList<MessageData> mMsgList;

    private DatabaseReference mMsgDatabase;
    private ValueEventListener mListener;

    private TextInputEditText textBox;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_send);

        initViews();

        textBox.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() >= (textBox.getRight() - textBox.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() - textBox.getPaddingRight())) {
                    // your action here
                    sendMsg(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), Objects.requireNonNull(textBox.getText()).toString());
                    textBox.setText("");
                    hideSoftInputFromWindow();
                    textBox.clearFocus();
                    return true;
                }
            }
            return false;
        });

        mListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMsgList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MessageData msg = data.getValue(MessageData.class);
                    mMsgList.add(msg);
                }
                mAdapter.notifyDataSetChanged();
                scrollDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mMsgDatabase.addValueEventListener(mListener);
    }

    private void initViews() {
        MissingPersonData mPersonData = (MissingPersonData) getIntent().getSerializableExtra(DATA_TRANSFER_KEY);
        mMsgDatabase = FirebaseDatabase.getInstance().getReference().child(DATABASE_ROOT_KEY).child(mPersonData.getUserId()).child(mPersonData.getName()).child(DATABASE_MESSAGE_KEY);
        mMsgList = new ArrayList<>();
        textBox = findViewById(R.id.msg_text_box);

        mAdapter = new MessageListAdapter(MessageSendActivity.this, mMsgList);

        mRecyclerView = findViewById(R.id.msg_list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MessageSendActivity.this));
    }

    private void hideSoftInputFromWindow() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void sendMsg(String email, String msg) {
        if (!msg.trim().equals("")) {
            MessageData message = new MessageData(email, msg);
            mMsgDatabase.push().setValue(message);
        }
    }

    public void scrollDown() {
        mRecyclerView.post(() -> {
            if (mMsgList.size() != 0) {
                mRecyclerView.smoothScrollToPosition(mMsgList.size() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMsgDatabase.removeEventListener(mListener);
    }
}