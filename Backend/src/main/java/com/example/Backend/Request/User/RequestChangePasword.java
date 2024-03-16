package com.example.Backend.Request.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestChangePasword {
    private String email;
    private  String currentpass;
    private String newpass;
}
