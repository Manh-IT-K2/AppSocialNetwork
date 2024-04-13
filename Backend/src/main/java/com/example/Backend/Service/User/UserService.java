package com.example.Backend.Service.User;

import com.example.Backend.Entity.GroupChat;
import com.example.Backend.Entity.Notification;
import com.example.Backend.Entity.model.NotificationOfUser;
import com.example.Backend.Entity.model.User;
import com.example.Backend.Request.GroupChat.RequestCreateGroupChat;
import com.example.Backend.Request.User.*;
import com.example.Backend.Response.ApiResponse.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void createAccount (RequestCreateAccount requestCreateAccount) throws Exception;
    ApiResponse<User> loginAccount (RequestLogin requestCreateLogin) throws Exception;
    ApiResponse<String> sendOtp(String email);
    ApiResponse<List<String>> getListUserName();
    ApiResponse<String> sendOTP_forgotpassword(String email);
    ApiResponse<List<User>> getAllUsers();
    ApiResponse<User> changePW(RequestForgetPass requestForgetPass) throws Exception;
    ApiResponse<User> changePassword(RequestChangePasword requestChangePasword) throws  Exception;
    ApiResponse<User> requestTrackingUser(RequestTracking requestTracking);
    ApiResponse<List<RequestGetAllUserByFollows>> getAllUserByFollows (String userId);
    ApiResponse<User> getDetailUserById(User user);
    ApiResponse<User> updateUser(RequestUpdateUser user);
    User findUserById(String id);

    List<User> findUsersByIds(List<String> ids);
    ApiResponse<GroupChat> createGroupChat(RequestCreateGroupChat requestCreateGroupChat);
    ApiResponse<List<GroupChat>> getGroupChatsByUserId(String userId);
    ApiResponse<GroupChat> addMemberToGroupChat(String groupId, String memberId);
    ApiResponse<GroupChat> removeMemberFromGroupChat(String groupId, String memberId);

    ApiResponse<List<User>> findUser_privatechat(String u);
    void addNotification(RequestNotification notification);
    void updateTokenFCM(RequestUpdateTokenFCM updateTokenFCM);
    List<NotificationOfUser> getNotificationById(String id);
    String getTokenFCM(String id);


}
