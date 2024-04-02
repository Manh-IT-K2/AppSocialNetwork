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
        newGroupChat.setCreatorId(requestCreateGroupChat.getCreatorId()); // Lưu thông tin về người tạo nhóm chat
        newGroupChat.setGroupName(requestCreateGroupChat.getGroupName());
        newGroupChat.setMemberIds(requestCreateGroupChat.getMemberIds());
        mongoTemplate.save(newGroupChat);

        GroupChatResponse groupChatResponse = new GroupChatResponse();
        groupChatResponse.setId(newGroupChat.getId());
        groupChatResponse.setCreatorId(newGroupChat.getCreatorId());
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
        String lastMessage=getLastMessageFromGroupChat(groupChat);
        response.setLastMessage(lastMessage);
        return response;
    }
    private String getLastMessageFromGroupChat(GroupChat groupChat) {
        // Lấy danh sách tin nhắn từ nhóm trò chuyện
        List<Message> messages = mongoTemplate.find(Query.query(Criteria.where("groupChatId").is(groupChat.getId())), Message.class);

        // Kiểm tra xem danh sách tin nhắn có rỗng không
        if (!messages.isEmpty()) {
            // Lấy tin nhắn cuối cùng từ danh sách
            Message lastMessage = messages.get(messages.size() - 1);

            // Tạo một đối tượng MessageWithSenderInfo cho tin nhắn cuối cùng
            MessageWithSenderInfo lastMessageInfo = new MessageWithSenderInfo();
            lastMessageInfo.setContent(lastMessage.getContent());
            lastMessageInfo.setId(lastMessage.getId());
            lastMessageInfo.setGroupChatId(lastMessage.getGroupChatId());
            lastMessageInfo.setCreatedAt(lastMessage.getCreatedAt());
            lastMessageInfo.setSender(userService.findUserById(lastMessage.getSenderId()));

            return lastMessageInfo.getContent();
        } else {
            // Trả về null nếu không có tin nhắn nào trong danh sách
            return null;
        }
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
    @Override
    public ApiResponse<String> deleteGroupChat(RequestDeleteGroupChat request) throws Exception {
        GroupChat groupChat = mongoTemplate.findById(request.getGroupId(), GroupChat.class);
        if (groupChat == null) {
            return new ApiResponse<>(false, "Group chat not found", null);
        }

        // Delete the group chat
        mongoTemplate.remove(groupChat);

        return new ApiResponse<>(true, "Group chat deleted successfully", null);
    }
    @Override
    public void updateLastMessage(String groupChatId, String lastMessage) {
        GroupChat groupChat = mongoTemplate.findById(groupChatId, GroupChat.class);
        groupChat.setLastMessage(lastMessage);
        mongoTemplate.save(groupChat);
    }

    public List<GroupChatWithMessagesResponse> getListChatGroup(String userId) {
        // Tìm tất cả các cuộc trò chuyện nhóm mà người dùng đã tham gia
        Query query = new Query();
        query.addCriteria(Criteria.where("memberIds").in(userId));
        List<GroupChat> groupChats = mongoTemplate.find(query, GroupChat.class);

        List<GroupChatWithMessagesResponse> groupChatsWithMessages = new ArrayList<>();

        // Lặp qua từng cuộc trò chuyện nhóm và lấy thông tin tin nhắn gần đây nhất
        for (GroupChat groupChat : groupChats) {
            GroupChatWithMessagesResponse groupChatWithMessagesResponse = new GroupChatWithMessagesResponse();
            groupChatWithMessagesResponse.setId(groupChat.getId());
            groupChatWithMessagesResponse.setGroupName(groupChat.getGroupName());
            groupChatWithMessagesResponse.setMembers(userService.findUsersByIds(groupChat.getMemberIds()));

            // Lấy tin nhắn gần đây nhất của cuộc trò chuyện nhóm
            String lastMessage = getLastMessageFromGroupChat(groupChat);
            groupChatWithMessagesResponse.setLastMessage(lastMessage);

            groupChatsWithMessages.add(groupChatWithMessagesResponse);
        }

        return groupChatsWithMessages;
    }
    @Override
    public GroupChatResponse getGroupChatById(String id) {
        GroupChat groupChat = mongoTemplate.findById(id, GroupChat.class);
        if (groupChat == null) {
            return null; // hoặc có thể trả về một giá trị mặc định hoặc báo lỗi tùy vào yêu cầu của bạn
        }

        GroupChatResponse groupChatResponse = new GroupChatResponse();
        groupChatResponse.setId(groupChat.getId());
        groupChatResponse.setCreatorId(groupChat.getCreatorId());
        groupChatResponse.setGroupName(groupChat.getGroupName());
        groupChatResponse.setMembers(userService.findUsersByIds(groupChat.getMemberIds()));
        return groupChatResponse;
    }

}
