package com.example.frontend.viewModel.Search;

import androidx.lifecycle.ViewModel;

public class SearchQuery_ViewModel extends ViewModel {
    private String searchQuery;

    public void setSearchQuery(String query) {
        searchQuery = query;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
