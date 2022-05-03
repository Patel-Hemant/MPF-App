package Adapter;

import static Constants.Params.DATA_TRANSFER_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hemantpatel.mpfapp.MessageSendActivity;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import Models.MessageData;
import Models.MissingPersonData;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<MissingPersonData> mList;
    HashMap<String, MessageData> mMessageData;

    public NotificationAdapter(Context context, ArrayList<MissingPersonData> list, HashMap<String, MessageData> messageData) {
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
        MessageData msgData = mMessageData.get(data.getName());

        holder.mName.setText(data.getName());

        if (msgData != null) {
            holder.mMessage.setText(String.format("%s : %s", msgData.getEmail().replace("@gmail.com", ""), msgData.getMsg()));
        } else {
            holder.mMessage.setText("no any message");
        }


        Glide.with(mContext)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_person))
                .load(data.getPhoto_urls().get(0)).circleCrop().into(holder.mProfile);

        holder.mSeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageSendActivity.class);
                intent.putExtra(DATA_TRANSFER_KEY, data);
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
