package com.example.Backend.Request.Story;

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
public class RequestStoryByUserId {
    private String idStory;
    private String userId;
    private String avtUser;
    private String userName;
    private String contentMedia;
    private Date createdAt;

}
