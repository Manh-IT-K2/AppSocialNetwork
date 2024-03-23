package com.example.frontend.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference_SearchHistory {

    private Context _context;
    private SharedPreferences pref_searchHistory;
    private SharedPreferences.Editor editor_searchHistory;
    private static final String PREF_SEARCH_HISTORY = "search_history";
    private static final String HISTORY = "history";

    public SharedPreference_SearchHistory(Context context) {
        this._context = context;
        pref_searchHistory = _context.getSharedPreferences(PREF_SEARCH_HISTORY, Context.MODE_PRIVATE);
        editor_searchHistory = pref_searchHistory.edit();
    }

    public void save_SearchHistoryList(String value){
        editor_searchHistory.putString(HISTORY, value);
        editor_searchHistory.apply();
    }

    public String read_SearchHistoryList(){
        return pref_searchHistory.getString(HISTORY, "");
    }
}
