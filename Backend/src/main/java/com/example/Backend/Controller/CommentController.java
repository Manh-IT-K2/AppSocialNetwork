package com.example.Backend.Controller;

import com.example.Backend.Entity.Comment;
import com.example.Backend.Request.Comment.AddComment;
import com.example.Backend.Request.Comment.DeleteComment;
import com.example.Backend.Request.Post.RequestPost;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Service.Comment.CommentService;
import com.example.Backend.Service.Post.PostService;
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

    @PostMapping("/addComment")
    public ResponseEntity<ApiResponse<Comment>> addComment(@RequestBody AddComment addComment) throws Exception{
        ApiResponse<Comment> apiResponse = new ApiResponse<Comment>(true, "",commentService.addComment(addComment));
        return new ResponseEntity<ApiResponse<Comment>>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/deleteComment")
    public ResponseEntity<ApiResponse<String>> deleteComment(@RequestBody DeleteComment deleteComment) throws Exception{
        commentService.deleteComment(deleteComment);
        ApiResponse<String> apiResponse = new ApiResponse<String>(true, "Đã xóa một bình luận","");
        return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/getListCommentByIdPost")
    public ResponseEntity<ApiResponse<List<Comment>>> getListCommentByIdPost(@RequestParam String id) throws Exception{
        ApiResponse<List<Comment>> apiResponse = new ApiResponse<List<Comment>>(true, "",commentService.getListCommentByIdPost(id));
        return new ResponseEntity<ApiResponse<List<Comment>>>(apiResponse, HttpStatus.OK);
    }
}
