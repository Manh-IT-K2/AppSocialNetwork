package com.example.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "story")
public class Story {

    @Id
    private String idStory;
    private String userId;
    private String contentMedia;
    private String createdAt;

}
