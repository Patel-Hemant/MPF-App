package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

import Models.NotificationData;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<NotificationData> mList;

    public NotificationAdapter(Context context, ArrayList<NotificationData> list) {
        mContext = context;
        mList = list;
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
        NotificationData data = mList.get(position);

        holder.mName.setText(data.getName());
        holder.mMessage.setText(data.getMsg());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mProfile;
        private TextView mName;
        private TextView mMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mProfile = itemView.findViewById(R.id.profile);
            mName = itemView.findViewById(R.id.name);
            mMessage = itemView.findViewById(R.id.msg);
        }
    }
}
