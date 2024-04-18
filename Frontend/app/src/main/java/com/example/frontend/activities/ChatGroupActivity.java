package com.example.frontend.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapter.GroupChatAdapter;
import com.example.frontend.request.GroupChat.RequestChatGroup;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatResponse;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.response.Message.Message;
import com.example.frontend.response.Message.MessageWithSenderInfo;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.PusherClient;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Message.GroupChatViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pusher.client.Pusher;

import java.util.ArrayList;
import java.util.List;

import io.stipop.Stipop;
import io.stipop.StipopDelegate;
import io.stipop.extend.StipopImageView;
import io.stipop.model.SPPackage;
import io.stipop.model.SPSticker;

public class ChatGroupActivity extends AppCompatActivity implements StipopDelegate {
    private boolean initialMessagesLoaded = false;
    private RecyclerView recyclerView;
    private GroupChatAdapter adapter;
    private EditText inputMessage;
    private ImageButton btnSend,btn_Menu,btn_back, btn_File, btn_openSticker;
    private GroupChatViewModel groupChatViewModel;
    private TextView groupNameTextView;
    private RelativeLayout groupAvatarImageView;
    private String groupId; // Thêm biến để lưu trữ id của nhóm chat
    private String currentUserId; // Thêm biến để lưu trữ ID của người dùng hiện tại
    private String message;

