package com.example.frontend.viewModel.Post;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.PostRepository;
import com.example.frontend.repository.UserRepository;
import com.example.frontend.request.Post.RequestCreatePost;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;

import java.util.List;

public class PostViewModel extends ViewModel {
    private PostRepository postRepository;

    public PostViewModel(){
        postRepository = new PostRepository();
    }

    //create post
    public void createPost(RequestCreatePost createPost, String userId){
        postRepository.createPost(createPost,userId);
    }

    // get list post by userId
    public MutableLiveData<ApiResponse<List<RequestPostByUserId>>> getListPostByUserId(String userId){
        return postRepository.getListPostByUserId(userId);
    }
}
