package com.example.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapter.ChatListAdapter;
import com.example.frontend.adapter.SeachPrivateChatAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
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

    private RecyclerView recyclerView,searchRecyclerView;
    private ChatListAdapter adapter;
    private SeachPrivateChatAdapter searchAdapter;
    private MessageViewModel messageViewModel;
    private MainChatViewModel mainChatViewModel;
    private ImageButton img_back;
    private TextView username;

    private Pusher pusher;
    private UserViewModel userViewModel;
    private ImageButton moreOptionsBtn;

    private EditText searchUser_txt;

    RelativeLayout layoutSearch,toolbar;

    Button cancelSearchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");


        recyclerView = findViewById(R.id.user_recycler_view);
        adapter = new ChatListAdapter(new ArrayList<>(), new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchAdapter = new SeachPrivateChatAdapter(new ArrayList<>(), this);
        searchRecyclerView = findViewById(R.id.search_recycler_view);
        searchRecyclerView.setAdapter(searchAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.getListChat(userId).observe(this, chatList -> {
            adapter.setChatList(chatList);
        });

        // Khởi tạo ViewModel cho cuộc trò chuyện nhóm
        mainChatViewModel = new ViewModelProvider(this).get(MainChatViewModel.class);
        mainChatViewModel.getListChat(userId).observe(this, groupChatList -> {
            adapter.setGroupChatList(groupChatList);
        });


        username = findViewById(R.id.userchat);
        searchUser_txt = findViewById(R.id.seach_username_input);
        toolbar = findViewById(R.id.toolbar);
        layoutSearch = findViewById(R.id.layout_search);
        cancelSearchBtn = findViewById(R.id.cancel_search_btn);
        cancelSearchBtn.setVisibility(View.GONE);
        //
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
        searchUser_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                layoutSearch.setVisibility(View.VISIBLE);
                cancelSearchBtn.setVisibility(View.VISIBLE);
                String keyword = searchUser_txt.getText().toString();
                userViewModel = new ViewModelProvider(MainChatActivity.this).get(UserViewModel.class);
                userViewModel.findUser_privatechat(keyword).observe(MainChatActivity.this, new Observer<ApiResponse<List<UserResponse>>>() {
                    @Override
                    public void onChanged(ApiResponse<List<UserResponse>> response) {
                        if (response != null) {
                            List<UserResponse> userResponse = null;
                            if (response.getStatus()) {
                                userResponse = response.getData();
                                searchRecyclerView.setVisibility(View.VISIBLE);
                                searchAdapter.setListUser(userResponse);
                            } else {
                                searchRecyclerView.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Có lỗi xảy ra. Vui lòng thử lại sau.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        cancelSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                layoutSearch.setVisibility(View.GONE);
                cancelSearchBtn.setVisibility(View.GONE);
                searchRecyclerView.setVisibility(View.GONE);
                searchUser_txt.setText(null);

            }
        });
        moreOptionsBtn = findViewById(R.id.more_options_btn);
        moreOptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Tạo Intent để chuyển sang Fragment_MoreFunctionForChat
                Intent intent = new Intent(MainChatActivity.this, Function_chatgroup_activity.class);
                startActivity(intent);
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
