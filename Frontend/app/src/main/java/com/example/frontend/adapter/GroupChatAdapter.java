package com.example.frontend.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.response.Message.MessageWithSenderInfo;

import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {
    private List<MessageWithSenderInfo> messages;
    private Context context;
    private String currentUserId; // Assuming you have the current user's ID

    public GroupChatAdapter(Context context, List<MessageWithSenderInfo> messages, String currentUserId) {
        this.context = context;
        this.messages = messages;
        this.currentUserId = currentUserId;
    }
    public void setMessages(List<MessageWithSenderInfo> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    // Phương thức addMessage để thêm danh sách tin nhắn mới vào danh sách và thông báo cho RecyclerView
    public void addMessage(GroupChatWithMessagesResponse groupChatWithMessagesResponse) {
        // Lấy danh sách tin nhắn từ đối tượng GroupChatWithMessagesResponse
        List<MessageWithSenderInfo> newMessages = groupChatWithMessagesResponse.getMessages();

        // Thêm từng tin nhắn vào danh sách messages
        messages.addAll(newMessages);
        notifyItemRangeInserted(messages.size() - newMessages.size(), newMessages.size());
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

        // Set visibility of chat layout based on sender
        if (message.getSender().equals(currentUserId)) {
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextView.setText(message.getContent());
        } else {
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextView.setText(message.getContent());
        }
        holder.timeTextView.setText(message.getCreatedAt().toString()); // Set time
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, leftChatTextView, rightChatTextView;
        LinearLayout leftChatLayout, rightChatLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.time_msg);
            leftChatTextView = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextView = itemView.findViewById(R.id.right_chat_textview);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
        }
    }

}
