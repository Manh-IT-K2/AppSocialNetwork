package com.example.frontend.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Message.GroupChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatGroupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GroupChatAdapter adapter;
    private EditText inputMessage;
    private ImageButton btnSend;
    private GroupChatViewModel groupChatViewModel;
    private TextView groupNameTextView;
    private RelativeLayout groupAvatarImageView;
    private String groupId; // Thêm biến để lưu trữ id của nhóm chat
    private String currentUserId; // Thêm biến để lưu trữ ID của người dùng hiện tại

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
                Log.d("id_curentUser", currentUserId);
                String message = inputMessage.getText().toString();
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
