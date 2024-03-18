package com.example.frontend.viewModel.User;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.UserRepository;
import com.example.frontend.request.User.RequestChangePW;
import com.example.frontend.request.User.RequestChangePass;
import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.request.User.RequestLogin;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public void registerUser(RequestCreateAccount requestCreateAccount) {
        userRepository.registerUser(requestCreateAccount);
    }

    public MutableLiveData<ApiResponse<UserResponse>> login(RequestLogin requestLogin) {
        return userRepository.login(requestLogin);
    }

    public MutableLiveData<ApiResponse<String>> sendOTP(String email) {
        return userRepository.sendOTP(email);
    }
    public MutableLiveData<ApiResponse<String>> sendOtp_forgotpassword(String email) {
        return userRepository.sendOtp_forgotpassword(email);
    }
    public MutableLiveData<ApiResponse<List<UserResponse>>> getAllUsers() {
        return userRepository.getAllUsers();
    }
    public MutableLiveData<ApiResponse<UserResponse>> changePW(RequestChangePW request) {
        return userRepository.changePW(request);
    }
    public MutableLiveData<ApiResponse<UserResponse>> changePass(RequestChangePass request) {
        return userRepository.changePass(request);
    }
}
