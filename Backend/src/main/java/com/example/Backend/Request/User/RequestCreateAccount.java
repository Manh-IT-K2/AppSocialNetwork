package com.example.Backend.Request.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateAccount {
    private String email;
    private String username;
    private String password;
    private String avatarImg;
}
