package com.example.frontend.adapter;

import android.content.Context;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.frontend.R;
import com.example.frontend.response.User.GetAllUserByFollowsResponse;
import com.google.gson.Gson;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddMemberAdapter extends ArrayAdapter<GetAllUserByFollowsResponse> {
    private List<GetAllUserByFollowsResponse> userList;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemCheckedListener onItemCheckedListener;
    private SparseBooleanArray checkedPositions = new SparseBooleanArray(); // Danh sách để lưu trạng thái checked của mỗi item

    public AddMemberAdapter(Context context, List<GetAllUserByFollowsResponse> userList, OnItemCheckedListener listener) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
        this.layoutInflater = LayoutInflater.from(context);
        this.onItemCheckedListener = listener;
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
            holder.imgAvatar = convertView.findViewById(R.id.imgAvatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GetAllUserByFollowsResponse user = userList.get(position);
        holder.textFriendName.setText(user.getUsername());
        holder.subInformation.setText(user.getId());

        Glide.with(context)
                .load(user.getAvatarImg() != null && !user.getAvatarImg().isEmpty() ? user.getAvatarImg() : R.drawable.logo )
                .centerCrop().into(holder.imgAvatar);

        // Set trạng thái của checkbox dựa trên cờ isSelected của người dùng
        holder.checkbox.setOnCheckedChangeListener(null); // Remove listener to prevent unintended triggering
        holder.checkbox.setChecked(checkedPositions.get(position));
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


    // Mẫu ViewHolder để tái sử dụng hiệu quả các view
    private static class ViewHolder {
        CheckBox checkbox;
        TextView textFriendName;
        TextView subInformation;
        CircleImageView imgAvatar;
    }

    // Giao diện lắng nghe sự kiện khi mục được chọn/loại bỏ chọn
    public interface OnItemCheckedListener {
        void onItemChecked(GetAllUserByFollowsResponse user, boolean isChecked);
    }
}
