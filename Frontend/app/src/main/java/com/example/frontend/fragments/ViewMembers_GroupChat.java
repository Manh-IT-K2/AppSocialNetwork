package com.example.frontend.fragments;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.example.frontend.R;
import com.example.frontend.adapter.ViewMemberAdapter;
import com.example.frontend.repository.UserRepository;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import java.util.ArrayList;
import java.util.List;

public class ViewMembers_GroupChat extends Fragment {

    private UserRepository userRepository;
    private String groupId;
    private String groupName;
    private TextView edtGroupName;
    private ListView listViewFriends;
    private ImageButton btnBack;
    private ViewMemberAdapter memberListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();

        // Nhận groupChatId và groupChatName từ Intent
        if (getArguments() != null) {
            groupId = getArguments().getString("groupChatId");
            groupName = getArguments().getString("groupChatName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_members__group_chat, container, false);

        // Khởi tạo views từ layout
        edtGroupName = view.findViewById(R.id.edtGroupName);
        listViewFriends = view.findViewById(R.id.listViewFriends);

        // Hiển thị groupName trên TextView edtGroupName
        if (groupName != null) {
            edtGroupName.setText(groupName);
        }

        // Khởi tạo adapter và đặt cho ListView
        memberListAdapter = new ViewMemberAdapter(getContext(), new ArrayList<>());
        listViewFriends.setAdapter(memberListAdapter);

        // Lấy danh sách thành viên từ Intent và hiển thị lên ListView
        ArrayList<String> memberIdList = getArguments().getStringArrayList("memberList");
        Log.d("ds_id", memberIdList.get(0));
        if (memberIdList != null) {
            for (String memberId : memberIdList) {
                getUserDetails(memberId);
            }
        }
// Xử lý sự kiện khi nút back được nhấn
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tắt Fragment hiện tại
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    // Lấy thông tin chi tiết của người dùng từ UserRepository và cập nhật vào adapter
    private void getUserDetails(String userId) {
        MutableLiveData<ApiResponse<UserResponse>> userLiveData = userRepository.getDetailUserById(userId);
        userLiveData.observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> userResponseApiResponse) {
                if (userResponseApiResponse != null && userResponseApiResponse.getData() != null) {
                    UserResponse user = userResponseApiResponse.getData();
                    memberListAdapter.add(user);
                    Log.d("user_of_group", user.getName());
                } else {
                    // Xử lý khi không nhận được thông tin người dùng
                }
            }
        });
    }
}
