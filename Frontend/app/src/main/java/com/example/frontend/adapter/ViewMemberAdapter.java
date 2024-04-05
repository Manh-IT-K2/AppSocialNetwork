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
//        // Kiểm tra nếu tên của người dùng là null, gán giá trị là "Chưa có tên"
//        if (currentMember.getName() == null || currentMember.getName().isEmpty()) {
//            textViewName.setText("Chưa có tên");
//        }else{
//            textViewName.setText(currentMember.getName());
//        }
        textViewName.setText(currentMember.getUsername());
        //Log.d("name", currentMember.getName());

        textViewSup = listItem.findViewById(R.id.subInformation);
        textViewSup.setText("ID: "+currentMember.getId());
        Log.d("nickname", currentMember.getId());
        // Cập nhật các trường thông tin khác nếu cần

        return listItem;
    }


}
