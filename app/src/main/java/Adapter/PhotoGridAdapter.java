package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hemantpatel.mpfapp.R;

import java.util.ArrayList;

public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<String> mUrlList;

    public PhotoGridAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mUrlList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.grid_photo_layout_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       // Glide.with(mContext).load(mUrlList.get(position)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mUrlList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
}