package com.example.frontend.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.frontend.R;
import com.example.frontend.response.User.UserResponse;
import java.util.List;

public class ViewMemberAdapter extends ArrayAdapter<UserResponse> {

    private Context mContext;
    private TextView textViewName,textViewSup;
    private List<UserResponse> mMembers;

    public ViewMemberAdapter(Context context, List<UserResponse> members) {
        super(context, 0, members);
        mContext = context;
        mMembers = members;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_view_members_of_groupchat, parent, false);
        }

        UserResponse currentMember = mMembers.get(position);

        textViewName = listItem.findViewById(R.id.textFriendName);
        textViewName.setText(currentMember.getName());
        Log.d("name", currentMember.getName());

        textViewSup = listItem.findViewById(R.id.subInformation);
        textViewSup.setText(currentMember.getUsername());
        Log.d("nickname", currentMember.getUsername());
        // Cập nhật các trường thông tin khác nếu cần

        return listItem;
    }

    public void add(UserResponse member) {
        mMembers.add(member);
        notifyDataSetChanged();
    }
}
