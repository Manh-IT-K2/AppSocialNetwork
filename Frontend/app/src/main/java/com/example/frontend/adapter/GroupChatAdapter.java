package com.example.frontend.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.activities.ChatGroupActivity;
import com.example.frontend.repository.UserRepository;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.response.Message.MessageWithSenderInfo;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.viewModel.User.UserViewModel;
import com.pusher.client.channel.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {
    private List<MessageWithSenderInfo> messages;
    private Context context;
    private String currentUserId; // Assuming you have the current user's ID

    private String NameOtherUser, NameCurrentUser;
    private LifecycleOwner lifecycleOwner;
    public GroupChatAdapter(Context context, List<MessageWithSenderInfo> messages, String currentUserId, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.messages = messages;
        this.currentUserId = currentUserId;
        this.lifecycleOwner = lifecycleOwner;
    }
    public void setMessages(List<MessageWithSenderInfo> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }
    // Phương thức thêm tin nhắn mới vào danh sách
    public void addNewMessage(MessageWithSenderInfo message) {
        // Thêm tin nhắn mới vào danh sách tin nhắn của adapter
        messages.add(message);
        // Thông báo cho RecyclerView về sự thay đổi
        notifyItemInserted(messages.size() - 1);
    }
    public void addMessage(GroupChatWithMessagesResponse groupChatWithMessagesResponse) {
        // Lấy danh sách tin nhắn từ đối tượng GroupChatWithMessagesResponse
        List<MessageWithSenderInfo> newMessages = groupChatWithMessagesResponse.getMessages();

        // Loại bỏ tin nhắn trùng lặp trong newMessages so với danh sách tin nhắn hiện tại
        List<MessageWithSenderInfo> uniqueNewMessages = new ArrayList<>();
        for (MessageWithSenderInfo newMessage : newMessages) {
            boolean isDuplicate = false;
            for (MessageWithSenderInfo existingMessage : messages) {
                if (newMessage.getId().equals(existingMessage.getId())) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                uniqueNewMessages.add(newMessage);
            }
        }

        // Thêm các tin nhắn mới không trùng lặp vào danh sách và thông báo cho Adapter cập nhật
        messages.addAll(uniqueNewMessages);
        notifyItemRangeInserted(messages.size() - uniqueNewMessages.size(), uniqueNewMessages.size());
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageWithSenderInfo message = messages.get(position);

        // Lấy đối tượng người gửi từ tin nhắn
        User sender = message.getSender();
        // Lấy ID của người gửi
        String senderId = sender.getId();

        UserViewModel userViewModel = new ViewModelProvider((ViewModelStoreOwner) lifecycleOwner).get(UserViewModel.class);
        // Lấy thông tin user từ senderId
        userViewModel.getDetailUserById(senderId).observe(lifecycleOwner, new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()) {
                    UserResponse userResponse = response.getData();
                    NameOtherUser = userResponse.getUsername();
                }
            }
        });
        // Lấy thông tin user hiện hành từ id currentUserId
        userViewModel.getDetailUserById(currentUserId).observe(lifecycleOwner, new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()) {
                    UserResponse userResponse = response.getData();
                    NameCurrentUser = userResponse.getUsername();
                }
            }
        });

        // Nếu tin nhắn được gửi bởi người dùng hiện tại
        if (senderId != null && senderId.equals(currentUserId)) {
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextView.setText(message.getContent());
            holder.user_name_right.setText(NameCurrentUser); // Hiển thị tên người gửi tin nhắn
            holder.user_name_left.setText(""); // Ẩn tên người gửi tin nhắn bên trái
        } else { // Nếu tin nhắn được gửi bởi người dùng khác
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextView.setText(message.getContent());
            holder.user_name_left.setText(NameOtherUser); // Hiển thị tên người gửi tin nhắn bên trái
            holder.user_name_right.setText(""); // Ẩn tên người gửi tin nhắn bên phải
        }

        // Lấy thời gian gửi tin nhắn
        Date sentTime = message.getCreatedAt();
        Date currentTime = new Date(); // Thời gian hiện tại

// Kiểm tra thời gian giữa tin nhắn cuối cùng và thời gian hiện tại trong vòng 1 giờ
        boolean withinOneHour = isWithinOneHour(sentTime, currentTime);

// Kiểm tra xem tin nhắn trước đó có được gửi trong vòng 1 giờ không
        boolean prevWithinOneHour = false;
        if (position > 0) {
            MessageWithSenderInfo prevMessage = messages.get(position - 1);
            prevWithinOneHour = isWithinOneHour(prevMessage.getCreatedAt(), currentTime);
        }

// Định dạng thời gian dựa trên điều kiện
        String formattedDate;
        if (withinOneHour && !prevWithinOneHour) {
            // Nếu tin nhắn cuối cùng trong vòng 1 giờ và không có tin nhắn trước đó trong vòng 1 giờ, hiển thị thời gian cho tin nhắn cuối cùng
            SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm"); // Định dạng giờ và phút
            formattedDate = hourMinuteFormat.format(sentTime);
        } else if (withinOneHour && prevWithinOneHour) {
            // Nếu cả hai tin nhắn đều trong vòng 1 giờ, không hiển thị thời gian cho tin nhắn đang xét
            formattedDate = "";
        } else if (isMoreThan24Hours(sentTime, currentTime)) {
            // Nếu lớn hơn 24 giờ, hiển thị thời gian đầy đủ
            SimpleDateFormat fullDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy"); // Định dạng thời gian đầy đủ
            formattedDate = fullDateFormat.format(sentTime);
        } else {
            // Hiển thị thời gian chỉ với giờ và phút
            SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm"); // Định dạng giờ và phút
            formattedDate = hourMinuteFormat.format(sentTime);
        }

// Đặt giá trị cho TextView
        holder.timeTextView.setText(formattedDate);


    }
    // Kiểm tra xem hai thời điểm có cách nhau trong vòng 1 giờ không
    private boolean isWithinOneHour(Date date1, Date date2) {
        long diffInMilliseconds = Math.abs(date2.getTime() - date1.getTime());
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds);
        return diffInMinutes <= 60;
    }

    // Kiểm tra xem hai thời điểm có cách nhau hơn 24 giờ không
    private boolean isMoreThan24Hours(Date date1, Date date2) {
        long diffInMilliseconds = Math.abs(date2.getTime() - date1.getTime());
        long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMilliseconds);
        return diffInHours >= 24;
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, leftChatTextView, rightChatTextView,user_name_left, user_name_right;
        LinearLayout leftChatLayout, rightChatLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.time_msg);
            leftChatTextView = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextView = itemView.findViewById(R.id.right_chat_textview);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            user_name_left = itemView.findViewById(R.id.user_name_left);
            user_name_right = itemView.findViewById(R.id.user_name_right);
        }
    }

}
