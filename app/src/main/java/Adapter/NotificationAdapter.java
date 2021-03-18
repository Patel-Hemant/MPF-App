package Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hemantpatel.mpfapp.MessageSendActivity;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

import Models.MessageData;
import Models.MissingPersonData;
import Models.NotificationData;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<MissingPersonData> mList;
    ArrayList<MessageData> mMessageData;

    public NotificationAdapter(Context context, ArrayList<MissingPersonData> list, ArrayList<MessageData> messageData) {
        mContext = context;
        mList = list;
        mMessageData = messageData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.notification_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MissingPersonData data = mList.get(position);

        holder.mName.setText(data.getName());

        Log.d("Hemu","msg size : " + mMessageData.size());

        if (mMessageData.size() != 0 ){
        holder.mMessage.setText(String.format("%s : %s", mMessageData.get(position).getEmail().replace("@gmail.com", ""), mMessageData.get(position).getMsg()));
        }else {
            holder.mMessage.setText("no any message");
        }
        Glide.with(mContext).load(data.getPhoto_urls().get(0)).into(holder.mProfile);

        holder.mSeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageSendActivity.class);
                intent.putExtra("data", data);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mProfile;
        private TextView mName;
        private TextView mMessage;
        private Button mSeeBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mProfile = itemView.findViewById(R.id.profile);
            mName = itemView.findViewById(R.id.name);
            mMessage = itemView.findViewById(R.id.msg);
            mSeeBtn = itemView.findViewById(R.id.see_all_msg_btn);
        }
    }
}
