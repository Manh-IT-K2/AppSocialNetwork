package com.example.Backend.Controller;

import com.example.Backend.Request.Post.RequestPost;
import com.example.Backend.Request.Post.RequestPostByUserId;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Service.Post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    PostService postService;

    // createPost
    @PostMapping("/createPost")
    public ResponseEntity<ApiResponse<String>> createPost(@RequestBody RequestPost requestPost, @RequestParam String userId) throws Exception{
        postService.createPost(requestPost,userId);
        ApiResponse<String> apiResponse = new ApiResponse<String>(true, "Đăng bài thành công !","");
        return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
    }

    // select post
    @GetMapping("/getListPostByUserId")
    public ResponseEntity<ApiResponse<List<RequestPostByUserId>>> getListPostByUserId(@RequestParam String userId) {
        if (userId.isEmpty()) userId = "";
        System.out.println(userId);
        ApiResponse<List<RequestPostByUserId>> result = postService.getListPostsByUserId(userId);
        return new ResponseEntity<ApiResponse<List<RequestPostByUserId>>>(result, HttpStatus.OK);
    }

}
