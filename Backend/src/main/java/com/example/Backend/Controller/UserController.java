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

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody RequestCreateAccount requestCreateAccount) throws Exception{
        userService.createAccount(requestCreateAccount);
        return ResponseEntity.ok("Đăng ký thành công");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> loginAccount(@RequestBody RequestLogin requestLogin) throws Exception {
        ApiResponse<User> user = userService.loginAccount(requestLogin);
        return new ResponseEntity<ApiResponse<User>>(user, HttpStatus.OK);
    }

    @GetMapping("/sendOTP")
    public ResponseEntity<String> sendOTP( @RequestParam String email) throws Exception {
        String rs = userService.sendOtp(email);
        if(rs.equals("Email đã được sử dụng")) return new ResponseEntity<String>(rs, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<String>(rs, HttpStatus.OK);
    }
}
