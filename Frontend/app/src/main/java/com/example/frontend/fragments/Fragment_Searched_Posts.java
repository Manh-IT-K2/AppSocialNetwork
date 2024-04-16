package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.adapter.PostAdapter;
import com.example.frontend.adapter.SearchPostAdapter;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.Search.SearchQuery_ViewModel;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Searched_Posts extends Fragment {

    private Toolbar toolbar;
    private TextView txtSearchQuery;
    private RecyclerView recyclerView_Post;
    private PostAdapter postAdapter;
    private SearchQuery_ViewModel searchQueryViewModel;
    private PostViewModel postViewModel;
    private List<RequestPostByUserId> post_searchList = new ArrayList<>();
    private Integer position;

    public Fragment_Searched_Posts() {
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

        searchQueryViewModel = new ViewModelProvider(requireActivity()).get(SearchQuery_ViewModel.class);
        postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);

        post_searchList = postViewModel.getListPostsBySearchQuery();
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getInt("position", -1) != -1) {
                position = bundle.getInt("position");
            }
        }

        toolbar = view.findViewById(R.id.toolbar_searchPost);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();

            }
        });

        txtSearchQuery = view.findViewById(R.id.txtSearchQuery);
        txtSearchQuery.setText(searchQueryViewModel.getSearchQuery());

        recyclerView_Post = view.findViewById(R.id.recyclerViewPost);

        // Dua ket qua tim kiem post vao recyclerView
        putResultSearch_ToRecyclerView();
    }

    private void putResultSearch_ToRecyclerView() {

        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_Post.setLayoutManager(layoutManager);

        // Set adapter
        postAdapter = new PostAdapter(getContext(), post_searchList, getViewLifecycleOwner());
        recyclerView_Post.setAdapter(postAdapter);

        // Cuộn RecyclerView đến vị trí mong muốn
        // Sử dụng scrollToPosition() để cuộn ngay lập tức
        layoutManager.scrollToPosition(position);
    }


}