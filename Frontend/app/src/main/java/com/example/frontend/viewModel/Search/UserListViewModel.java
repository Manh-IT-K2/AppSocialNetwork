package com.example.frontend.viewModel.Search;

import com.example.frontend.response.User.UserResponse;

import java.util.List;

public class UserListViewModel {

    private List<UserResponse> userList;

    public void setUserList(List<UserResponse> list) {
        userList = list;
    }

    public List<UserResponse> getUserList() {
        return userList;
    }
}
