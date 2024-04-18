package com.example.frontend.viewModel.User;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.UserRepository;
import com.example.frontend.request.Notification.Notification;
import com.example.frontend.response.User.NotificationResponse;
import com.example.frontend.request.User.RequestChangePW;
import com.example.frontend.request.User.RequestChangePass;
import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.request.User.RequestLogin;
import com.example.frontend.request.User.RequestUpdateTokenFCM;
import com.example.frontend.request.User.RequestUpdateUser;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.GetAllUserByFollowsResponse;
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

    public MutableLiveData<ApiResponse<List<String>>> getListUserName() {
        return userRepository.getListUserName();
    }

    public MutableLiveData<ApiResponse<String>> sendOTP(String email) {
        return userRepository.sendOTP(email);
    }
    public MutableLiveData<ApiResponse<String>> sendOTP_forgotpassword(String email) {
        return userRepository.sendOTP_forgotpassword(email);
    }
    public MutableLiveData<ApiResponse<List<UserResponse>>> getAllUsers() {
        return userRepository.getAllUsers();
    }
    public MutableLiveData<ApiResponse<List<GetAllUserByFollowsResponse>>> getAllUsersByFollows(String id) {
        return userRepository.getAllUsersByFollows(id);
    }
    public MutableLiveData<ApiResponse<List<UserResponse>>> getRequestTrackingUser() {
        return userRepository.getRequestTrackingUser();
    }
    public MutableLiveData<ApiResponse<UserResponse>> getDetailUserById(String id) {
        return userRepository.getDetailUserById(id);
    }
    public MutableLiveData<ApiResponse<UserResponse>> updateUser(RequestUpdateUser requestUpdateUser) {
        return userRepository.updateUser(requestUpdateUser);
    }
    public MutableLiveData<ApiResponse<UserResponse>> changePW(RequestChangePW request) {
        return userRepository.changePW(request);
    }
    public MutableLiveData<ApiResponse<UserResponse>> changePass(RequestChangePass request) {
        return userRepository.changePass(request);
    }
    public MutableLiveData<ApiResponse<List<UserResponse>>>findUser_privatechat(String keyword) {
        return userRepository.findUser_privatechat(keyword);
    }
    public void updateTokenFCM(RequestUpdateTokenFCM request){
        userRepository.updateTokenFCM(request);
    }

    public MutableLiveData<ApiResponse<String>> getTokenFCM(String id){
        return userRepository.getTokenFCM(id);
    }

    public void addNotification(Notification notification){
        userRepository.addNotification(notification);
    }

    public MutableLiveData<ApiResponse<List<NotificationResponse>>> getNotification(String id){
        return userRepository.getNotification(id);
    }
}