    private GroupChatResponse Infor_GroupChat;
    private boolean isInforGroupChatLoaded = false;
    private Pusher pusher;
    StipopImageView btn_Sticker;
    ImageView stickerImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);


        currentUserId = SharedPreferenceLocal.read(getApplicationContext(), "userId");
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
        btn_back=findViewById(R.id.back_btn);
        btn_Menu=findViewById(R.id.menu_btn);
        btn_Sticker = findViewById(R.id.btn_Sticker);
        btn_File = findViewById(R.id.btn_File);
        stickerImageView = findViewById(R.id.stickerImageView);
        btn_openSticker = findViewById(R.id.btn_openSticker);

        groupChatViewModel = new ViewModelProvider(this).get(GroupChatViewModel.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new GroupChatAdapter(this, new ArrayList<>(), currentUserId,this);


        recyclerView.setAdapter(adapter);

        // Lấy và hiển thị lịch sử tin nhắn khi hoạt động được tạo
        loadChatHistory();

        // Thực hiện bắt sự kiện từ Pusher
        pusher = PusherClient.init();
        pusher.connect();
        pusher.subscribe(groupId)

                .bind("send_chatgroup", (channelName, eventName, data) -> {
                    try {
                        Gson gson = new GsonBuilder()
                                .setDateFormat("MMM dd, yyyy, hh:mm:ss a")
                                .create();
                        String jsonData = data.toString();
                        MessageWithSenderInfo message = gson.fromJson(jsonData, MessageWithSenderInfo.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addNewMessage(message); // Thêm tin nhắn mới vào RecyclerView
                                recyclerView.scrollToPosition(adapter.getItemCount() - 1); // Cuộn đến tin nhắn mới nhất
                            }
                        });

                    } catch (Exception e) {
                        Log.d("trycatch", new Gson().toJson(e));
                    }
                });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Gửi tin nhắn khi nhấn nút gửi
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("chatgroup_content", groupId+currentUserId+message);
                message = inputMessage.getText().toString();
                if(!message.isEmpty()){
                    RequestChatGroup request = new RequestChatGroup(groupId, currentUserId, message, "","");

                    groupChatViewModel.sendMessage(groupId, request);
                    inputMessage.setText("");
                    message = "";
                }
            }
        });

        btn_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupChatViewModel.getGroupChatById(groupId).observe(ChatGroupActivity.this, new Observer<GroupChatResponse>() {
                    @Override
                    public void onChanged(GroupChatResponse response) {
                        if (response != null) {
                            // Xử lý khi nhận được phản hồi thành công
                            Infor_GroupChat = response;
//                            Log.d("ktra1", Infor_GroupChat.getCreator().toString());
                            // Đảm bảo rằng dữ liệu đã được tải
                            isInforGroupChatLoaded = true;
                        } else {
                            // Xử lý khi nhận được thông báo lỗi
                            Toast.makeText(ChatGroupActivity.this, "Failed to load group chat info", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                showPopupMenu(v);
            }
        });

        Stipop.Companion.connect(this, btn_Sticker, "1234", "en", "US", this);
        //btn_Sticker.setOnClickListener(v -> Stipop.Companion.showSearch());

        btn_openSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stipop.Companion.showSearch();
            }
        });

        btn_File.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        if (currentUserId.equals(Infor_GroupChat.getCreatorId())) {
            // Hiển thị tất cả các mục menu nếu là người tạo nhóm
        } else {
            // Ẩn mục menu "Xóa thành viên" và "Giải tán nhóm" nếu không phải là người tạo nhóm
            popupMenu.getMenu().findItem(R.id.menu_remove_member).setVisible(false);
            popupMenu.getMenu().findItem(R.id.menu_disband_group).setVisible(false);
        }

        // Thiết lập sự kiện cho mỗi mục menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_view_members) {
                    // Gọi phương thức hiển thị danh sách thành viên
                    showGroupMembers();
                    return true;
                }
                if (item.getItemId() == R.id.menu_add_member) {
                    addMember();
                    finish();
                    return true;
                }
                if (item.getItemId() == R.id.menu_remove_member) {
                    removeMember();
                    finish();
                    return true;
                }
                if (item.getItemId() == R.id.menu_disband_group) {
                    // Gọi hàm xử lý giải tán nhóm
                    handleDisbandGroup();
                    // Quay lại MainActivityChat mới
                    Intent intent = new Intent(ChatGroupActivity.this, MainChatActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

        // Hiển thị menu popup
        popupMenu.show();

    }


    private void loadChatHistory() {
        if (!initialMessagesLoaded) {
            // Chỉ tải tin nhắn ban đầu nếu chưa được tải
            groupChatViewModel.getMessagesByGroupChatId(groupId).observe(this, new Observer<ApiResponse<GroupChatWithMessagesResponse>>() {
                @Override
                public void onChanged(ApiResponse<GroupChatWithMessagesResponse> response) {
                    if (response != null && response.isSuccess()) {
                        GroupChatWithMessagesResponse chatHistory = response.getData();
                        if (chatHistory != null) {
                            List<MessageWithSenderInfo> messages = chatHistory.getMessages();
                            if (messages != null && !messages.isEmpty()) {
                                // Nếu có tin nhắn, hiển thị chúng và cuộn đến tin nhắn mới nhất
                                adapter.setMessages(messages);
                                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                            }
                        }
                        // Đã tải tin nhắn ban đầu
                        initialMessagesLoaded = true;

                    } else {
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

    private void handleDisbandGroup() {
        groupChatViewModel.deleteGroupChat(groupId).observe(this, new Observer<ApiResponse<String>>() {
            @Override
            public void onChanged(ApiResponse<String> response) {
                if (response != null && response.isSuccess()) {
                    // Xử lý khi giải tán nhóm thành công
                    Toast.makeText(ChatGroupActivity.this, "Group disbanded successfully", Toast.LENGTH_SHORT).show();
                    // Kết thúc hoạt động và quay lại màn hình trước đó
                    finish();
                } else {
                    // Xử lý khi không thể giải tán nhóm
                    String errorMessage = "Failed to disband group";
                    if (response != null && response.getMessage() != null) {
                        errorMessage += ": " + response.getMessage();
                    }
                    Toast.makeText(ChatGroupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showGroupMembers() {
        Intent intent = new Intent(ChatGroupActivity.this, ViewMembers.class);
        intent.putExtra("groupChatId", groupId);
        intent.putExtra("groupChatName", Infor_GroupChat.getGroupName());
        intent.putExtra("idcreater", Infor_GroupChat.getCreatorId());
        // Tạo một danh sách chứa id của các thành viên
        ArrayList<String> memberIdList = new ArrayList<>();
        for (UserResponse member : Infor_GroupChat.getMembers()) {
            memberIdList.add(member.getId());
        }

        // Truyền danh sách id qua Intent
        intent.putStringArrayListExtra("memberIdList", memberIdList);

        startActivity(intent);
    }
    private void addMember(){
        Intent intent = new Intent(ChatGroupActivity.this, AddMemberGroupChat.class);
        intent.putExtra("groupChatId", groupId);
        intent.putExtra("groupChatName", Infor_GroupChat.getGroupName());
        // Tạo một danh sách chứa id của các thành viên
        ArrayList<String> memberIdList = new ArrayList<>();
        for (UserResponse member : Infor_GroupChat.getMembers()) {
            memberIdList.add(member.getId());
        }

        // Truyền danh sách id qua Intent
        intent.putStringArrayListExtra("memberIdList", memberIdList);

        startActivity(intent);
    }
    private void removeMember(){
        Intent intent = new Intent(ChatGroupActivity.this, RemoveMemberGroupChat.class);
        intent.putExtra("groupChatId", groupId);
        intent.putExtra("groupChatName", Infor_GroupChat.getGroupName());
        // Tạo một danh sách chứa id của các thành viên
        ArrayList<String> memberIdList = new ArrayList<>();
        for (UserResponse member : Infor_GroupChat.getMembers()) {
            memberIdList.add(member.getId());
        }

        // Truyền danh sách id qua Intent
        intent.putStringArrayListExtra("memberIdList", memberIdList);

        startActivity(intent);
    }

    @Override
    public boolean canDownload(@NonNull SPPackage spPackage) {
        return true;
    }

    @Override
    public boolean onStickerSelected(@NonNull SPSticker spSticker) {
        RequestChatGroup request = new RequestChatGroup(groupId, currentUserId,"", spSticker.getStickerImg(),"");
        groupChatViewModel.sendMessage(groupId, request);
        return false;
    }
}
