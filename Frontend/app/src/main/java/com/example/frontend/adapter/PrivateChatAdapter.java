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

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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


    public MessageWithSenderInfo getMessageAtPosition(int position) {
        if (position >= 0 && position < messages.size()) {
            return messages.get(position);
        }
        return null;
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
        String userId = SharedPreferenceLocal.read(context.getApplicationContext(), "userId"); // assuming this retrieves current user id

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
        holder.time_msg.setText(formattedDate);
    }
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
        LinearLayout leftChatLayout,rightChatLayout,layout_time;
        TextView leftChatTextview,rightChatTextview,time_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
            layout_time = itemView.findViewById(R.id.layout_time);
            time_msg =  itemView.findViewById(R.id.time_msg);
        }
    }
}
