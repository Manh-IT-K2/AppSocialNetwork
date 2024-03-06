package com.example.frontend.viewModel.User;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.UserRepository;
import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.response.ApiResponse.ApiResponse;

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<ApiResponse<Object>> apiResponseLiveData;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public void registerUser(RequestCreateAccount requestCreateAccount) {
        apiResponseLiveData = userRepository.registerUser(requestCreateAccount);
    }

    public String sendOTP(String email) {
        return userRepository.sendOTP(email);
    }
}
