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
import com.example.frontend.response.Message.MessageWithSenderInfo;
import com.example.frontend.response.PrivateChat.PrivateChatWithMessagesResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.pusher.client.channel.User;

import java.util.List;

public class PrivateChatAdapter extends RecyclerView.Adapter<PrivateChatAdapter.ViewHolder> {
    private List<MessageWithSenderInfo> messages; // Sửa tên biến thành 'messages'
    private Context context;

    public PrivateChatAdapter(List<MessageWithSenderInfo> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    public void setListMessage(List<MessageWithSenderInfo> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageWithSenderInfo message = messages.get(position); // Lấy tin nhắn tại vị trí 'position'

        // Lấy đối tượng người gửi từ tin nhắn
        User sender = message.getSender();
        if (sender != null) {
            // Lấy ID của người gửi
            String senderId = sender.getId();

            // Check if the senderId matches the current user id
            String userId = SharedPreferenceLocal.read(context.getApplicationContext(), "userId");// assuming this retrieves current user id

            // Logic to show message based on senderId
            if (senderId != null && senderId.equals(userId)) {
                // Current user sent the message
                holder.leftChatLayout.setVisibility(View.GONE);
                holder.rightChatLayout.setVisibility(View.VISIBLE);
                holder.rightChatTextview.setText(message.getContent());
            } else {
                // Other user sent the message
                holder.rightChatLayout.setVisibility(View.GONE);
                holder.leftChatLayout.setVisibility(View.VISIBLE);
                holder.leftChatTextview.setText(message.getContent());
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatLayout,rightChatLayout;
        TextView leftChatTextview,rightChatTextview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
        }
    }
}
