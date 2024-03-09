package com.example.Backend.Controller;

import com.example.Backend.Request.Post.RequestPost;
import com.example.Backend.Request.Post.RequestPostByUserId;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Service.Post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getListPostByUserId(@RequestParam String userId) {
        try {
            if (userId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<String>(false,"userId is required!",""));
            }
            List<RequestPostByUserId> result = postService.getListPostsByUserId(userId);
            return result == null ? ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<String>(true,"No data!","")) : ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<String>(false,"Error occurred!",""));
        }
    }

}
