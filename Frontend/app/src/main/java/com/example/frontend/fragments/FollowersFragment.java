package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.frontend.R;
import com.example.frontend.adapter.FollowerAdapter;
import com.example.frontend.adapter.FollowingAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;

import java.util.List;

public class FollowersFragment extends Fragment {

    private RecyclerView list_follower;
    private List<UserResponse> userResponseList;
    private FollowerAdapter followerAdapter;
    private UserViewModel userViewModel;
    public FollowsViewModel followsViewModel;
    ProgressBar progressBar;

    public FollowersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        list_follower = view.findViewById(R.id.list_follower);
        list_follower.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // init call api suggest
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        followsViewModel = new ViewModelProvider(this).get(FollowsViewModel.class);

        // Hiển thị ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        list_follower.setVisibility(View.GONE);
        String userId = SharedPreferenceLocal.read(getContext().getApplicationContext(), "userId");
        followsViewModel.getUserFollowerById(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
            @Override
            public void onChanged(ApiResponse<List<UserResponse>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.d("following",json);
                if (response.getMessage().equals("Success!") && response.getStatus()) {
                    progressBar.setVisibility(View.GONE);
                    list_follower.setVisibility(View.VISIBLE);
                    userResponseList = response.getData();
                    followerAdapter = new FollowerAdapter(getContext(), userResponseList,followsViewModel,getViewLifecycleOwner(),userViewModel);
                    list_follower.setAdapter(followerAdapter);
                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
            }
        });
        return view;
    }
}