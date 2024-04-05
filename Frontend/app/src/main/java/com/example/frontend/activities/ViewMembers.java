package com.example.frontend.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.example.frontend.R;
import com.example.frontend.adapter.ViewMemberAdapter;
import com.example.frontend.repository.UserRepository;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.User.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewMembers extends AppCompatActivity {


    UserViewModel userViewModel;
    private String groupId;
    private String groupName;
    private TextView edtGroupName;
    private ListView listViewMembers;
    private ImageButton btnBack;
    private ViewMemberAdapter memberListAdapter;
    private List<UserResponse> tam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        // Nhận groupChatId và groupChatName từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupId = extras.getString("groupChatId");
            groupName = extras.getString("groupChatName");
        }

        // Khởi tạo views từ layout
        edtGroupName = findViewById(R.id.edtGroupName);
        listViewMembers = findViewById(R.id.listViewMembers);
        btnBack = findViewById(R.id.btnBack);

        // Hiển thị groupName trên TextView edtGroupName
        if (groupName != null) {
            edtGroupName.setText(groupName);
        }

        // Khởi tạo adapter và đặt cho ListView
        memberListAdapter = new ViewMemberAdapter(this, new ArrayList<>());
        listViewMembers.setAdapter(memberListAdapter);

        // Lấy danh sách thành viên từ Intent và hiển thị lên ListView
        ArrayList<String> memberIdList = getIntent().getStringArrayListExtra("memberIdList");
        // In danh sách memberIdList ra Logcat
        Log.d("MemberIdList", "Member IDs: " + memberIdList.toString());

        if (memberIdList != null) {
            tam=new ArrayList<>();
            for (String memberId : memberIdList) {
                final String currentMemberId = memberId;
                userViewModel.getDetailUserById(memberId).observe(this, new Observer<ApiResponse<UserResponse>>() {
                    @Override
                    public void onChanged(ApiResponse<UserResponse> response) {
                        if (response.getMessage().equals("Success") && response.getStatus() && memberIdList.contains(currentMemberId)) {
                            UserResponse userResponse = response.getData();
                            tam.add(userResponse);
                            Log.d("ttttt", userResponse.getId());
                        }
                    }
                });
            }
        }
        // Xử lý sự kiện khi nút back được nhấn
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng Activity hiện tại
//                finish();
            }
        });
    }
    // Phương thức để xử lý UserResponse
    private void upload(UserResponse userResponse) {
        // Thêm userResponse vào adapter hoặc làm bất kỳ điều gì bạn cần ở đây
        tam.add(userResponse);
        // Ví dụ:
        // memberListAdapter.add(userResponse);
        for (UserResponse i: tam) {
            Log.d("kkkk", i.getName());
        }
    }
}