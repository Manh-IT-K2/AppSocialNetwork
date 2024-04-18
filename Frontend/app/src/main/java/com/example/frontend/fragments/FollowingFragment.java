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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.frontend.R;
import com.example.frontend.adapter.FollowingAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.response.User.UserTrackingStatus;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FollowingFragment extends Fragment {

    private RecyclerView list_following;
    private List<UserResponse> userResponseList = new ArrayList<>();
    private List<UserResponse> myFollowingList = new ArrayList<>();
    private List<UserTrackingStatus> followingList = new ArrayList<>();
    private FollowingAdapter followingAdapter;
    private UserViewModel userViewModel;
    public FollowsViewModel followsViewModel;
    public LinearLayout linearLayout;
    ProgressBar progressBar;
    private String userId;
    private boolean isUserFollowingLoaded, isFollowingLoaded = false;

    public FollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        list_following = view.findViewById(R.id.list_following);
        linearLayout = view.findViewById(R.id.noDataFollowing);
        list_following.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // init call api suggest
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        followsViewModel = new ViewModelProvider(this).get(FollowsViewModel.class);

        // Hiển thị ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        list_following.setVisibility(View.GONE);

        userId = SharedPreferenceLocal.read(getContext().getApplicationContext(), "userId");
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("userId") != null) {
                userId = bundle.getString("userId", "");
                // Lấy danh sách following của user đã đăng nhập
                followsViewModel.getUserFollowingById(SharedPreferenceLocal.read(getContext().getApplicationContext(), "userId")).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
                    @Override
                    public void onChanged(ApiResponse<List<UserResponse>> response) {
                        Gson gson = new Gson();
                        String json = gson.toJson(response);
                        Log.d("my following", json);
                        if (response.getMessage().equals("Success!") && response.getStatus()) {
                            myFollowingList = response.getData();
                        } else {
                            // Xử lý khi không có dữ liệu hoặc có lỗi
                        }
                        isFollowingLoaded = true;
                        if (isUserFollowingLoaded) {
                            handleFollowingList();
                            progressBar.setVisibility(View.GONE);
                            followingAdapter = new FollowingAdapter(getContext(), followingList, followsViewModel, getViewLifecycleOwner(), userViewModel);
                            list_following.setAdapter(followingAdapter);
                        }
                    }
                });
            }

        }

        followsViewModel.getUserFollowingById(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
            @Override
            public void onChanged(ApiResponse<List<UserResponse>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.d("following", json);
                if (response.getMessage().equals("Success!") && response.getStatus()) {
                    list_following.setVisibility(View.VISIBLE);
                    userResponseList = response.getData();
                    if (userResponseList.size() > 0) {
                        if (userId.equals(SharedPreferenceLocal.read(getContext().getApplicationContext(), "userId"))) {
                            // Nếu profile hiện tại là tài khoản đã đăng  nhập
                            transferDataInUserTrackingStatusList();
                            progressBar.setVisibility(View.GONE);
                            followingAdapter = new FollowingAdapter(getContext(), followingList, followsViewModel, getViewLifecycleOwner(), userViewModel);
                            list_following.setAdapter(followingAdapter);
                        } else if (isFollowingLoaded) {
                            handleFollowingList();
                            progressBar.setVisibility(View.GONE);
                            followingAdapter = new FollowingAdapter(getContext(), followingList, followsViewModel, getViewLifecycleOwner(), userViewModel);
                            list_following.setAdapter(followingAdapter);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        list_following.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }

                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
                isUserFollowingLoaded = true;
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void handleFollowingList() {
        int tam = -1;
        for (int i = 0; i < userResponseList.size(); i++) {
            // Kiểm tra từng tài khoản xem có nằm trong danh sách đang theo dõi của tài khoản đang sử dựng không
            UserTrackingStatus trackingStatus = new UserTrackingStatus();
            trackingStatus.setId(userResponseList.get(i).getId());
            trackingStatus.setUsername(userResponseList.get(i).getUsername());
            trackingStatus.setName(userResponseList.get(i).getName());
            trackingStatus.setAvatarImg(userResponseList.get(i).getAvatarImg());

            if (userResponseList.get(i).getId().equals(SharedPreferenceLocal.read(getContext().getApplicationContext(), "userId"))) {
                tam = i;
                continue;
            }
            if (check_user_Followed(userResponseList.get(i).getId())) {
                trackingStatus.setStatus("1"); // tài khoản đang được theo dõi
                followingList.add(0, trackingStatus);
                continue;
            }
            trackingStatus.setStatus("2"); // tài khoản chưa được theo dõi
            followingList.add(trackingStatus);
        }
        if (tam > 0) {
            UserTrackingStatus trackingStatus = new UserTrackingStatus();
            trackingStatus.setId(userResponseList.get(tam).getId());
            trackingStatus.setUsername(userResponseList.get(tam).getUsername());
            trackingStatus.setName(userResponseList.get(tam).getName());
            trackingStatus.setAvatarImg(userResponseList.get(tam).getAvatarImg());
            trackingStatus.setStatus("0"); // tài khoản đã đăng nhập
            followingList.add(0, trackingStatus);
        }

    }

    // Kiểm tra người dùng hiện tại đã được follow chưa
    private boolean check_user_Followed(String id) {
        int i = 0;
        while (i < myFollowingList.size()) {
            if (id.equals(myFollowingList.get(i).getId())) {
                return true;
            }
            i++;
        }
        return false;
    }

    private void transferDataInUserTrackingStatusList() {
        for (int i = 0; i < userResponseList.size(); i++) {
            // Chuyển dữ liệu userResponseList sang userTrackingStatusList
            UserTrackingStatus trackingStatus = new UserTrackingStatus();
            trackingStatus.setId(userResponseList.get(i).getId());
            trackingStatus.setUsername(userResponseList.get(i).getUsername());
            trackingStatus.setName(userResponseList.get(i).getName());
            trackingStatus.setStatus("1");
            trackingStatus.setAvatarImg(userResponseList.get(i).getAvatarImg());
            followingList.add(trackingStatus);
        }
    }
}