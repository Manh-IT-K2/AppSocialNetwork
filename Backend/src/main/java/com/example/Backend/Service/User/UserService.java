package com.example.Backend.Service.User;

import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.User.RequestChangePasword;
import com.example.Backend.Request.User.RequestCreateAccount;
import com.example.Backend.Request.User.RequestLogin;
import com.example.Backend.Request.User.RequestTracking;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void createAccount (RequestCreateAccount requestCreateAccount) throws Exception;
    ApiResponse<User> loginAccount (RequestLogin requestCreateLogin) throws Exception;
    ApiResponse<String> sendOtp(String email);
    ApiResponse<String> sendOtp_forgotpassword(String email);
    ApiResponse<List<User>> getAllUsers();
    ApiResponse<User> changePassword(RequestChangePasword requestChangePasword) throws  Exception;
    ApiResponse<User> requestTrackingUser(RequestTracking requestTracking);
}
