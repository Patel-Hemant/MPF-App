package Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

import Models.MessageData;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<MessageData> mList;
    String myEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    public MessageListAdapter(Context context, ArrayList<MessageData> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.msg_list_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String email = mList.get(position).getEmail().replace("@gmail.com", "");
        holder.email.setText(email);
        holder.msg.setText(mList.get(position).getMsg());

        if (myEmail.equals(mList.get(position).getEmail())) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.RIGHT;
            params.weight = 1.0f;

            holder.email.setVisibility(View.GONE);
            holder.msg.setLayoutParams(params);
            //   change color of text background
            holder.msg.setBackgroundResource(R.drawable.my_msg_item_bg);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.LEFT;
            params.weight = 1.0f;

            holder.msg.setLayoutParams(params);
            holder.email.setLayoutParams(params);
            //   change color of text background
            holder.msg.setBackgroundResource(R.drawable.other_msg_item_layout);


        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView email, msg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.msg_list_email);
            msg = itemView.findViewById(R.id.msg_list_msg);
        }
    }
}
