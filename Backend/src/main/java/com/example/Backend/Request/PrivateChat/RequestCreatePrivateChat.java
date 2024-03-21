package com.example.Backend.Request.PrivateChat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreatePrivateChat {
    String creatorId;
    String recipientId;
}
