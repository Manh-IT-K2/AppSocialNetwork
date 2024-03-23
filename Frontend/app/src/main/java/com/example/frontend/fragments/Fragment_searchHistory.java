package com.example.frontend.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontend.R;
import com.example.frontend.adapter.SearchHistoryAdapter;
import com.example.frontend.adapter.SearchUserAdapter;
import com.example.frontend.response.Search.SearchHistoryResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.utils.SharedPreference_SearchHistory;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Fragment_searchHistory extends Fragment {

    private RecyclerView recyclerView_searchHistory;

    private ArrayList<SearchHistoryResponse> searchHistoryResponseArrayList;
    private SharedPreference_SearchHistory sharedPreferenceLocal;
    private Gson gson;


    public Fragment_searchHistory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView_searchHistory = view.findViewById(R.id.recyclerView_SearchHistory);

        gson = new Gson();
        sharedPreferenceLocal = new SharedPreference_SearchHistory(getActivity());
        getSearchHistoryListFromSharedPreference();
        putSearchHistoryListToRecyclerView();
    }

    private void getSearchHistoryListFromSharedPreference() {
        String jsonHistory = sharedPreferenceLocal.read_SearchHistoryList();
        Type type = new TypeToken<List<SearchHistoryResponse>>(){}.getType();
        searchHistoryResponseArrayList = gson.fromJson(jsonHistory, type);

        if (searchHistoryResponseArrayList == null) {
            searchHistoryResponseArrayList = new ArrayList<>();
        }
    }

    private void putSearchHistoryListToRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_searchHistory.setLayoutManager(layoutManager);
        SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(getContext(), searchHistoryResponseArrayList);
        recyclerView_searchHistory.setAdapter(searchHistoryAdapter);
    }

}