package com.example.Backend.Response.ApiResponse.Post;

import com.example.Backend.Entity.Post;
import com.example.Backend.Entity.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePostById {
    Post post;
    User user;
}
