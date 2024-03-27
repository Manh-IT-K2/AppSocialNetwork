package com.example.Backend.Controller;

import com.example.Backend.Entity.model.Message.GetQuantityFollows;
import com.example.Backend.Request.Follows.RequestCreateFollows;
import com.example.Backend.Request.Follows.RequestUpdateFollows;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Service.Follows.FollowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/getQuantityFollows")
    public ResponseEntity<ApiResponse<GetQuantityFollows>> getQuantityFollows(@RequestParam String id) throws Exception{
        ApiResponse<GetQuantityFollows> apiResponse = followsService.getQuantityFollows(id);
        return new ResponseEntity<ApiResponse<GetQuantityFollows>>(apiResponse, HttpStatus.OK);
    }
}
