package com.example.frontend.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapter.ChatListAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.response.PrivateChat.PrivateChatResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.PusherClient;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Message.MainChatViewModel;
import com.example.frontend.viewModel.Message.MessageViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.pusher.client.Pusher;

import java.util.ArrayList;
import java.util.List;

public class MainChatActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_chat);
//    }
    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private MessageViewModel messageViewModel;
    private MainChatViewModel mainChatViewModel;
    private ImageButton img_back;
    private TextView username;

    private Pusher pusher;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");

        // Khởi tạo RecyclerView và Adapter cho cuộc trò chuyện riêng lẻ
        recyclerView = findViewById(R.id.user_recycler_view);
        adapter = new ChatListAdapter(new ArrayList<>(), new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo ViewModel cho cuộc trò chuyện riêng lẻ
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.getListChat(userId).observe(this, chatList -> {
            adapter.setChatList(chatList);
        });

        // Khởi tạo ViewModel cho cuộc trò chuyện nhóm
        mainChatViewModel = new ViewModelProvider(this).get(MainChatViewModel.class);
        mainChatViewModel.getListChat(userId).observe(this, groupChatList -> {
            adapter.setGroupChatList(groupChatList);
        });

        // Lấy thông tin người dùng và hiển thị tên người dùng
        username = findViewById(R.id.userchat);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getDetailUserById(userId).observe(this, new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response != null && response.getMessage().equals("Success") && response.getStatus()) {
                    UserResponse userResponse = response.getData();
                    username.setText(userResponse.getUsername());

                }
            }
        });


        img_back = findViewById(R.id.back_btn);


        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.getListChat(userId).observe(this, chatList -> {
            Log.d("ChatList", "Received chat list: " + chatList);
            pusher = PusherClient.init();
            pusher.connect();
            pusher.subscribe("Lastmessage")
                    .bind("update", (channelName, eventName, data) -> {
                        // Xử lý dữ liệu nhận được từ sự kiện
                        ObjectMapper objectMapper = new ObjectMapper();
                        Log.d("pushertest1", new Gson().toJson(data));
                        try {
                            PrivateChatResponse privateChatResponse = new Gson().fromJson(data, PrivateChatResponse.class);
                            // Sử dụng Handler để hiển thị Toast trên luồng giao diện chính
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setChatList(chatList);

                                }
                            });
                            Log.d("pushertest2", new Gson().toJson(privateChatResponse));
                        } catch (Exception e) {
                            Log.d("trycatch", new Gson().toJson(e));
                        }
                    });
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();
        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");

        messageViewModel.getListChat(userId).observe(this, chatList -> {
            adapter.setChatList(chatList);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");

        // Hủy đăng ký lắng nghe khi không cần thiết
        messageViewModel.getListChat(userId).removeObservers(this);
    }
}
