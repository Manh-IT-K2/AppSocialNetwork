package com.example.Backend.Controller;

import com.example.Backend.Entity.Comment;
import com.example.Backend.Request.Comment.RequestCreateComment;
import com.example.Backend.Request.Comment.RequestDeleteComment;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Service.Comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    // create comment
    @PostMapping("/createComment")
    public ResponseEntity<ApiResponse<Comment>> createComment(@RequestBody RequestCreateComment comment) throws Exception{
        ApiResponse<Comment> apiResponse = new ApiResponse<Comment>(true, "",commentService.createComment(comment));
        return new ResponseEntity<ApiResponse<Comment>>(apiResponse, HttpStatus.OK);
    }

    // delete comment
    @PostMapping("/deleteComment")
    public ResponseEntity<ApiResponse<String>> deleteComment(@RequestBody RequestDeleteComment deleteComment) throws Exception{
        commentService.deleteComment(deleteComment);
        ApiResponse<String> apiResponse = new ApiResponse<String>(true, "Đã xóa một bình luận","");
        return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
    }

    // get list comment by post
    @GetMapping("/getListCommentByIdPost")
    public ResponseEntity<ApiResponse<List<Comment>>> getListCommentByIdPost(@RequestParam String id) throws Exception{
        ApiResponse<List<Comment>> apiResponse = new ApiResponse<List<Comment>>(true, "",commentService.getListCommentByIdPost(id));
        return new ResponseEntity<ApiResponse<List<Comment>>>(apiResponse, HttpStatus.OK);
    }
}
