package com.example.frontend.viewModel.Comment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.frontend.repository.CommentRepository;
import com.example.frontend.request.Comment.RequestCreateComment;
import com.example.frontend.request.Comment.RequestDeleteComment;
import com.example.frontend.request.Comment.RequestLikeComment;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Comment.CommentResponse;

import java.util.List;

public class CommentViewModel extends ViewModel {

    //
    private CommentRepository commentRepository;

    //
    public CommentViewModel(){
        commentRepository = new CommentRepository();
    }

    // create comment
    public MutableLiveData createComment(RequestCreateComment createComment){
        return commentRepository.createComment(createComment);
    }

    // delete comment
    public MutableLiveData deleteComment(RequestDeleteComment deleteComment){
        return commentRepository.deleteComment(deleteComment);
    }

    // get list comment by id post
    public MutableLiveData<ApiResponse<List<CommentResponse>>> getListCommentByIdPost(String id, String idComment){
        return commentRepository.getListCommentByIdPost(id, idComment);
    }

    // get list comment by id post
    public MutableLiveData<ApiResponse<List<CommentResponse>>> likeComment(RequestLikeComment likeComment){
        return commentRepository.likeComment(likeComment);
    }
}
