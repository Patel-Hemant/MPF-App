package Adapter;

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

import com.hemantpatel.mpfapp.MissingPersonDetailActivity;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

import Models.MissingPersonData;

public class MissingListAdapter extends RecyclerView.Adapter<MissingListAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<MissingPersonData> mList;

    public MissingListAdapter(Context context, ArrayList<MissingPersonData> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.missing_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MissingPersonData data = mList.get(position);

        holder.mName.setText(data.getName());
        holder.mAge.setText(String.format("Age: %s", data.getAge()));
        holder.mGender.setText(String.format("Gender: %s", data.getGender()));
        holder.mMissingDate.setText(String.format("missing date : %s", data.getMissing_date()));

        holder.detailBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mContext.startActivity(new Intent(mContext, MissingPersonDetailActivity.class));
                    }
                });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mMissingImage;
        private TextView mName;
        private TextView mAge;
        private TextView mGender;
        private TextView mMissingDate;
        private Button detailBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

           // mMissingImage = itemView.findViewById(R.id.missing_person_image);
            mName = itemView.findViewById(R.id.missing_name);
            mAge = itemView.findViewById(R.id.age);
            mGender = itemView.findViewById(R.id.gender);
            mMissingDate = itemView.findViewById(R.id.missing_date);
            detailBtn = itemView.findViewById(R.id.see_detail);
        }
    }
}
