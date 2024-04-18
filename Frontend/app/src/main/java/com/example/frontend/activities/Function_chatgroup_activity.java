package com.example.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.frontend.R;
import com.example.frontend.adapter.FlowAdapter;
import com.example.frontend.request.GroupChat.RequestChatGroup;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatResponse;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.response.Message.MessageWithSenderInfo;
import com.example.frontend.response.User.GetAllUserByFollowsResponse;
import com.example.frontend.utils.PusherClient;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.User.UserViewModel;
import com.example.frontend.repository.GroupChatRepository;
import com.example.frontend.request.GroupChat.RequestCreateGroupChat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pusher.client.Pusher;

import java.util.ArrayList;
import java.util.List;

public class Function_chatgroup_activity extends AppCompatActivity {
    private Pusher pusher;

    private EditText edtGroupName;
    private Button btnCreateGroup;
    private ListView listViewFriends;
    private UserViewModel userViewModel;
    private FlowAdapter adapter;
    private String currentUserId;
    private List<GetAllUserByFollowsResponse> selectedUsers = new ArrayList<>();
    private GroupChatRepository groupChatRepository;
    // Thêm biến boolean để kiểm soát việc cập nhật trạng thái của các CheckBox
    private boolean isEditTextFocused = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_chatgroup);

        // Lấy id user hiện hành
        currentUserId = SharedPreferenceLocal.read(getApplicationContext(), "userId");

        // Ánh xạ các thành phần UI từ layout
        edtGroupName = findViewById(R.id.edtGroupName);
        btnCreateGroup = findViewById(R.id.btnCreateGroup);
        listViewFriends = findViewById(R.id.listViewFriends);

        // Khởi tạo UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        groupChatRepository = new GroupChatRepository();

        // Khởi tạo adapter cho listViewFriends
        adapter = new FlowAdapter(this, new ArrayList<>(), new FlowAdapter.OnItemCheckedListener() {
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
        listViewFriends.setAdapter(adapter);

        // Thiết lập sự kiện click cho nút tạo nhóm
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý logic tạo nhóm chat ở đây
                if (selectedUsers.size() >= 3) {
                    String groupName = edtGroupName.getText().toString();
                    createGroupChat(groupName);
                    // Quay lại MainActivityChat mới
                    Intent intent = new Intent(Function_chatgroup_activity.this, MainChatActivity.class);
                    startActivity(intent);
                    finish(); // Đóng Activity hiện tại nếu bạn muốn


                } else {
                    Toast.makeText(Function_chatgroup_activity.this, "Bạn cần chọn ít nhất 3 thành viên để tạo nhóm chat", Toast.LENGTH_SHORT).show();
                }

            }

        });

        // Gọi hàm getAllUsersByFollows để lấy danh sách người dùng theo follow
        getAllUsersByFollows();

        // Ánh xạ và thiết lập sự kiện click cho nút back
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết thúc hoạt động và quay lại MainChatActivity
                finish();
            }
        });
        // Theo dõi sự kiện focus của EditText
        edtGroupName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Cập nhật trạng thái của biến isEditTextFocused
                isEditTextFocused  = hasFocus;
            }
        });
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
                    adapter.clear();
                    adapter.addAll(userList);
                } else {
                    // Nếu có lỗi, hiển thị thông báo lỗi
                    Toast.makeText(Function_chatgroup_activity.this, "Failed to fetch users.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Cập nhật trạng thái của nút tạo nhóm dựa trên số lượng thành viên đã chọn
    private void updateCreateGroupButtonState() {
        btnCreateGroup.setEnabled(selectedUsers.size() >= 3);
    }

    // Tạo nhóm chat mới
    private void createGroupChat(String groupName) {
        List<String> memberIds = new ArrayList<>();
        for (GetAllUserByFollowsResponse user : selectedUsers) {
            memberIds.add(user.getId());
        }

        // Tạo một đối tượng RequestCreateGroupChat với thông tin cần thiết
        RequestCreateGroupChat request = new RequestCreateGroupChat();
        request.setCreatorId(currentUserId); // currentUserId là id của người tạo nhóm
        request.setGroupName(groupName); // groupName là tên của nhóm muốn tạo
        request.setMemberIds(getSelectedUserIds()); // getSelectedUserIds() là phương thức để lấy danh sách id của các thành viên được chọn

        // Gọi hàm createGroupChat trong GroupChatRepository và truyền request vào
        groupChatRepository.createGroupChat(request).observe(this, new Observer<ApiResponse<GroupChatResponse>>() {
            @Override
            public void onChanged(ApiResponse<GroupChatResponse> response) {
                if (response != null && response.getStatus()) {
                    // Xử lý khi tạo nhóm chat thành công
                    sendMessMacDinh(response,groupName);
                } else {
                    // Xử lý khi gặp lỗi
                }
            }
        });
    }
    private List<String> getSelectedUserIds() {
        List<String> selectedIds = new ArrayList<>();
        boolean currentUserAdded = false; // Biến để kiểm tra xem currentUserId đã được thêm vào hay chưa
        for (GetAllUserByFollowsResponse user : selectedUsers) {
            if (user.isSelected()) {
                selectedIds.add(user.getId());
                if (!currentUserAdded) {
                    // Thêm currentUserId vào danh sách nếu chưa có trong selectedIds
                    selectedIds.add(currentUserId);
                    currentUserAdded = true; // Đánh dấu đã thêm currentUserId
                }
            }
        }
        return selectedIds;
    }
    // Thêm phương thức để truy cập biến isEditTextFocused từ bên ngoài
    public boolean isEditTextFocused() {
        return isEditTextFocused;
    }

    private void sendMessMacDinh(ApiResponse<GroupChatResponse> response, String groupName){
        // Tạo tin nhắn mặc định khi tạo nhóm chat thành công
        String message ="Chào mừng bạn đã tham gia nhóm chat: " + groupName;
        RequestChatGroup request = new RequestChatGroup(response.getData().getId(), currentUserId, message, "","");
        groupChatRepository.sendMessage(response.getData().getId(), request);
    }
}
