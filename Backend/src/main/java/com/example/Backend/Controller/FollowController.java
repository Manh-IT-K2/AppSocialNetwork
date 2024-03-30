package com.example.Backend.Controller;

import com.example.Backend.Entity.model.Message.GetQuantityFollows;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.Follows.RequestCreateFollows;
import com.example.Backend.Request.Follows.RequestUpdateFollows;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Service.Follows.FollowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/updateFollows")
    public ResponseEntity<ApiResponse<String>> updateFollows(@RequestBody RequestUpdateFollows follows) throws Exception{
        followsService.updateFollows(follows);
        ApiResponse<String> apiResponse = new ApiResponse<String>(true, "Success","");
        return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/deleteFollow")
    public ResponseEntity<ApiResponse<String>> deleteFollows(@RequestParam String idFollower,@RequestParam String idFollowing) throws Exception{
        ApiResponse<String> apiResponse = followsService.deleteFollow(idFollower,idFollowing);
        return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/getQuantityFollows")
    public ResponseEntity<ApiResponse<GetQuantityFollows>> getQuantityFollows(@RequestParam String id) throws Exception{
        ApiResponse<GetQuantityFollows> apiResponse = followsService.getQuantityFollows(id);
        return new ResponseEntity<ApiResponse<GetQuantityFollows>>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/getUserFollowingById")
    public ResponseEntity<ApiResponse<List<User>>> getUserFollowingById(@RequestParam String id) throws Exception{
        ApiResponse<List<User>> apiResponse = followsService.getUserFollowingById(id);
        return new ResponseEntity<ApiResponse<List<User>>>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/getUserFollowerById")
    public ResponseEntity<ApiResponse<List<User>>> getUserFollowerById(@RequestParam String id) throws Exception{
        ApiResponse<List<User>> apiResponse = followsService.getUserFollowerById(id);
        return new ResponseEntity<ApiResponse<List<User>>>(apiResponse, HttpStatus.OK);
    }
}
