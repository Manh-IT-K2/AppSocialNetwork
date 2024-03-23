package com.example.Backend.Request.Follows;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateFollows {
    private ObjectId idFollower;
    private ObjectId idFollowing;
    private Date created_at;
}
