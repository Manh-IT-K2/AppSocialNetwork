package com.example.frontend.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.adapter.GroupChatAdapter;
import com.example.frontend.request.GroupChat.RequestChatGroup;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatResponse;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Message.GroupChatViewModel;
import com.example.frontend.viewModel.User.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatGroupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GroupChatAdapter adapter;
    private EditText inputMessage;
    private ImageButton btnSend,btn_Menu;
    private GroupChatViewModel groupChatViewModel;
    private TextView groupNameTextView;
    private RelativeLayout groupAvatarImageView;
    private String groupId; // Thêm biến để lưu trữ id của nhóm chat
    private String currentUserId; // Thêm biến để lưu trữ ID của người dùng hiện tại
    private String message;

    private GroupChatResponse Infor_GroupChat;
    private boolean isInforGroupChatLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);


        String currentUserId = SharedPreferenceLocal.read(getApplicationContext(), "userId");
        // Lấy id của nhóm chat từ Intent
        groupId = getIntent().getStringExtra("groupChatId");
        String groupName = getIntent().getStringExtra("groupChatName");
        Log.d("dulieu_nhan", groupId+"  "+groupName);
        // Thiết lập tên nhóm
        groupNameTextView = findViewById(R.id.other_username);
        groupNameTextView.setText(groupName);

        // Thiết lập hình ảnh đại diện của nhóm
        groupAvatarImageView = findViewById(R.id.profile_pic_layout);

        recyclerView = findViewById(R.id.chat_recycler_view);
        inputMessage = findViewById(R.id.chat_message_input);
        btnSend = findViewById(R.id.message_send_btn);

        groupChatViewModel = new ViewModelProvider(this).get(GroupChatViewModel.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new GroupChatAdapter(this, new ArrayList<>(), currentUserId);


        recyclerView.setAdapter(adapter);

        // Gửi tin nhắn khi nhấn nút gửi
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("chatgroup_content", groupId+currentUserId+message);
                message = inputMessage.getText().toString();
                RequestChatGroup request = new RequestChatGroup(groupId, currentUserId, message);

                groupChatViewModel.sendMessage(groupId, request).observe(ChatGroupActivity.this, new Observer<ApiResponse<GroupChatWithMessagesResponse>>() {
                    @Override
                    public void onChanged(ApiResponse<GroupChatWithMessagesResponse> response) {
                        if (response != null && response.isSuccess()) {
                            // Xử lý phản hồi khi gửi tin nhắn thành công
                            GroupChatWithMessagesResponse chatWithMessagesResponse = response.getData();

                            if (chatWithMessagesResponse != null) {
                                // Thêm tin nhắn mới vào RecyclerView
                                adapter.addMessage(chatWithMessagesResponse);
                                // Cuộn đến tin nhắn mới nhất
                                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                            }
                            // Xóa nội dung tin nhắn sau khi gửi
                            inputMessage.setText("");
                        } else {
                            // Xử lý khi gửi tin nhắn không thành công
                            String errorMessage = "Failed to send message";
                            if (response != null && response.getMessage() != null) {
                                errorMessage += ": " + response.getMessage();
                            }
                            Toast.makeText(ChatGroupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });


        // Lấy và hiển thị lịch sử tin nhắn khi hoạt động được tạo
        loadChatHistory();

        btn_Menu=findViewById(R.id.menu_btn);
        btn_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tải thông tin nhóm trước khi hiển thị menu
                loadGroupChatInfo();
            }
        });
    }
    private void loadGroupChatInfo() {
        groupChatViewModel.getGroupChatById(groupId).observe(ChatGroupActivity.this, new Observer<GroupChatResponse>() {
            @Override
            public void onChanged(GroupChatResponse response) {
                if (response != null) {
                    // Xử lý khi nhận được phản hồi thành công
                    Infor_GroupChat = response;
                    // Đảm bảo rằng dữ liệu đã được tải
                    isInforGroupChatLoaded = true;
                    // Hiển thị menu sau khi thông tin nhóm đã được tải
                    showPopupMenu(btn_Menu);
                } else {
                    // Xử lý khi nhận được thông báo lỗi
                    Toast.makeText(ChatGroupActivity.this, "Failed to load group chat info", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showPopupMenu(View v) {
        // Kiểm tra xem dữ liệu nhóm chat đã được tải chưa
        if (!isInforGroupChatLoaded) {
            // Nếu chưa, không hiển thị menu
            Toast.makeText(ChatGroupActivity.this, "Group chat info is loading. Please try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_group_chat, popupMenu.getMenu());

        // Kiểm tra xem currentUserId có phải là người tạo nhóm hay không
        if (currentUserId.equals(Infor_GroupChat.getCreator().getId())) {
            // Hiển thị tất cả các mục menu nếu là người tạo nhóm
        } else {
            // Ẩn mục menu "Xóa thành viên" và "Giải tán nhóm" nếu không phải là người tạo nhóm
            popupMenu.getMenu().findItem(R.id.menu_remove_member).setVisible(false);
            popupMenu.getMenu().findItem(R.id.menu_disband_group).setVisible(false);
        }

        // Thiết lập sự kiện cho mỗi mục menu
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menu_view_members:
//                        // Xử lý khi nhấn vào "Xem thành viên"
//                        return true;
//                    case R.id.menu_add_member:
//                        // Xử lý khi nhấn vào "Thêm thành viên"
//                        return true;
//                    case R.id.menu_remove_member:
//                        // Xử lý khi nhấn vào "Xóa thành viên"
//                        return true;
//                    case R.id.menu_disband_group:
//                        // Xử lý khi nhấn vào "Giải tán nhóm"
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });

        // Hiển thị menu popup
        popupMenu.show();
    }


    private void loadChatHistory() {
        groupChatViewModel.getMessagesByGroupChatId(groupId).observe(this, new Observer<ApiResponse<GroupChatWithMessagesResponse>>() {
            @Override
            public void onChanged(ApiResponse<GroupChatWithMessagesResponse> response) {
                if (response != null && response.isSuccess()) {
                    // Hiển thị lịch sử tin nhắn khi nhận được phản hồi thành công từ API
                    GroupChatWithMessagesResponse chatHistory = response.getData();
                    if (chatHistory != null) {
                        adapter.setMessages(chatHistory.getMessages());
                        // Cuộn đến tin nhắn mới nhất
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }
                } else {
                    // Xử lý khi không thể tải lịch sử tin nhắn
                    String errorMessage = "Failed to load chat history";
                    if (response != null && response.getMessage() != null) {
                        errorMessage += ": " + response.getMessage();
                    }
                    Toast.makeText(ChatGroupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
