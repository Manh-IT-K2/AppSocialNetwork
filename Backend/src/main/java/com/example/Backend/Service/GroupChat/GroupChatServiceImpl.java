package com.example.Backend.Service.GroupChat;

import com.example.Backend.Entity.Message;
import com.example.Backend.Entity.GroupChat;
import com.example.Backend.Entity.model.Message.MessageWithSenderInfo;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.GroupChat.*;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatResponse;
import com.example.Backend.Response.ApiResponse.GroupChatResponse.GroupChatWithMessagesResponse;
import com.example.Backend.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GroupChatServiceImpl implements GroupChatService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    UserService userService;

    @Override
    public GroupChatResponse createGroupChat(RequestCreateGroupChat requestCreateGroupChat) {
        GroupChat newGroupChat = new GroupChat();
        newGroupChat.setGroupName(requestCreateGroupChat.getGroupName());
        newGroupChat.setMemberIds(requestCreateGroupChat.getMemberIds());
        mongoTemplate.save(newGroupChat);

        GroupChatResponse groupChatResponse = new GroupChatResponse();
        groupChatResponse.setId(newGroupChat.getId());
        groupChatResponse.setGroupName(newGroupChat.getGroupName());
        groupChatResponse.setMembers(userService.findUsersByIds(newGroupChat.getMemberIds()));
        return groupChatResponse;
    }

    @Override
    public GroupChatWithMessagesResponse getMessagesByGroupChatId(String id) throws Exception {
        GroupChat groupChat = mongoTemplate.findById(id, GroupChat.class);
        List<MessageWithSenderInfo> messages = getMessageList(groupChat);
        GroupChatWithMessagesResponse response = new GroupChatWithMessagesResponse();
        response.setId(groupChat.getId());
        response.setGroupName(groupChat.getGroupName());
        response.setMembers(userService.findUsersByIds(groupChat.getMemberIds()));
        response.setMessages(messages);
        return response;
    }

    @Override
    public GroupChatWithMessagesResponse sendMessage(RequestChatGroup requestChatGroup) throws Exception {
        GroupChat groupChat = mongoTemplate.findById(requestChatGroup.getGroupId(), GroupChat.class);
        Message newMessage = new Message();
        newMessage.setSenderId(requestChatGroup.getSenderId());
        newMessage.setContent(requestChatGroup.getContent());
        newMessage.setCreatedAt(new Date());
        newMessage.setGroupChatId(groupChat.getId());
        mongoTemplate.save(newMessage);
        List<MessageWithSenderInfo> messages = getMessageList(groupChat);
        GroupChatWithMessagesResponse response = new GroupChatWithMessagesResponse();
        response.setId(groupChat.getId());
        response.setGroupName(groupChat.getGroupName());
        response.setMembers(userService.findUsersByIds(groupChat.getMemberIds()));
        response.setMessages(messages);
        return response;
    }

    private List<MessageWithSenderInfo> getMessageList(GroupChat groupChat) {
        List<Message> messages = mongoTemplate.find(Query.query(Criteria.where("groupChatId").is(groupChat.getId())), Message.class);
        List<MessageWithSenderInfo> messageWithSenderInfos = new ArrayList<>();
        for (Message message : messages) {
            // Create MessageWithSenderInfo object and set message information along with sender information
            MessageWithSenderInfo messageWithSenderInfo = new MessageWithSenderInfo();
            messageWithSenderInfo.setContent(message.getContent());
            messageWithSenderInfo.setId(message.getId());
            messageWithSenderInfo.setGroupChatId(message.getGroupChatId());
            messageWithSenderInfo.setCreatedAt(message.getCreatedAt());
            messageWithSenderInfo.setSender(userService.findUserById(message.getSenderId()));
            messageWithSenderInfos.add(messageWithSenderInfo);
        }
        return messageWithSenderInfos;
    }
    @Override
    public ApiResponse<String> addMemberToGroupChat(RequestAddMemberToGroupChat request) {
        GroupChat groupChat = mongoTemplate.findById(request.getGroupId(), GroupChat.class);
        if (groupChat == null) {
            return new ApiResponse<>(false, "Group chat not found", null);
        }

        List<String> memberIds = groupChat.getMemberIds();
        memberIds.addAll(request.getMemberIds());
        groupChat.setMemberIds(memberIds);
        mongoTemplate.save(groupChat);
        return new ApiResponse<>(true, "Members added successfully", null);
    }

    @Override
    public ApiResponse<String> removeMemberFromGroupChat(RequestRemoveMemberFromGroupChat request) {
        GroupChat groupChat = mongoTemplate.findById(request.getGroupId(), GroupChat.class);
        if (groupChat == null) {
            return new ApiResponse<>(false, "Group chat not found", null);
        }

        List<String> memberIds = groupChat.getMemberIds();
        memberIds.removeAll(request.getMemberIds());
        groupChat.setMemberIds(memberIds);
        mongoTemplate.save(groupChat);
        return new ApiResponse<>(true, "Members removed successfully", null);
    }

    @Override
    public ApiResponse<String> renameGroupChat(RequestRenameGroupChat request) {
        GroupChat groupChat = mongoTemplate.findById(request.getGroupId(), GroupChat.class);
        if (groupChat == null) {
            return new ApiResponse<>(false, "Group chat not found", null);
        }

        groupChat.setGroupName(request.getNewGroupName());
        mongoTemplate.save(groupChat);
        return new ApiResponse<>(true, "Group chat renamed successfully", null);
    }

}
