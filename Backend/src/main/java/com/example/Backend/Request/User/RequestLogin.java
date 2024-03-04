package com.example.Backend.Request.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLogin {
    String email;
    String password;
    String username;
    String avatarImg;
    boolean fromGoogle;
}
