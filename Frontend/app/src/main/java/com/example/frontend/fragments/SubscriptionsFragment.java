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
import com.example.frontend.adapter.SuggestedMeAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.GetAllUserByFollowsResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;

import java.util.List;

public class SubscriptionsFragment extends Fragment{


    private RecyclerView list_suggestedMe;
    private List<GetAllUserByFollowsResponse> userResponseList;
    private SuggestedMeAdapter suggestedMeAdapter;
    private UserViewModel userViewModel;
    public FollowsViewModel followsViewModel;

    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        list_suggestedMe = view.findViewById(R.id.list_suggestedMe);
        list_suggestedMe.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // init call api suggest
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        followsViewModel = new ViewModelProvider(this).get(FollowsViewModel.class);

        // Hiển thị ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        list_suggestedMe.setVisibility(View.GONE);
        String userId = SharedPreferenceLocal.read(getContext().getApplicationContext(), "userId");
        userViewModel.getAllUsersByFollows(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<GetAllUserByFollowsResponse>>>() {
            @Override
            public void onChanged(ApiResponse<List<GetAllUserByFollowsResponse>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                if (response.getData().size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    list_suggestedMe.setVisibility(View.VISIBLE);
                    userResponseList = response.getData();
                    suggestedMeAdapter = new SuggestedMeAdapter(getContext(), userResponseList,followsViewModel,getViewLifecycleOwner(),userViewModel);
                    list_suggestedMe.setAdapter(suggestedMeAdapter);
                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
            }
        });
        return view;
    }
}