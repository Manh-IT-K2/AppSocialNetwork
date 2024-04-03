package com.example.Backend.Service.Comment;

import com.example.Backend.Entity.Comment;
import com.example.Backend.Request.Comment.RequestCreateComment;
import com.example.Backend.Request.Comment.RequestDeleteComment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    // create comment
    Comment createComment(RequestCreateComment addComment) throws Exception;

    // delete comment
    void deleteComment(RequestDeleteComment deleteComment) throws Exception;

    // get list comment by post
    List<Comment> getListCommentByIdPost(String id) throws Exception;
}
