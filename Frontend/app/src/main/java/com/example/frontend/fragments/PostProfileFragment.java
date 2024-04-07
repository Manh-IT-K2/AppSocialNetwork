package com.example.frontend.fragments;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontend.R;
import com.example.frontend.adapter.FollowerAdapter;
import com.example.frontend.adapter.PostAdapter;
import com.example.frontend.adapter.PostsProfileAdapter;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Post.PostResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.GridSpacingItemDecoration;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;

import java.util.List;

public class PostProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<RequestPostByUserId> postResponseList;
    private PostsProfileAdapter postsProfileAdapter;
    private UserViewModel userViewModel;
    public PostViewModel postViewModel;

    public PostProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_profile, container, false);
        recyclerView = view.findViewById(R.id.list_posts);
        // Khai báo GridLayoutManager với 3 cột
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        // Thiết lập layout manager cho RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        final int spacing = 1; // Khoảng cách mong muốn giữa các item
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(spacing, spacing, spacing, spacing); // Set khoảng cách giữa các item
            }
        });
        // init call api suggest
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        String userId = SharedPreferenceLocal.read(getContext().getApplicationContext(), "userId");
        postViewModel.getListPostByUserId(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<RequestPostByUserId>>>() {
            @Override
            public void onChanged(ApiResponse<List<RequestPostByUserId>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.d("check", json);
                if (response.getData().size() > 0) {
                    postResponseList = response.getData();
                    postsProfileAdapter = new PostsProfileAdapter(getContext(), postResponseList, getViewLifecycleOwner(),userViewModel,postViewModel);
                    recyclerView.setAdapter(postsProfileAdapter);
                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
            }
        });
        return view;
    }
}