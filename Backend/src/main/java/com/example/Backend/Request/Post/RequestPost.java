package com.example.Backend.Request.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPost {

    private String imagePost;
    private String userId;
    private String description;
    private String location;
    private Date createAt;
}
