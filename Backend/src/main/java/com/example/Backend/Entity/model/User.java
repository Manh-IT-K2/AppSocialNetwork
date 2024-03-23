package com.example.Backend.Entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    private boolean fromGoogle;

    private String email;

    private String phoneNumber;

    private String avatarImg;

    private String bio;

    private String website;

    private String gender;

    private String name;
    private boolean status;

    @Min(0)
    private int followers;

    @Min(0)
    private int following;
}