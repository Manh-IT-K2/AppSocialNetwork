package com.example.Backend.Service.Comment;

import com.example.Backend.Entity.Comment;
import com.example.Backend.Request.Comment.AddComment;
import com.example.Backend.Request.Comment.DeleteComment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    Comment addComment(AddComment addComment) throws Exception;
    void deleteComment(DeleteComment deleteComment) throws Exception;
    List<Comment> getListCommentByIdPost(String id) throws Exception;
}
