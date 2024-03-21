package com.example.Backend.Request.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RequestPostByUserId {
    private String idPost;
    private String userId;
    private String userName;
    private String avtImage;
    private String imagePost;
    private String description;
    private String location;
    private Date createAt;

}
