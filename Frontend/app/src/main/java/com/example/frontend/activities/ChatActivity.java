package com.example.frontend.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.adapter.ChatListAdapter;
import com.example.frontend.adapter.PrivateChatAdapter;
import com.example.frontend.fragments.HomeFragment;
import com.example.frontend.fragments.QRCodeFragment;
import com.example.frontend.request.PrivateChat.RequestPrivateChat;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Message.MessageWithSenderInfo;
import com.example.frontend.response.PrivateChat.PrivateChatResponse;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.PusherClient;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Message.MessageViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pusher.client.Pusher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private TextView username_recipient;
    private ImageButton btn_back_main_chat;

    private CircleImageView imgAvatar;

    private ImageButton btnSend;

    private Pusher pusher;

    private RecyclerView recyclerView;

    private PrivateChatAdapter adapter;
    private MessageViewModel messageViewModel;

    private EditText inputMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String username_re = getIntent().getStringExtra("recipientUserId");
        String avatar = getIntent().getStringExtra("recipientAvater");
        String ID_re = getIntent().getStringExtra("recipientID");
        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");

        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        username_recipient = findViewById(R.id.other_username);
        btn_back_main_chat = findViewById(R.id.back_btn);
        imgAvatar = findViewById(R.id.imgAvatar);


        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Thêm LayoutManager vào RecyclerView
        adapter = new PrivateChatAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);


        btnSend = findViewById(R.id.message_send_btn);
        username_recipient.setText(username_re);
        inputMessage = findViewById(R.id.chat_message_input);


        //call PUSHER


        //
        if (avatar != null) {
            Glide.with(getApplicationContext())
                    .load(Uri.parse(avatar))
                    .into(imgAvatar);
        }
        btn_back_main_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });


//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message = inputMessage.getText().toString();
//                RequestPrivateChat requestPrivateChat = new RequestPrivateChat(userId, ID_re, message);
//                messageViewModel.sendMessage(requestPrivateChat).observe(ChatActivity.this, new Observer<ApiResponse<PrivateChatWithMessagesResponse>>() {
//                    @Override
//                    public void onChanged(ApiResponse<PrivateChatWithMessagesResponse> response) {
//                        if (response != null && response.isSuccess()) {
//                            pusher = PusherClient.init();
//                pusher.connect();
//                pusher.subscribe("newmess")
//                        .bind("send", (channelName, eventName, data) -> {
//                            try {
//                                Gson gson = new GsonBuilder()
//                                        .setDateFormat("MMM dd, yyyy, hh:mm:ss a")
//                                        .create();
//                                // Chuyển đổi dữ liệu từ JSONObject sang chuỗi JSON
//                                String jsonData = data.toString();
//                                // Tạo một đối tượng PrivateChatWithMessagesResponse từ chuỗi JSON
//                                PrivateChatWithMessagesResponse privateChatResponse = gson.fromJson(jsonData, PrivateChatWithMessagesResponse.class);
//
//                                // Lấy tin nhắn cuối cùng từ danh sách tin nhắn
//                                List<MessageWithSenderInfo> messages = privateChatResponse.getMessages();
//                                if (messages != null && !messages.isEmpty()) {
//                                    MessageWithSenderInfo lastMessage = messages.get(messages.size() - 1);
//                                    // Hiển thị tin nhắn cuối cùng trong giao diện người dùng
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(getApplicationContext(), lastMessage.getContent(), Toast.LENGTH_LONG).show();
//                                            inputMessage.setText(null);
//                                        }
//                                    });
//                                }
//                            } catch (Exception e) {
//                                Log.d("trycatch", new Gson().toJson(e));
//                            }
//                        });
//                        } else {
//                            // Xử lý khi gửi tin nhắn không thành công
//                            String errorMessage = "Request failed";
//                            if (response != null && response.getMessage() != null) {
//                                errorMessage += ": " + response.getMessage();
//                            }
//                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//
//                    }
//                });
//            }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputMessage.getText().toString();
                RequestPrivateChat requestPrivateChat = new RequestPrivateChat(userId, ID_re, message);
                messageViewModel.sendMessage(requestPrivateChat).observe(ChatActivity.this, new Observer<ApiResponse<PrivateChatWithMessagesResponse>>() {
                    @Override
                    public void onChanged(ApiResponse<PrivateChatWithMessagesResponse> response) {
                        if (response != null && response.isSuccess()) {
                            // Xử lý dữ liệu trên Pusher và nhận dữ liệu mới
                            pusher = PusherClient.init();
                            pusher.connect();
                            pusher.subscribe("newmess")
                                    .bind("send", (channelName, eventName, data) -> {
                                        try {
                                            Gson gson = new GsonBuilder()
                                                    .setDateFormat("MMM dd, yyyy, hh:mm:ss a")
                                                    .create();
                                            String jsonData = data.toString();
                                            PrivateChatWithMessagesResponse privateChatResponse = gson.fromJson(jsonData, PrivateChatWithMessagesResponse.class);
                                            List<MessageWithSenderInfo> messages = privateChatResponse.getMessages();
                                            if (messages != null && !messages.isEmpty()) {
                                                // Cập nhật danh sách tin nhắn và thông báo Adapter
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        adapter.setListMessage(messages);
                                                        inputMessage.setText(null);
                                                        // Sửa đổi tên phương thức thành setListMessage
                                                    }
                                                });
                                            }
                                        } catch (Exception e) {
                                            Log.d("trycatch", new Gson().toJson(e));
                                        }
                                    });
                        } else {
                            // Xử lý khi gửi tin nhắn không thành công
                            String errorMessage = "Request failed";
                            if (response != null && response.getMessage() != null) {
                                errorMessage += ": " + response.getMessage();
                            }
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                // Xử lý phản hồi khi có tin nhắn mới được gửi thành công
            }
        }); // Thêm dấu đóng ngoặc để đóng khối onClick
    }
}