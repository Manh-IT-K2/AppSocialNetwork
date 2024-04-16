package com.example.frontend.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.frontend.fragments.Fragment_searchPost;
import com.example.frontend.fragments.Fragment_searchUser;
import com.example.frontend.fragments.SearchFragment;

public class SearchViewPagerAdapter extends FragmentStateAdapter {

    private final String query;

    public SearchViewPagerAdapter(@NonNull SearchFragment fragmentActivity, String query) {
        super(fragmentActivity);
        this.query = query;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("search_query", query);

        switch (position) {
            case 0:
                Fragment_searchUser searchUser = new Fragment_searchUser();
                searchUser.setArguments(bundle);
                return searchUser;
            case 1:
                Fragment_searchPost searchPost = new Fragment_searchPost();
                searchPost.setArguments(bundle);
                return searchPost;
            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}


