package com.example.Backend.Request.Post;

import com.example.Backend.Entity.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RequestPostByUserId {
    private String idPost;
    private String userId;
    private String userName;
    private String avtImage;
    private List<String> imagePost;
    private String description;
    private String location;
    private Date createAt;
    private List<User> like;
    private String tokenFCM;

}
