package com.example.Backend.Entity;

import com.example.Backend.Request.Story.RequestCreateStory;
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
@Document(collection = "story")
public class Story {

    @Id
    private String id;
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
