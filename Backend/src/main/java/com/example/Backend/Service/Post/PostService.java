package com.example.Backend.Service.Post;

import com.example.Backend.Entity.Post;
import com.example.Backend.Request.Post.RequestCreatePost;
import com.example.Backend.Request.Post.RequestPostByUserId;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {

     // create post
     void createPost(RequestCreatePost requestPost, String userId) throws Exception;

     // select post by userId
     ApiResponse<List<RequestPostByUserId>> getListPostsByUserId(String userId);

     // add user like post
     ApiResponse<Post> addLikeToPost(String postId, String userId);

     // get posts by search query
     ApiResponse<List<RequestPostByUserId>> getListPostsBySearchQuery(String id, String searchQuery);
}
