package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontend.R;
import com.example.frontend.adapter.SearchPostAdapter;
import com.example.frontend.adapter.SearchUserAdapter;
import com.example.frontend.response.Post.PostResponse;

import java.util.List;

public class Fragment_searchPost extends Fragment {

    private RecyclerView recyclerView_Post;
    private List<PostResponse> post_searchList;

    public Fragment_searchPost() {
        // Required empty public constructor
    }
    public static Fragment_searchPost newInstance(String param1, String param2) {
        Fragment_searchPost fragment = new Fragment_searchPost();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
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

        recyclerView_Post = view.findViewById(R.id.recyclerViewPost);

        // Dua ket qua tim kiem post vao recyclerView
        putResultSearch_ToRecyclerView();
    }

    private void putResultSearch_ToRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView_Post.setLayoutManager(layoutManager);
        SearchPostAdapter searchPostAdapter = new SearchPostAdapter(getContext(), post_searchList);
        recyclerView_Post.setAdapter(searchPostAdapter);
    }
}