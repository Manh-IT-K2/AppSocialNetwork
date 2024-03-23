package com.example.frontend.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.FollowsRepository;
import com.example.frontend.repository.UserRepository;
import com.example.frontend.request.Follows.RequestCreateFollows;
import com.example.frontend.request.User.RequestCreateAccount;

public class FollowsViewModel extends ViewModel {
    private FollowsRepository followsRepository;

    public FollowsViewModel() {
        followsRepository = new FollowsRepository();
    }

    public void createFollows(RequestCreateFollows requestCreateFollows) {
        followsRepository.createFollows(requestCreateFollows);
    }
}
