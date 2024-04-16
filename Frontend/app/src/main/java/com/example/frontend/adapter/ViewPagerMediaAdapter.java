package com.example.frontend.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.frontend.fragments.PostProfileFragment;
import com.example.frontend.fragments.PostsUserLikedFragment;
import com.example.frontend.fragments.WatchProfileFragment;

import java.util.ArrayList;

public class ViewPagerMediaAdapter extends FragmentStateAdapter {


    public ViewPagerMediaAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new PostProfileFragment();
            case 1:
                return new WatchProfileFragment();
            case 2:
                return new PostsUserLikedFragment();
            default:
                return new PostProfileFragment();
      }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
