package com.example.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.frontend.R;
import com.example.frontend.adapter.AddMemberAdapter;
import com.example.frontend.adapter.FlowAdapter;
import com.example.frontend.request.GroupChat.RequestAddMemberToGroupChat;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatResponse;
import com.example.frontend.response.User.GetAllUserByFollowsResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Message.GroupChatViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.example.frontend.repository.GroupChatRepository;
import com.example.frontend.request.GroupChat.RequestCreateGroupChat;

import java.util.ArrayList;
import java.util.List;

public class AddMemberGroupChat extends AppCompatActivity {

    private TextView GroupName;
    private Button btnAddMember;
    private ImageButton btnBack;
    private ListView listView;
    private UserViewModel userViewModel;
    private AddMemberAdapter adapter;
    private String currentUserId;
    private List<GetAllUserByFollowsResponse> selectedUsers = new ArrayList<>();
    private GroupChatViewModel groupChatViewModel;
    private String GroupID;
    private String groupName;
    private ArrayList<String> memberIdList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_group_chat);

        // Lấy id user hiện hành
        currentUserId = SharedPreferenceLocal.read(getApplicationContext(), "userId");

        // Nhận groupChatId và groupChatName từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            GroupID = extras.getString("groupChatId");
            groupName = extras.getString("groupChatName");
            // Lấy danh sách thành viên từ Intent và hiển thị lên ListView
            memberIdList = getIntent().getStringArrayListExtra("memberIdList");
            Log.d("dl nhan", GroupID+"  "+memberIdList+" "+groupName +" "+ currentUserId);
        }


        // Ánh xạ các thành phần UI từ layout
        GroupName = findViewById(R.id.txtGroupName);
        btnAddMember = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.listView);
        btnBack = findViewById(R.id.btnBack);
        // Hiển thị groupName trên TextView edtGroupName
        if (groupName != null) {
            GroupName.setText(groupName);
        }
        // Khởi tạo UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        groupChatViewModel = new ViewModelProvider(this).get(GroupChatViewModel.class);

        // Khởi tạo adapter cho listViewFriends
        adapter = new AddMemberAdapter(this, new ArrayList<>(), new AddMemberAdapter.OnItemCheckedListener() {
            @Override
            public void onItemChecked(GetAllUserByFollowsResponse user, boolean isChecked) {
                if (isChecked) {
                    selectedUsers.add(user);
                } else {
                    selectedUsers.remove(user);
                }
                updateCreateGroupButtonState();
            }
        });
        listView.setAdapter(adapter);

        // Thiết lập sự kiện click cho nút tạo nhóm
        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý logic tạo nhóm chat ở đây
                if (selectedUsers.size() >= 1) {
                    AddmemberFroupChat(GroupID);
                    // Quay lại MainActivityChat mới
                    Intent intent = new Intent(AddMemberGroupChat.this, ChatGroupActivity.class);

                    intent.putExtra("groupChatId", GroupID);
                    intent.putExtra("groupChatName", groupName);
                    startActivity(intent);
                    finish(); // Đóng Activity hiện tại nếu bạn muốn
                } else {
                    Toast.makeText(AddMemberGroupChat.this, "Bạn cần chọn ít nhất 3 thành viên để tạo nhóm chat", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết thúc hoạt động và quay lại MainChatActivity
                finish();
            }
        });
        // Gọi hàm getAllUsersByFollows để lấy danh sách người dùng theo follow
        getAllUsersByFollows();


    }

    // Phương thức để lấy danh sách người dùng theo follow
    private void getAllUsersByFollows() {
        // Gọi phương thức trong UserViewModel để lấy danh sách người dùng theo follow
        userViewModel.getAllUsersByFollows(currentUserId).observe(this, new Observer<ApiResponse<List<GetAllUserByFollowsResponse>>>() {
            @Override
            public void onChanged(ApiResponse<List<GetAllUserByFollowsResponse>> response) {
                if (response != null && response.getStatus()) {
                    // Nếu phản hồi thành công, cập nhật danh sách người dùng trong adapter
                    List<GetAllUserByFollowsResponse> userList = response.getData();
                    Log.d("adddd", userList.get(0).getUsername());
                    // Loại bỏ các người dùng có id trong memberIdList khỏi userList
                    List<GetAllUserByFollowsResponse> filteredList = new ArrayList<>();
                    for (GetAllUserByFollowsResponse user : userList) {
                        if (!memberIdList.contains(user.getId())) {
                            filteredList.add(user);
                        }
                    }
                    adapter.clear();
                    adapter.addAll(filteredList);
                } else {
                    // Nếu có lỗi, hiển thị thông báo lỗi
                    Toast.makeText(AddMemberGroupChat.this, "Bạn không flow người dùng nào cả", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Cập nhật trạng thái của nút tạo nhóm dựa trên số lượng thành viên đã chọn
    private void updateCreateGroupButtonState() {
        btnAddMember.setEnabled(selectedUsers.size() >= 1);
    }

    private void AddmemberFroupChat(String GroupId){
       // Tạo một đối tượng RequestAddMemberToGroupChat với thông tin cần thiết
        RequestAddMemberToGroupChat request = new RequestAddMemberToGroupChat();
        request.setGroupId(GroupId); //id nhóm chat
        request.setMemberIds(getSelectedUserIds());
        // Gọi hàm addMemberToGroupChat từ viewModelGroupChat để thêm thành viên
        groupChatViewModel.addMemberToGroupChat(GroupID, request).observe(this, new Observer<ApiResponse<String>>() {
            @Override
            public void onChanged(ApiResponse<String> response) {
                if (response != null && response.getStatus()) {
                    Toast.makeText(AddMemberGroupChat.this, "Đã thêm thành viên vào nhóm", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddMemberGroupChat.this, "Thất bại khi thêm thành viên", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<String> getSelectedUserIds() {
        List<String> selectedIds = new ArrayList<>();
        for (GetAllUserByFollowsResponse user : selectedUsers) {
            if (user.isSelected()) {
                selectedIds.add(user.getId());
            }
        }
        return selectedIds;
    }
}
