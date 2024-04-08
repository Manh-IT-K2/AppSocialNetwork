package com.example.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.frontend.R;
import com.example.frontend.adapter.FlowAdapter;
import com.example.frontend.adapter.RemoveMemberAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatResponse;
import com.example.frontend.response.User.GetAllUserByFollowsResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Message.GroupChatViewModel;
import com.example.frontend.request.GroupChat.RequestRemoveMemberFromGroupChat;
import com.example.frontend.viewModel.User.UserViewModel;
import com.pusher.client.channel.User;

import java.util.ArrayList;
import java.util.List;

public class RemoveMemberGroupChat extends AppCompatActivity {

    private ListView listViewMembers;
    private RemoveMemberAdapter adapter;
    private List<UserResponse> selectedUsers = new ArrayList<>();
    private GroupChatViewModel groupChatViewModel;
    private UserViewModel userViewModel;
    private String currentUserId;
    private Button btnRemove;
    private TextView txtGroupName;
    private ImageButton btnBack;
    private String GroupID;
    private String groupName;
    private ArrayList<String> memberIdList;
    private List<UserResponse> tam=new ArrayList<>();
    private List<String> memberIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_member_gruop_chat);

        currentUserId = SharedPreferenceLocal.read(getApplicationContext(), "userId");
        // Nhận groupChatId và groupChatName từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            GroupID = extras.getString("groupChatId");
            groupName = extras.getString("groupChatName");
            // Lấy danh sách thành viên từ Intent và hiển thị lên ListView
            memberIdList = getIntent().getStringArrayListExtra("memberIdList");
            Log.d("dl nhan", GroupID+"  "+memberIdList);
        }
        // Ánh xạ các thành phần UI từ layout
        txtGroupName = findViewById(R.id.edtGroupName);
        btnRemove = findViewById(R.id.btnCreateGroup);
        listViewMembers = findViewById(R.id.listViewFriends);
        btnBack = findViewById(R.id.btnBack);
        // Khởi tạo UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        groupChatViewModel = new ViewModelProvider(this).get(GroupChatViewModel.class);

        txtGroupName.setText(groupName);

        adapter = new RemoveMemberAdapter(this, new ArrayList<>(), new RemoveMemberAdapter.OnItemCheckedListener() {
            @Override
            public void onItemChecked(UserResponse user, boolean isChecked) {
                if (isChecked) {
                    selectedUsers.add(user);
                } else {
                    selectedUsers.remove(user);
                }
                updateCreateGroupButtonState();
            }
        });
        listViewMembers.setAdapter(adapter);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUsers.size() >= 1) {
                    // Kiểm tra số lượng thành viên hiện tại trong nhóm
                    if (memberIdList.size() <= 3) {
                        Toast.makeText(RemoveMemberGroupChat.this, "Nhóm phải có ít nhất 3 thành viên!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Xóa các thành viên được chọn
                        removeAllSelectedMembers();
                        // Quay lại MainActivityChat mới
                        Intent intent = new Intent(RemoveMemberGroupChat.this, MainChatActivity.class);
                        startActivity(intent);
                        finish(); // Đóng Activity hiện tại nếu bạn muốn
                    }
                } else {
                    Toast.makeText(RemoveMemberGroupChat.this, "Vui lòng chọn ít nhất một thành viên để xóa", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getAllGroupMembers();
    }

    private void getAllGroupMembers() {
        if (memberIdList != null) {
            for (String memberId : memberIdList) {
                final String currentMemberId = memberId;
                userViewModel.getDetailUserById(memberId).observe(this, new Observer<ApiResponse<UserResponse>>() {
                    @Override
                    public void onChanged(ApiResponse<UserResponse> response) {
                        if (response.getMessage().equals("Success") && response.getStatus() && memberIdList.contains(currentMemberId)) {
                            UserResponse userResponse = response.getData();
                            if (userResponse != null) {

                                tam.add(userResponse);
//                                // Nếu đã thêm tất cả các thành viên vào danh sách tam, cập nhật giao diện
//                                if (tam.size() == memberIdList.size()) {
//
//                                    adapter.setMembers(tam);
//                                }
                            } else {
                                Log.d("ttttt", "UserResponse is null for memberId: " + currentMemberId);
                            }
                        } else {
                            Log.d("ttttt", "Failed to get UserResponse for memberId: " + currentMemberId);
                        }
                    }
                });
            }
            adapter.setMembers(tam);
        } else {
            Log.d("ttttt", "MemberIdList is null");
        }
    }
    // Cập nhật trạng thái của nút remove
    private void updateCreateGroupButtonState() {
        btnRemove.setEnabled(selectedUsers.size() >= 1);
    }

    private void removeAllSelectedMembers() {

        for (UserResponse selectedUser : selectedUsers) {
            memberIds.add(selectedUser.getId());
        }
        RequestRemoveMemberFromGroupChat request = new RequestRemoveMemberFromGroupChat();
        request.setGroupId(GroupID);
        request.setMemberIds(memberIds);
        // Gọi hàm removeMemberFromGroupChat từ viewModelGroupChat để xóa thành viên
        groupChatViewModel.removeMemberFromGroupChat(GroupID, request).observe(this, new Observer<ApiResponse<String>>() {
            @Override
            public void onChanged(ApiResponse<String> response) {
                if (response != null && response.getStatus()) {
                    Toast.makeText(RemoveMemberGroupChat.this, "Đã xóa thành viên khỏi nhóm", Toast.LENGTH_SHORT).show();
                    // Xóa các phần tử khớp trong memberIdList với memberIds
                    memberIdList.removeAll(memberIds);
                    // Refresh danh sách thành viên sau khi xóa
                    getAllGroupMembers();
                } else {
                    Toast.makeText(RemoveMemberGroupChat.this, "Failed to remove member from group", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
