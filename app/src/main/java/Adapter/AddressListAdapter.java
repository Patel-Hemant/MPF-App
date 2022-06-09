package Adapter;

import static android.app.Activity.RESULT_OK;

import static Constants.Params.ADDRESS_CODE;
import static Constants.Params.LAT_CODE;
import static Constants.Params.LON_CODE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hemantpatel.mpfapp.R;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressViewHolder> {
    List<Address> mList;
    Context mContext;
    Activity mActivity;

    public AddressListAdapter(List<Address> mList, Activity mActivity) {
        this.mList = mList;
        this.mContext = mActivity;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.address_list_item, parent, false);
        return new AddressListAdapter.AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Address address = mList.get(position);

        holder.address_line.setText(address.getAddressLine(0));
        // holder.address_line_lower.setText(address.getAddressLine(0));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // return the result to previous activity
                Intent data = new Intent();
                data.putExtra(ADDRESS_CODE, address.getAddressLine(0));
                data.putExtra(LAT_CODE, address.getLatitude());
                data.putExtra(LON_CODE, address.getLongitude());
                mActivity.setResult(RESULT_OK, data);
                mActivity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView address_line;
        TextView address_line_lower;
        ImageView add_address_btn;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);

            address_line = itemView.findViewById(R.id.address_tv);
            address_line_lower = itemView.findViewById(R.id.address_lower_tv);
            add_address_btn = itemView.findViewById(R.id.add_address_img_btn);
        }
    }
}