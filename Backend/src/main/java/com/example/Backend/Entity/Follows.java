package com.example.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "follows")
public class Follows {
    @Id
    private String idFollows;

    private String idFollower;

    private String idFollowing;

    private String created_at;

    private String update_at;

    private String delete_at;
}
