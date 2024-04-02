package com.example.frontend.adapter;

import android.content.Context;
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
import com.example.frontend.response.User.GetAllUserByFollowsResponse;

import java.util.List;

public class FlowAdapter extends ArrayAdapter<GetAllUserByFollowsResponse> {
    private List<GetAllUserByFollowsResponse> userList;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemCheckedListener onItemCheckedListener;

    public FlowAdapter(Context context, List<GetAllUserByFollowsResponse> userList, OnItemCheckedListener listener) {
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GetAllUserByFollowsResponse user = userList.get(position);
        holder.textFriendName.setText(user.getName());
        // holder.subInformation.setText(user.getSubInformation());

        // Set trạng thái của checkbox dựa trên trường boolean trong mỗi mục
        holder.checkbox.setChecked(user.isSelected());
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
        void onItemChecked(GetAllUserByFollowsResponse user, boolean isChecked);
    }
}