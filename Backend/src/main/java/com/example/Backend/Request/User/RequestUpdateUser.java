package com.example.Backend.Request.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateUser {
    private String id;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatarImg;
    private String bio;
    private String website;
    private String gender;
    private String name;
}
