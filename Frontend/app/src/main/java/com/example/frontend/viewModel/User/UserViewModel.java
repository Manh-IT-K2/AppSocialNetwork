package com.example.frontend.viewModel.User;

import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.UserRepository;
import com.example.frontend.request.User.RequestCreateAccount;

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public void registerUser(RequestCreateAccount requestCreateAccount) {
        userRepository.registerUser(requestCreateAccount);
    }

    public String sendOTP(String email) {
        return userRepository.sendOTP(email);
    }
}
