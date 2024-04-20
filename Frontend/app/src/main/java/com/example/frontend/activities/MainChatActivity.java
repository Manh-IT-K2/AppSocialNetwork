package com.example.frontend.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapter.ChatListAdapter;
import com.example.frontend.adapter.SeachPrivateChatAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.response.Message.MessageWithSenderInfo;
import com.example.frontend.response.PrivateChat.PrivateChatResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.PusherClient;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Message.GroupChatViewModel;
import com.example.frontend.viewModel.Message.MainChatViewModel;
import com.example.frontend.viewModel.Message.MessageViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.pusher.client.Pusher;

import java.lang.reflect.Type;
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

    private Pusher pusher,pushergroup;
    private UserViewModel userViewModel;
    private ImageButton moreOptionsBtn;

    private EditText searchUser_txt;

    RelativeLayout layoutSearch,toolbar;

    Button cancelSearchBtn;
    private List<GroupChatWithMessagesResponse> groupChatList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Xử lý tin nhắn mới nhận được
                String newMessageData = intent.getStringExtra("new_message");
                // Cập nhật giao diện với tin nhắn mới
                updateUIWithNewMessage(newMessageData);
            }
        };

        IntentFilter filter = new IntentFilter("NEW_MESSAGE_ACTION");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

        recyclerView = findViewById(R.id.user_recycler_view);
        adapter = new ChatListAdapter(new ArrayList<>(), groupChatList, this);
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
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                //finish();
            }
        });
        searchUser_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị layout tìm kiếm và các yếu tố liên quan
                toolbar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                layoutSearch.setVisibility(View.VISIBLE);
                cancelSearchBtn.setVisibility(View.VISIBLE);
            }
        });

// Sử dụng TextWatcher để lắng nghe sự kiện thay đổi văn bản trong searchUser_txt
        searchUser_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện hành động trước khi văn bản thay đổi
                toolbar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                layoutSearch.setVisibility(View.VISIBLE);
                cancelSearchBtn.setVisibility(View.VISIBLE);
                layoutSearch.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không cần thực hiện hành động khi văn bản đang thay đổi
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Khi văn bản đã thay đổi, thực hiện chức năng tìm kiếm
                String keyword = s.toString();
                if (searchUser_txt == null) {
                    toolbar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    layoutSearch.setVisibility(View.GONE);

                }
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

        // Đăng ký lắng nghe sự kiện tin nhắn mới từ Pusher khi Activity được resume
        pushergroup = PusherClient.init();
        pushergroup.connect();
        pushergroup.subscribe("ListGroupChat")
                .bind("lastmess", (channelName, eventName, data) -> {
                    // Xử lý dữ liệu tin nhắn mới nhận được từ Pusher
                    Log.d("Pusher", "Received new message: " + data);
                    try {
                        // Xử lý dữ liệu tin nhắn mới
                        processNewMessage(data);
                    } catch (Exception e) {
                        Log.e("Pusher", "Error processing new message: " + e.getMessage());
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

        mainChatViewModel.getListChat(userId).observe(this, groupChatList -> {
            adapter.setGroupChatList(groupChatList);
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");
        // Hủy đăng ký lắng nghe khi không cần thiết
       messageViewModel.getListChat(userId).removeObservers(this);


        mainChatViewModel.getListChat(userId).removeObservers(this);

    }
    private void processNewMessage(Object data) {

        try {

            // Chuyển đổi dữ liệu JSON thành danh sách các cuộc trò chuyện nhóm mới
            Type listType = new TypeToken<List<GroupChatWithMessagesResponse>>(){}.getType();
            List<GroupChatWithMessagesResponse> newGroupChats = new Gson().fromJson(new JsonParser().parse(data.toString()), listType);

            // Cập nhật dữ liệu mới trên main thread
            runOnUiThread(() -> {
                // Duyệt qua danh sách các cuộc trò chuyện nhóm mới từ Pusher
                for (GroupChatWithMessagesResponse newGroupChat : newGroupChats) {
                    boolean isExisting = false;
                    // Duyệt qua danh sách các cuộc trò chuyện nhóm hiện tại
                    for (int i = 0; i < groupChatList.size(); i++) {
                        GroupChatWithMessagesResponse groupChat = groupChatList.get(i);
                        // Kiểm tra xem cuộc trò chuyện nhóm đã tồn tại trong danh sách hay chưa
                        if (groupChat.getId().equals(newGroupChat.getId())) {
                            // Nếu đã tồn tại, cập nhật tin nhắn mới nhất
                            groupChat.setLastMessage(newGroupChat.getLastMessage());
                            // Di chuyển cuộc trò chuyện nhóm lên đầu danh sách
                            groupChatList.remove(i);
                            Log.d("GroupChatPosition", "Group chat position: " + i);
                            groupChatList.add(0, groupChat);
                            //Log.d("ds", groupChatList.get(0).getLastMessage());
                            isExisting = true;
                            break;
                        }
                    }
                    // Nếu cuộc trò chuyện nhóm chưa tồn tại trong danh sách, thêm mới vào đầu danh sách
                    if (!isExisting) {
                        groupChatList.add(0, newGroupChat);
                    }
                }
                // Cập nhật giao diện người dùng sau khi xử lý dữ liệu mới
                adapter.setGroupChatList(groupChatList);
            });

        } catch (Exception e) {
            Log.e("processNewMessage", "Failed to process new message: " + e.getMessage(), e);
        }


    }
    private void updateUIWithNewMessage(String newMessageData) {
        Gson gson = new Gson();
        Type messageType = new TypeToken<List<MessageWithSenderInfo>>() {}.getType();
//        List<MessageWithSenderInfo> messages = gson.fromJson(newMessageData, messageType);
        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");
        messageViewModel.getListChat(userId).observe(this, chatList -> {
            adapter.setChatList(chatList);
        });
    }

}

