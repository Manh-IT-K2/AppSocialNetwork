package com.example.Backend.Entity.model.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetQuantityFollows {
    private String id;
    private int quantityFollower;
    private int quantityFollowing;
}
