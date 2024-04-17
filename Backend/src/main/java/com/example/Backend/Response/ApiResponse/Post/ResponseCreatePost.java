package com.example.Backend.Response.ApiResponse.Post;

import com.example.Backend.Entity.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ResponseCreatePost {
    String postId;
    List<User> listFollowers;
}
