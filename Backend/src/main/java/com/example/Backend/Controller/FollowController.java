package com.example.Backend.Controller;

import com.example.Backend.Request.Follows.RequestCreateFollows;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Service.Follows.FollowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/follows")
public class FollowController {
    @Autowired
    FollowsService followsService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createFollows(@RequestBody RequestCreateFollows requestCreateFollows) throws Exception{
        followsService.createFollows(requestCreateFollows);
        ApiResponse<String> apiResponse = new ApiResponse<String>(true, "Success","");
        return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
    }
}
