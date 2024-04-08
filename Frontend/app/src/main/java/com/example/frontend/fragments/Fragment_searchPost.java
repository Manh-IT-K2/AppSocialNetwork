package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.frontend.R;
import com.example.frontend.adapter.SearchPostAdapter;
import com.example.frontend.adapter.SearchUserAdapter;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Post.PostResponse;
import com.example.frontend.utils.GridSpacingItemDecoration;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.Search.SearchQuery_ViewModel;

import java.util.ArrayList;
import java.util.List;

public class Fragment_searchPost extends Fragment {

    private RecyclerView recyclerView_Post;
    ProgressBar progressBar;
    private List<RequestPostByUserId> post_searchList = new ArrayList<>();
    private String id, searchQuery;
    private PostViewModel postViewModel;
    private SearchQuery_ViewModel searchQueryViewModel;

    public Fragment_searchPost() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
        searchQueryViewModel = new ViewModelProvider(requireActivity()).get(SearchQuery_ViewModel.class);

//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            if (bundle.getString("searchQuery") != null)
//                searchQuery = bundle.getString("searchQuery", "");
//        }
        searchQuery = searchQueryViewModel.getSearchQuery();
        id = SharedPreferenceLocal.read(getContext(), "userId");
        recyclerView_Post = view.findViewById(R.id.recyclerViewPost);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Bắt đầu tìm kiếm theo search query
        startSearch(id, searchQuery);

    }

    private void startSearch(String id, String searchQuery) {
        postViewModel.getListPostsBySearchQuery(id, searchQuery).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<RequestPostByUserId>>>() {
            @Override
            public void onChanged(ApiResponse<List<RequestPostByUserId>> listApiResponse) {
                post_searchList = listApiResponse.getData();
                progressBar.setVisibility(View.GONE);
                // Dua ket qua tim kiem post vao recyclerView
                putResultSearch_ToRecyclerView();
            }
        });
    }

    private void putResultSearch_ToRecyclerView() {
        // Set layout manager
        recyclerView_Post.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Set spacing between items
        int spacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView_Post.addItemDecoration(new GridSpacingItemDecoration(3, spacing, true));

        // Set adapter
        SearchPostAdapter searchPostAdapter = new SearchPostAdapter(getContext(), post_searchList);
        recyclerView_Post.setAdapter(searchPostAdapter);
    }
}