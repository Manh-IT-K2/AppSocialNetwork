package com.example.Backend.Request.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPost {

    private List<String> imagePost;
    private ObjectId userId;
    private String description;
    private String location;
    private Date createAt;
}
