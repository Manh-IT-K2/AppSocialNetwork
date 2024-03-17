package com.example.frontend.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.frontend.R;
import com.example.frontend.viewModel.Search.SearchQuery_ViewModel;


public class SearchFragment extends Fragment {

    Fragment_searchUser fragmentSearchView;
    private SearchQuery_ViewModel searchQueryViewModel;
    private SearchView searchView;
    Toolbar toolbar;
    private MenuItem menuItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khởi tạo search Query ViewModel
        searchQueryViewModel = new ViewModelProvider(requireActivity()).get(SearchQuery_ViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentSearchView = new Fragment_searchUser();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_Search_Container, fragmentSearchView)
                .commit();


        searchView = view.findViewById(R.id.searchView);
        /*toolbar = view.findViewById(R.id.search_toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setTitle("");*/


        // Nhập nội dung tìm kiếm và bắt đầu tìm kiếm theo tên user
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchQueryViewModel.setSearchQuery(query);
                Fragment_performSearch fragment_performSearch = new Fragment_performSearch();
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.fragment_Search_Container, fragment_performSearch)
                        .addToBackStack(null) // Optional: Add to back stack for navigation
                        .commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchQueryViewModel.setSearchQuery(newText);

                // Trong fragment searchView bắt đầu lấy toàn bộ dữ liệu user
                // Sau đó tìm kiếm và hiển thị kết quả lên recyclerView trong fragment searchView
                fragmentSearchView.resultList();
                //search_User(newText);
                return false;
            }
        });


    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        //inflater.inflate(R.menu.search_menu, menu);
        menuItem = menu.findItem(R.id.menu_search);
        //searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        //searchView.setIconified(true);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQueryViewModel.setSearchQuery(query);
                Fragment_performSearch fragment_performSearch = new Fragment_performSearch();
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.fragment_Search_Container, fragment_performSearch)
                        .addToBackStack(null) // Optional: Add to back stack for navigation
                        .commit();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchQueryViewModel.setSearchQuery(newText);

                // Trong fragment searchView bắt đầu lấy toàn bộ dữ liệu user
                // Sau đó tìm kiếm và hiển thị kết quả lên recyclerView trong fragment searchView
                fragmentSearchView.resultList();
                //search_User(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }*/
}
