package com.example.frontend.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.response.User.UserResponse;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewMemberAdapter extends ArrayAdapter<UserResponse> {

    private Context mContext;
    private TextView textViewName,textViewSup;
    CircleImageView imgAvatar;
    private List<UserResponse> mMembers;
    public ViewMemberAdapter(Context context, List<UserResponse> members) {
        super(context, 0, members);
        mContext = context;
        mMembers = members;
    }
    public void add(UserResponse user) {
        mMembers.add(user);
        notifyDataSetChanged(); // Cập nhật giao diện
    }
    public void setMembers(List<UserResponse> members) {
        mMembers.clear(); // Xóa dữ liệu hiện tại
        mMembers.addAll(members); // Thêm dữ liệu mới
        notifyDataSetChanged(); // Cập nhật giao diện
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_view_members_of_groupchat, parent, false);
        }

        UserResponse currentMember = mMembers.get(position);

        textViewName = listItem.findViewById(R.id.textFriendName);
        textViewSup = listItem.findViewById(R.id.subInformation);
        imgAvatar = listItem.findViewById(R.id.imgAvatar);

        Glide.with(mContext)
                .load(currentMember.getAvatarImg() != null ? currentMember.getAvatarImg() : R.drawable.logo)
                .centerCrop().into(imgAvatar);

        textViewName.setText(currentMember.getUsername());

        textViewSup.setText("ID: "+currentMember.getId());

        Log.d("nickname", currentMember.getId());
        // Cập nhật các trường thông tin khác nếu cần

        return listItem;
    }


}
