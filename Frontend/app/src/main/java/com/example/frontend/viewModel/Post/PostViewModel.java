package com.example.frontend.viewModel.Post;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.PostRepository;
import com.example.frontend.request.Post.RequestCreatePost;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Post.PostResponse;
import com.example.frontend.response.Post.ResponsePostById;

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
    // add user like post
    public MutableLiveData<ApiResponse<PostResponse>> addLike(String postId, String userId){
        return postRepository.addLike(postId,userId);
    }

    // get posts by search query
    public MutableLiveData<ApiResponse<List<RequestPostByUserId>>> getListPostsBySearchQuery(String id, String searchQuery) {
        return postRepository.getListPostsBySearchQuery(id, searchQuery);
    }

    public MutableLiveData<ApiResponse<ResponsePostById>> getPostById(String id){
        return postRepository.getPostById(id);
    }
    public MutableLiveData<ApiResponse<List<RequestPostByUserId>>> getListPostUserLiked(String id){
        return postRepository.getListPostUserLiked(id);
    }
}
