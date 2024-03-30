package com.example.frontend.viewModel.Follows;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.FollowsRepository;
import com.example.frontend.repository.UserRepository;
import com.example.frontend.request.Follows.RequestCreateFollows;
import com.example.frontend.request.Follows.RequestUpdateFollows;
import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Follows.FollowsResponse;
import com.example.frontend.response.Follows.GetQuantityResponse;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

public class FollowsViewModel extends ViewModel {
    private final FollowsRepository followsRepository;

    public FollowsViewModel() {
        followsRepository = new FollowsRepository();
    }

    public MutableLiveData<ApiResponse<String>> createFollows(RequestCreateFollows requestCreateFollows) {
         return followsRepository.createFollows(requestCreateFollows);
    }
    public MutableLiveData<ApiResponse<GetQuantityResponse>> getQuantityFollows(String id) {
        return followsRepository.getQuantityFollows(id);
    }
    public MutableLiveData<ApiResponse<String>> updateFollows(RequestUpdateFollows requestUpdateFollows) {
        return followsRepository.updateFollows(requestUpdateFollows);
    }
    public MutableLiveData<ApiResponse<String>> deleteFollows(String idFollower,String idFollowing) {
        return followsRepository.deleteFollows(idFollower,idFollowing);
    }
    public MutableLiveData<ApiResponse<List<UserResponse>>> getUserFollowingById(String id) {
        return followsRepository.getUserFollowingById(id);
    }
    public MutableLiveData<ApiResponse<List<UserResponse>>> getUserFollowerById(String id) {
        return followsRepository.getUserFollowerById(id);
    }
}
