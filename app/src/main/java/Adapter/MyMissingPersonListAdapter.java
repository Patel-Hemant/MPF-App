package Adapter;

import static Constants.Params.DATA_TRANSFER_KEY;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hemantpatel.mpfapp.EditActivity;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

import Fragments.AccountFragment;
import Models.MissingPersonData;

public class MyMissingPersonListAdapter extends RecyclerView.Adapter<MyMissingPersonListAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<MissingPersonData> mList;
    AlertDialog.Builder mBuilder;
    AccountFragment mFragment;

    public MyMissingPersonListAdapter(Context context, ArrayList<MissingPersonData> list, AccountFragment fragment) {
        mContext = context;
        mList = list;
        mBuilder = new AlertDialog.Builder(context);
        mFragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.account_fragment_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(mContext).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_person)).load(mList.get(position).getPhoto_urls().get(0)).circleCrop().into(holder.mProfileImage);
        holder.mName.setText(mList.get(position).getName());

        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog(position);
            }
        });

        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra(DATA_TRANSFER_KEY, mList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    // show dialog for delete confermation
    private void showDeleteDialog(int position) {
        mBuilder.setMessage("Do you really want to delete ?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mFragment.deleteData(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = mBuilder.create();
        //Setting the title manually
        alert.setTitle("Delete");
        alert.show();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mProfileImage, mEditBtn, mDeleteBtn;
        TextView mName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImage = itemView.findViewById(R.id.profile);
            mEditBtn = itemView.findViewById(R.id.edit_btn);
            mDeleteBtn = itemView.findViewById(R.id.delete_btn);
            mName = itemView.findViewById(R.id.name);
        }
    }
}