package com.example.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.adapter.PostAdapter;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class DetailPostsProfileFragment extends Fragment {

    private RecyclerView recyclerViewPost;
    private PostViewModel postViewModel;
    private UserViewModel userViewModel;
    private PostAdapter postAdapter;
    private Toolbar toolbar;
    private TextView nameUser;
    private List<RequestPostByUserId> postList;
    public DetailPostsProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_posts_profile, container, false);
        recyclerViewPost = view.findViewById(R.id.list_post_detail);
        toolbar = view.findViewById(R.id.toolbarDetail);
        nameUser = view.findViewById(R.id.nameUserDetail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewPost.setLayoutManager(layoutManager);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        String userId = SharedPreferenceLocal.read(getContext().getApplicationContext(), "userId");
        String location = getArguments().getString("postId");
        postViewModel.getListPostByUserId(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<RequestPostByUserId>>>() {
            @Override
            public void onChanged(ApiResponse<List<RequestPostByUserId>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.d("check1", json);
                if (response.getData() != null) {
                    postList = response.getData();
                    postAdapter = new PostAdapter(getContext(), postList, (LifecycleOwner) getContext());

                    recyclerViewPost.setAdapter(postAdapter);
                    if (location != null) {
                        // Gọi phương thức để cuộn đến bài post cụ thể
                        scrollToPost(location);
                    }
                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
            }
        });
        userViewModel.getDetailUserById(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()) {
                    nameUser.setText(response.getData().getUsername());
                }
            }
        });
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity (), MainActivity.class);
                intent.putExtra("fragment_to_load", "back_profile");
                startActivity(intent);
            }
        });
    }

    public void scrollToPost(String postId) {
        // Tìm vị trí của bài post trong danh sách
        for (int i = 0; i < postList.size(); i++) {
            if (postList.get(i).getIdPost().equals(postId)) {
                // Cuộn RecyclerView đến vị trí của bài post
                recyclerViewPost.scrollToPosition(i);
                break;
            }
        }
    }
}