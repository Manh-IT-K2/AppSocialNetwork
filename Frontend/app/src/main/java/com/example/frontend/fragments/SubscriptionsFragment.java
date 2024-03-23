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

import com.example.frontend.R;
import com.example.frontend.adapter.StoryAdapter;
import com.example.frontend.adapter.SuggestedMeAdapter;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.request.Story.RequestStoryByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;

import java.util.List;

public class SubscriptionsFragment extends Fragment {


    private RecyclerView list_suggestedMe;
    private List<UserResponse> userResponseList;
    private SuggestedMeAdapter suggestedMeAdapter;
    private UserViewModel userViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        // init list Suggested Me
        list_suggestedMe = view.findViewById(R.id.list_suggestedMe);
        list_suggestedMe.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        // init call api suggest
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
            @Override
            public void onChanged(ApiResponse<List<UserResponse>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.d("check1", json);
                if (response.getData().size() > 0) {
                    userResponseList = response.getData();
                    suggestedMeAdapter = new SuggestedMeAdapter(getContext(), userResponseList);
                    list_suggestedMe.setAdapter(suggestedMeAdapter);
                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
            }
        });
        return view;
    }
}