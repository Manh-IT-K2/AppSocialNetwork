package com.example.Backend.Request.Story;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestStoryByUserId {
    private String userId;
    private String avtUser;
    private String userName;
    private String contentMedia;
    private String createdAt;

}
