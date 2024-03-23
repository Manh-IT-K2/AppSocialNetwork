package com.example.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "follows")
public class Follows {
    @Id
    private String idFollows;

    private ObjectId idFollower;

    private ObjectId idFollowing;

    private Date created_at;

    private Date update_at;

    private Date delete_at;

    private int status;
}
