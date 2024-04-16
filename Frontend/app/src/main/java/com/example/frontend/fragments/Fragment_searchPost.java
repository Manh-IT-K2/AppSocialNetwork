package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.adapter.PostAdapter;
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
    private ProgressBar progressBar;
    private TextView search_noResult;
    private List<RequestPostByUserId> post_searchList;
    private String id, searchQuery;
    private PostViewModel postViewModel;
    SearchPostAdapter searchPostAdapter;

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("search_query") != null)
                searchQuery = bundle.getString("search_query");
        }
        id = SharedPreferenceLocal.read(getContext(), "userId");

        recyclerView_Post = view.findViewById(R.id.recyclerViewPost);
        search_noResult = view.findViewById(R.id.search_noResults);
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
                postViewModel.setListPostsBySearchQuery(post_searchList);
                progressBar.setVisibility(View.GONE);
                if (!post_searchList.isEmpty()) {
                    search_noResult.setText("");
                    // Dua ket qua tim kiem post vao recyclerView
                    putResultSearch_ToRecyclerView();
                } else {
                    search_noResult.setText(R.string.noResults_wereFound);
                }
            }
        });
    }

    private void putResultSearch_ToRecyclerView() {
        // Set layout manager
        recyclerView_Post.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Set spacing between items
        int spacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView_Post.addItemDecoration(new GridSpacingItemDecoration(3, spacing, false));

        // Set adapter
        searchPostAdapter = new SearchPostAdapter(getContext(), post_searchList);
        recyclerView_Post.setAdapter(searchPostAdapter);

        // Khi nhan vao mot post
        clickAPost();
    }

    private void clickAPost() {
        searchPostAdapter.setOnItemClickListener(new SearchPostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Bundle bundle = new Bundle();
                bundle.putInt("position", position);

                Fragment_Searched_Posts fragment_searched_posts= new Fragment_Searched_Posts();
                fragment_searched_posts.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_layout_main, fragment_searched_posts).addToBackStack("search_post").commit();
            }
        });
    }
}