package com.example.frontend.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frontend.R;
import com.example.frontend.response.User.UserResponse;

import java.util.List;

public class RemoveMemberAdapter extends ArrayAdapter<UserResponse> {
    private List<UserResponse> userList;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemCheckedListener onItemCheckedListener;
    // Khai báo một danh sách để lưu trữ trạng thái của checkbox
    private SparseBooleanArray checkedPositions = new SparseBooleanArray();

    public RemoveMemberAdapter(Context context, List<UserResponse> userList, OnItemCheckedListener listener) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
        this.layoutInflater = LayoutInflater.from(context);
        this.onItemCheckedListener = listener;
    }
    public void setMembers(List<UserResponse> tam) {
        this.userList = tam;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_friend_to_chatgroup, parent, false);
            holder = new ViewHolder();
            holder.checkbox = convertView.findViewById(R.id.checkboxFriend);
            holder.textFriendName = convertView.findViewById(R.id.textFriendName);
            holder.subInformation = convertView.findViewById(R.id.subInformation);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserResponse user = userList.get(position);
        holder.textFriendName.setText(user.getUsername());
        holder.subInformation.setText(user.getId());

        holder.checkbox.setChecked(checkedPositions.get(position)); // Cập nhật trạng thái của checkbox

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedPositions.put(position, isChecked); // Lưu trạng thái mới của checkbox
                user.setSelected(isChecked);
                if (onItemCheckedListener != null) {
                    onItemCheckedListener.onItemChecked(user, isChecked);
                }
            }
        });

        return convertView;
    }

    // ViewHolder pattern để tối ưu hóa hiệu suất
    private static class ViewHolder {
        CheckBox checkbox;
        TextView textFriendName;
        TextView subInformation;
    }

    // Interface để lắng nghe sự kiện khi item được chọn
    public interface OnItemCheckedListener {
        void onItemChecked(UserResponse user, boolean isChecked);
    }
}
