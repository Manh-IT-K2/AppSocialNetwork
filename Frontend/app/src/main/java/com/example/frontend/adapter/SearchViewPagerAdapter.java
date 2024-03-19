package com.example.frontend.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.frontend.fragments.Fragment_performSearch;
import com.example.frontend.fragments.Fragment_searchPost;
import com.example.frontend.fragments.Fragment_searchUser;
import com.example.frontend.fragments.SearchFragment;

public class SearchViewPagerAdapter extends FragmentStateAdapter {

    public SearchViewPagerAdapter(@NonNull Fragment_performSearch fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Fragment_searchUser();
            case 1:
                return new Fragment_searchPost();
            default:
                return new SearchFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}


