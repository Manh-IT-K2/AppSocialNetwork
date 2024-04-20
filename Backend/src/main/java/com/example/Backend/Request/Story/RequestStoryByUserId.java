package com.example.Backend.Request.Story;

import com.example.Backend.Entity.model.User;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestStoryByUserId {
    private String idStory;
    private String userId;
    private String avtUser;
    private String userName;
    private Date createdAt;
    private String image;
    private int status;
    private List<RequestCreateStory.ContentMedia> contentMedia;
    private List<RequestCreateStory.Stickers> stickers;
    private List<User> seen;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentMedia {
        private String content;
        private float x;
        private float y;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stickers {
        private String uriSticker;
        private float x;
        private float y;
    }
}
