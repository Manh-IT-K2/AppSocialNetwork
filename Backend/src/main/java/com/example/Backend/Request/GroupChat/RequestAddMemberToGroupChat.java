package com.example.Backend.Request.GroupChat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAddMemberToGroupChat {
    private String groupId;
    private List<String> memberIds;

}
