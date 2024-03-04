package com.example.Backend.Entity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "fromGoogle")
    private boolean fromGoogle = false;

    @Column(name = "statusOnline")
    private boolean statusOnline;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "avatar_img")
    private String avatarImg;

    @Min(0)
    @Column(name = "followers")
    private int followers;

    @Min(0)
    @Column(name = "following")
    private int following;
}
