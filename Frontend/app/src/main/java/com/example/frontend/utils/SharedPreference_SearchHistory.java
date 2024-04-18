package com.example.frontend.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

import com.example.frontend.response.Search.SearchHistoryResponse;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreference_SearchHistory {

    private Context _context;
    private SharedPreferences pref_searchHistory;
    private SharedPreferences.Editor editor_searchHistory;
    private Gson gson;
    private static final String PREF_SEARCH_HISTORY = "search_history";
    private static final String HISTORY = "history";

    public SharedPreference_SearchHistory(Context context) {
        this._context = context;
        pref_searchHistory = _context.getSharedPreferences(PREF_SEARCH_HISTORY, Context.MODE_PRIVATE);
        editor_searchHistory = pref_searchHistory.edit();
    }

    public List<SearchHistoryResponse> read_SearchHistoryList(List<SearchHistoryResponse> searchHistoryResponseArrayList) {
        gson = new Gson();
        String jsonHistory = pref_searchHistory.getString(HISTORY, "");;
        Type type = new TypeToken<List<SearchHistoryResponse>>() {
        }.getType();
        searchHistoryResponseArrayList = gson.fromJson(jsonHistory, type);

        if (searchHistoryResponseArrayList == null) {
            searchHistoryResponseArrayList = new ArrayList<>();
        }
        return searchHistoryResponseArrayList;
    }

    public void save_SearchHistoryList(List<SearchHistoryResponse> searchHistoryList) {
        // convert object to String by Gson
        String jsonHistory = gson.toJson(searchHistoryList);

        // save to shared preference
        editor_searchHistory.putString(HISTORY, jsonHistory);
        editor_searchHistory.apply();
    }
}
