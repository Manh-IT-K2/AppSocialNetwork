package com.example.Backend.Request.GroupChat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestRenameGroupChat {
    private String groupId;
    private String newGroupName;

}
