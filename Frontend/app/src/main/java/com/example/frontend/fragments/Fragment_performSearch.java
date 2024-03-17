package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontend.R;
import com.example.frontend.adapter.SearchViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class Fragment_performSearch extends Fragment {

    TabLayout searchTabLayout;
    ViewPager2 viewPager2;
    SearchViewPagerAdapter searchViewPagerAdapter;

    public Fragment_performSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perform_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchTabLayout = view.findViewById(R.id.search_tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager);
        searchViewPagerAdapter = new SearchViewPagerAdapter(this);
        viewPager2.setAdapter(searchViewPagerAdapter);

        searchTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                searchTabLayout.getTabAt(position).select();
            }
        });
    }
}