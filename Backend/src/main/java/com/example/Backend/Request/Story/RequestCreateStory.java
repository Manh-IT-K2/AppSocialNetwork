package com.example.Backend.Request.Story;

import com.example.Backend.Entity.Story;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateStory {
    private ObjectId userId;
    private Date createdAt;
    private String image;
    private List<ContentMedia> contentMedia;
    private List<Stickers> stickers;

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
