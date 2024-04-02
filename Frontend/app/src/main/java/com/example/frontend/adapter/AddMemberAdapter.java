package com.example.frontend.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.fragments.AddMemberGroupChat;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

public class AddMemberAdapter extends RecyclerView.Adapter<AddMemberAdapter.ViewHolder> {
    private Context context;
    private List<UserResponse> userList;

    public AddMemberAdapter(Context context, List<UserResponse> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_friend_to_chatgroup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserResponse user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTextView;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.textFriendName);
            checkBox = itemView.findViewById(R.id.checkboxFriend);
        }

        public void bind(UserResponse user) {
            usernameTextView.setText(user.getUsername());
            // Xử lý sự kiện khi người dùng chọn một người dùng để thêm vào nhóm
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
//                        ((AddMemberGroupChat) context).addMemberToGroup(user);
                    }
                }
            });
        }
    }
}
