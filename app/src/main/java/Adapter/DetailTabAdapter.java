package Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import Fragments.DetailFragment;
import Fragments.PhotosFragment;

public class DetailTabAdapter extends FragmentPagerAdapter {
    Context mContext;
    int totalTabs;

    public DetailTabAdapter(Context context, @NonNull FragmentManager fm, int totalTabs) {
        super(fm);
        mContext = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DetailFragment();
            case 1:
                return new PhotosFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
