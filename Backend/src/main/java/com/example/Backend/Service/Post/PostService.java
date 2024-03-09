package com.example.Backend.Service.Post;

import com.example.Backend.Request.Post.RequestPost;
import com.example.Backend.Request.Post.RequestPostByUserId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {

     // create post
     void createPost(RequestPost requestPost, String userId) throws Exception;

     // select post by userId
     List<RequestPostByUserId> getListPostsByUserId(String userId);
}
