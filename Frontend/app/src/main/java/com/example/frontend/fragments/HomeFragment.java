package com.example.frontend.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.activities.CreatePostActivity;
import com.example.frontend.activities.MainChatActivity;
import com.example.frontend.adapter.PostAdapter;
import com.example.frontend.adapter.StoryAdapter;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.request.Story.RequestStoryByUserId;
import com.example.frontend.request.User.RequestUpdateTokenFCM;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.Story.StoryViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 1;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    public static RecyclerView recyclerViewPost, recyclerViewStory;
    private PostAdapter postAdapter;
    private StoryAdapter storyAdapter;
    private List<RequestPostByUserId> postList;
    private List<RequestStoryByUserId> storyList;
    private PostViewModel postViewModel;
    private StoryViewModel storyViewModel;
    private ImageView imgMessage;
    private UserViewModel userViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // init list post
        recyclerViewPost = view.findViewById(R.id.list_post);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext()));

        // init list story
        recyclerViewStory = view.findViewById(R.id.list_story);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewStory.setLayoutManager(layoutManager);

        // init call api post
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // init call api story
        storyViewModel = new ViewModelProvider(this).get(StoryViewModel.class);

        // init call api user
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        //View Chat
        imgMessage = view.findViewById(R.id.img_message);
        // init userId
        String userId = SharedPreferenceLocal.read(getContext(),"userId");

        // call api display list story in home
        storyViewModel.getListStoryByUserId(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<RequestStoryByUserId>>>() {
            @Override
            public void onChanged(ApiResponse<List<RequestStoryByUserId>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.d("check1", json);
                if (response.getData() != null) {
                    storyList = response.getData();
                    boolean foundUserStory = false; // Biến để kiểm tra xem đã tìm thấy story của user hay chưa
                    for (RequestStoryByUserId story : storyList) {
                        if (story.getUserId().equals(userId)) {
                            // Nếu tìm thấy story của user, đặt foundUserStory thành true và thoát khỏi vòng lặp
                            foundUserStory = true;
                            break;
                        }
                    }
                    if (foundUserStory) {
                        // Nếu tìm thấy story của user, hiển thị danh sách story
                        storyAdapter = new StoryAdapter(getContext(), storyList);
                        recyclerViewStory.setAdapter(storyAdapter);
                    } else {
                        // Nếu không tìm thấy story của user, thêm một story mặc định
                        userViewModel.getDetailUserById(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
                            @Override
                            public void onChanged(ApiResponse<UserResponse> response) {
                                if (response.getMessage().equals("Success") && response.getStatus()){
                                    Date createAt = new Date();
                                    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                                    String isoDateString = isoFormat.format(createAt);
                                    UserResponse userResponse = response.getData();
                                    RequestStoryByUserId defaultStory = new RequestStoryByUserId("",userId, userResponse.getAvatarImg(), userResponse.getUsername(), isoDateString, "",0, null, null, null);
                                    storyList.add(defaultStory);
                                    storyAdapter = new StoryAdapter(getContext(), storyList);
                                    recyclerViewStory.setAdapter(storyAdapter);
                                }
                            }
                        });
                    }
                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
            }
        });


        // call api display list post in home
        postViewModel.getListPostByUserId(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<RequestPostByUserId>>>() {
            @Override
            public void onChanged(ApiResponse<List<RequestPostByUserId>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.d("check", json);
                if (response.getData() != null) {
                    postList = response.getData();
                    postAdapter = new PostAdapter(getContext(), postList, (LifecycleOwner) getContext());
                    recyclerViewPost.setAdapter(postAdapter);
                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
            }

        });

       imgMessage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), MainChatActivity.class);
               startActivity(intent);
           }
       });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // Xử lý lỗi khi không lấy được token
                        return;
                    }

                    // Lấy token thành công
                    String deviceToken = task.getResult();
                    userViewModel.updateTokenFCM(new RequestUpdateTokenFCM(userId, deviceToken));
                    // Sử dụng deviceToken để gửi thông báo FCM cho thiết bị người nhận
                });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền truy cập đã được cấp, thực hiện các hành động cần thiết
            } else {
                // Quyền truy cập bị từ chối, xử lý tương ứng (ví dụ: hiển thị thông báo, vô hiệu hóa tính năng liên quan, vv.)
            }
        }
    }
}