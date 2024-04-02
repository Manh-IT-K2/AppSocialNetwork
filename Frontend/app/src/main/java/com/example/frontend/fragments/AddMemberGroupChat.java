package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontend.R;
import com.example.frontend.adapter.AddMemberAdapter;
import com.example.frontend.repository.UserRepository;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;

import java.util.ArrayList;
import java.util.List;


public class AddMemberGroupChat extends Fragment {

    private RecyclerView recyclerView;
    private AddMemberAdapter adapter;
    private List<UserResponse> userList = new ArrayList<>();
    private UserRepository userRepository;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_member_group_chat, container, false);

        userRepository = new UserRepository();

//        recyclerView = root.findViewById(R.id.recycler_view_add_member);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AddMemberAdapter(getActivity(), userList);
        recyclerView.setAdapter(adapter);

        loadAllUsers();

        return root;
    }

    private void loadAllUsers() {
        userRepository.getAllUsers().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
            @Override
            public void onChanged(ApiResponse<List<UserResponse>> response) {
                if (response != null && response.isSuccess()) {
                    userList.clear();
                    userList.addAll(response.getData());
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle error
                }
            }
        });
    }

    public void addMemberToGroup(UserResponse user) {
        // Add user to the group and update UI
    }
}
