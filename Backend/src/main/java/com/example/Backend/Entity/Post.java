package com.example.Backend.Entity;

import com.example.Backend.Entity.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "post")
public class Post {

    @Id
    private String id;

    private ObjectId userId;

    private List<String> imagePost;

    private String description;

    private String location;

    private Date createAt;

    private List<User> like;
}
