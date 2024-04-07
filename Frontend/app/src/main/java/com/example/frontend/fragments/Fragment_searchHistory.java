package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontend.R;
import com.example.frontend.adapter.SearchHistoryAdapter;
import com.example.frontend.response.Search.SearchHistoryResponse;
import com.example.frontend.utils.SharedPreference_SearchHistory;
import com.example.frontend.viewModel.Search.SearchQuery_ViewModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Fragment_searchHistory extends Fragment {

    private RecyclerView recyclerView_searchHistory;
    private SearchHistoryAdapter searchHistoryAdapter;
    private ArrayList<SearchHistoryResponse> searchHistoryResponseArrayList;
    private SharedPreference_SearchHistory sharedPreferenceSearchHistory;
    private Gson gson;

    private SearchQuery_ViewModel searchQueryViewModel;
    private SearchView searchView;


    public Fragment_searchHistory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khởi tạo search Query ViewModel
        searchQueryViewModel = new ViewModelProvider(requireActivity()).get(SearchQuery_ViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView_searchHistory = view.findViewById(R.id.recyclerView_SearchHistory);
        searchView = getActivity().findViewById(R.id.searchView);

        gson = new Gson();
        sharedPreferenceSearchHistory = new SharedPreference_SearchHistory(getActivity());
        // Lay du lieu trong shared preference
        getSearchHistoryListFromSharedPreference();
        // Dua search history vao recyclerView
        putSearchHistoryListToRecyclerView();
        // Xu ly click mot item trong recycler view
        clickOneSearchHistory();
    }

    private void getSearchHistoryListFromSharedPreference() {
        String jsonHistory = sharedPreferenceSearchHistory.read_SearchHistoryList();
        Type type = new TypeToken<List<SearchHistoryResponse>>(){}.getType();
        searchHistoryResponseArrayList = gson.fromJson(jsonHistory, type);

        if (searchHistoryResponseArrayList == null) {
            searchHistoryResponseArrayList = new ArrayList<>();
        }
    }

    private void putSearchHistoryListToRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_searchHistory.setLayoutManager(layoutManager);
        searchHistoryAdapter = new SearchHistoryAdapter(getContext(), searchHistoryResponseArrayList);
        recyclerView_searchHistory.setAdapter(searchHistoryAdapter);
    }

    private void clickOneSearchHistory() {

        searchHistoryAdapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClickToRemove(int position) {
                deleteOneSearchHistory(position);
            }

            @Override
            public void onItemClickToSearch(int position) {
                clickOneSearchHistoryToSearch(position);
            }
        });
    }

    private void deleteOneSearchHistory(int position) {

        // Xoa mot item
        // Xoa item o vi tri position
        searchHistoryResponseArrayList.remove(position);

        // Cap nhat lai lich su tim kiem trong shared preference
        saveSearchHistoryListToSharedPreference(searchHistoryResponseArrayList);

        // Thong bao cho Adapter khi du lieu bi thay doi
        searchHistoryAdapter.notifyItemRemoved(position);
    }

    private void clickOneSearchHistoryToSearch(int position) {

        // Nếu lịch sử là text
        if (searchHistoryResponseArrayList.get(position).getAccount() == false) {
            // Set text o vi tri position vào ViewModel để Fragment_searchUser có thể lấy được text
            searchQueryViewModel.setSearchQuery(searchHistoryResponseArrayList.get(position).getText());

            // Chuyen sang fragment perform search
            Fragment_performSearch fragment_performSearch = new Fragment_performSearch();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_Search_Container, fragment_performSearch)
                    .commit();

            // Set search query to searchView of SearchFragment
            searchView.setQuery(searchHistoryResponseArrayList.get(position).getText(), true);
            searchView.clearFocus();
        }

        // Nếu lịch sử là tài khoản
        else {

            Bundle args = new Bundle();
            args.putString("userId", searchHistoryResponseArrayList.get(position).getId());

            ProfileFragment clickAccount = new ProfileFragment();
            clickAccount.setArguments(args);

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_layout_main, clickAccount).addToBackStack(null).commit();

            // Xoa account trong shared preference trùng với account ở vị trí position trong arraylist
            SearchHistoryResponse searchHistoryResponse = new SearchHistoryResponse();
            searchHistoryResponse = searchHistoryResponseArrayList.get(position);
            searchHistoryResponseArrayList.remove(searchHistoryResponseArrayList.get(position));
            searchHistoryResponseArrayList.add(0, searchHistoryResponse);
            // Luu vao shared preference
            saveSearchHistoryListToSharedPreference(searchHistoryResponseArrayList);
        }
    }

    private void saveSearchHistoryListToSharedPreference(ArrayList<SearchHistoryResponse> searchHistoryList) {
        // convert object to String by Gson
        String jsonHistory = gson.toJson(searchHistoryList);

        // save to shared preference
        sharedPreferenceSearchHistory.save_SearchHistoryList(jsonHistory);
    }
}