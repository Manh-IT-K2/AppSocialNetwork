package com.example.Backend.Controller;

import com.example.Backend.Entity.Post;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.Post.RequestCreatePost;
import com.example.Backend.Request.Post.RequestPostByUserId;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Response.ApiResponse.Post.ResponseCreatePost;
import com.example.Backend.Response.ApiResponse.Post.ResponsePostById;
import com.example.Backend.Service.Follows.FollowsService;
import com.example.Backend.Service.Post.PostService;
import com.google.gson.Gson;
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
    @Autowired
    FollowsService followsService;

    // createPost
    @PostMapping("/createPost")
    public ResponseEntity<ApiResponse<ResponseCreatePost>> createPost(@RequestBody RequestCreatePost requestPost, @RequestParam String userId) throws Exception{
        String idPost = postService.createPost(requestPost,userId);
        List<User> listFollowers = followsService.getUserFollowerById(userId).getData();

        ResponseCreatePost responseCreatePost = new ResponseCreatePost(idPost, listFollowers);
        System.out.println("idPost "+new Gson().toJson(idPost));

        ApiResponse<ResponseCreatePost> apiResponse = new ApiResponse<ResponseCreatePost>(true, "Đăng bài thành công !",responseCreatePost);
        return new ResponseEntity<ApiResponse<ResponseCreatePost>>(apiResponse, HttpStatus.OK);
    }

    // select post
    @GetMapping("/getListPostByUserId")
    public ResponseEntity<ApiResponse<List<RequestPostByUserId>>> getListPostByUserId(@RequestParam String userId) {
        if (userId.isEmpty()) userId = "";
        System.out.println(userId);
        ApiResponse<List<RequestPostByUserId>> result = postService.getListPostsByUserId(userId);
        return new ResponseEntity<ApiResponse<List<RequestPostByUserId>>>(result, HttpStatus.OK);
    }

    // select post
    @GetMapping("/getListPostsProfile")
    public ResponseEntity<ApiResponse<List<RequestPostByUserId>>> getListPostsProfile(@RequestParam String userId) {
        if (userId.isEmpty()) userId = "";
        ApiResponse<List<RequestPostByUserId>> result = postService.getListPostsProfile(userId);
        return new ResponseEntity<ApiResponse<List<RequestPostByUserId>>>(result, HttpStatus.OK);
    }

    // add user like post
    @PostMapping("/addLike")
    public ResponseEntity<ApiResponse<Post>> addLikeToPost(@RequestParam String postId, @RequestParam String userId){
        if (!postId.isEmpty() && !userId.isEmpty()) {
            ApiResponse<Post> apiResponse =  postService.addLikeToPost(postId, userId);
            return new ResponseEntity<ApiResponse<Post>>(apiResponse, HttpStatus.OK);
        }
        return null;
    }

    // get posts by search query
    @GetMapping("/getListPostsBySearchQuery")
    public ResponseEntity<ApiResponse<List<RequestPostByUserId>>> getListPostsBySearchQuery(@RequestParam String id, @RequestParam String searchQuery) {
        if (!id.isEmpty() && !searchQuery.isEmpty()) {
            ApiResponse<List<RequestPostByUserId>> apiResponse =  postService.getListPostsBySearchQuery(id, searchQuery);
            return new ResponseEntity<ApiResponse<List<RequestPostByUserId>>>(apiResponse, HttpStatus.OK);
        }
        return null;
    }

    @GetMapping("/getPostById")
    public ResponseEntity<ApiResponse<ResponsePostById>> getPostById(@RequestParam String id) {
        ApiResponse<ResponsePostById> result = postService.getPostById(id);
        return new ResponseEntity<ApiResponse<ResponsePostById>>(result, HttpStatus.OK);
    }

    @GetMapping("/getPostUserLiked")
    public ResponseEntity<ApiResponse<List<RequestPostByUserId>>> getListPostUserLiked(@RequestParam String id) {
        if (id.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(), HttpStatus.NOT_FOUND);
        }

        ApiResponse<List<RequestPostByUserId>> result = postService.getListPostUserLiked(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
