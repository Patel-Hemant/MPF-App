package Adapter;

import static Constants.Params.IMAGE_URL_TRANSFER_KEY;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hemantpatel.mpfapp.ImagePreviewActivity;
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
        Glide.with(mContext).load(mUrlList.get(position)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.mProgressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.img);


        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImagePreviewActivity.class);

                intent.putExtra(IMAGE_URL_TRANSFER_KEY, mUrlList.get(position));
                ActivityOptionsCompat options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, holder.img, holder.img.getTransitionName());
                }
                mContext.startActivity(intent, options.toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUrlList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ProgressBar mProgressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            mProgressBar = itemView.findViewById(R.id.prgrss);
        }
    }
}