package com.example.Backend.Controller;

import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.User.RequestCreateAccount;
import com.example.Backend.Request.User.RequestLogin;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/createAccount")
    public ResponseEntity<ApiResponse<String>> createAccount(@RequestBody RequestCreateAccount requestCreateAccount) throws Exception{
        userService.createAccount(requestCreateAccount);
        ApiResponse<String> apiResponse = new ApiResponse<String>(true, "Tạo tài khoản thành công","");
        return new ResponseEntity<ApiResponse<String>>(apiResponse,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> loginAccount(@RequestBody RequestLogin requestLogin) throws Exception {
        ApiResponse<User> user = userService.loginAccount(requestLogin);
        return new ResponseEntity<ApiResponse<User>>(user, HttpStatus.OK);
    }

    @GetMapping("/sendOTP")
    public ResponseEntity<ApiResponse<String>> sendOTP( @RequestParam String email) throws Exception {
        ApiResponse<String> apiResponse = userService.sendOtp(email);
        return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() throws Exception {
        ApiResponse<List<User>> apiResponse = userService.getAllUsers();
        return new ResponseEntity<ApiResponse<List<User>>>(apiResponse, HttpStatus.OK);
    }
}
