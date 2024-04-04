package com.example.frontend.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.frontend.R;
import com.example.frontend.activities.FollowsActivity;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.adapter.SuggestedMeAdapter;
import com.example.frontend.request.Follows.RequestCreateFollows;
import com.example.frontend.request.Follows.RequestUpdateFollows;
import com.example.frontend.request.User.RequestUpdateUser;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Follows.GetQuantityResponse;
import com.example.frontend.response.User.GetAllUserByFollowsResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.FirebaseStorageUploader;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_search_ClickAccount extends Fragment {

    ImageButton editprofileImageBtn, menuSetting;
    Button followBtn, messageBtn, qrcodeBtn;
    TextView username, followingCount, followerCount, postCount;
    LinearLayout openFollows, openFollowing;
    UserViewModel userViewModel;
    String userId;
    CircleImageView profileImage;
    TextView nameTV;
    Toolbar toolbar;
    public static FollowsViewModel followsViewModel;
    private List<Uri> selectedFiles;
    private List<GetAllUserByFollowsResponse> userResponseList = new ArrayList<>();


    @Override
    public void onResume() {
        super.onResume();
        userViewModel.getDetailUserById(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()) {
                    UserResponse userResponse = response.getData();
                    username.setText(userResponse.getUsername());
                    Picasso.get().load(userResponse.getAvatarImg()).into(profileImage);
                    nameTV.setText(userResponse.getName());
                    followerCount.setText(String.valueOf(userResponse.getFollowers()));
                    followingCount.setText(String.valueOf(userResponse.getFollowing()));
                    postCount.setText("0");
                }
            }
        });
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Kiểm tra nếu có fragment trước đó trong back stack
//        boolean hasPreviousFragment = getFragmentManager().getBackStackEntryCount() > 0;
//
//        // Kiểm tra xem ActionBar có null hay không
//        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        if (actionBar != null) {
//            // Hiển thị nút "Back" trên ActionBar
//            actionBar.setDisplayHomeAsUpEnabled(hasPreviousFragment);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_click_account, container, false);
        qrcodeBtn = view.findViewById(R.id.qrcodeBtn);
        followBtn = view.findViewById(R.id.followBtn);
        messageBtn = view.findViewById(R.id.messageBtn);
        username = view.findViewById(R.id.toolbarNameAccount);
        followingCount = view.findViewById(R.id.followingCount);
        followerCount = view.findViewById(R.id.followerCount);
        postCount = view.findViewById(R.id.postCount);
        openFollows = view.findViewById(R.id.openFollows);
        openFollowing = view.findViewById(R.id.openFollowing);
        profileImage = view.findViewById(R.id.profileImage);
        nameTV = view.findViewById(R.id.nameTV);
        toolbar = view.findViewById(R.id.toolbar);

        followsViewModel = new ViewModelProvider(this).get(FollowsViewModel.class);

        // Thiết lập Toolbar là ActionBar của activity
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Nhận dữ liệu email từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("userId") != null) {
                userId = bundle.getString("userId", "");
            }
        }

        userViewModel.getDetailUserById(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()) {
                    UserResponse userResponse = response.getData();
                    username.setText(userResponse.getUsername());
                    Picasso.get().load(userResponse.getAvatarImg()).into(profileImage);
                    nameTV.setText(userResponse.getName());
                    followerCount.setText(String.valueOf(userResponse.getFollowers()));
                    followingCount.setText(String.valueOf(userResponse.getFollowing()));
                    postCount.setText("0");
                }
            }
        });

        userViewModel.getAllUsersByFollows(SharedPreferenceLocal.read(getContext().getApplicationContext(), "userId")).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<GetAllUserByFollowsResponse>>>() {
            @Override
            public void onChanged(ApiResponse<List<GetAllUserByFollowsResponse>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                if (response.getData().size() > 0) {
                    userResponseList = response.getData();
                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
            }
        });

        // Nếu người dùng hiện tại đã được follow
        if (!check_user_Followed()) {
            setTextBtn(followBtn, "Following");
        }
        System.out.println(userResponseList.size());

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Nếu người dùng hiện tại chưa được follow
                if (check_user_Followed()) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    Date currentDate = new Date();
                    String formattedDate = dateFormat.format(currentDate);

                    RequestCreateFollows requestCreateFollows = new RequestCreateFollows(SharedPreferenceLocal.read(getContext(), "userId"), userId, formattedDate);
                    followsViewModel.createFollows(requestCreateFollows).observe(getViewLifecycleOwner(), new Observer<ApiResponse<String>>() {
                        @Override
                        public void onChanged(ApiResponse<String> response) {
                            if (response.getStatus() && response.getMessage().equals("Success")) {
                                setTextBtn(followBtn, "Following");
                                handleGetQuantityFollows(SharedPreferenceLocal.read(getContext(), "userId"), "following");
                                handleGetQuantityFollows(userId, "follower");
                            }
                        }
                    });
                }

            }
        });

        openFollows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowsActivity.class);
                // Thêm dữ liệu cho Intent để FragmentReplacerActivity biết cần thay thế fragment nào
                intent.putExtra("fragment_to_load", "openFollows");
                intent.putExtra("userName", username.getText().toString());
                intent.putExtra("userId", userId);
                // Bắt đầu activity với Intent đã tạo
                startActivity(intent);
            }
        });
        openFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowsActivity.class);
                // Thêm dữ liệu cho Intent để FragmentReplacerActivity biết cần thay thế fragment nào
                intent.putExtra("fragment_to_load", "openFollowing");
                intent.putExtra("userName", username.getText().toString());
                intent.putExtra("userId", userId);
                // Bắt đầu activity với Intent đã tạo
                startActivity(intent);
            }
        });

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        menuSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String usernameValue = username.getText().toString();
//                Bundle bundle = new Bundle();
//                bundle.putString("username", usernameValue);
//                SettingFragment settingFragment = new SettingFragment();
//                settingFragment.setArguments(bundle); // Thiết lập arguments cho SettingFragment
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_layout_main, settingFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        qrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo instance của fragment mới
                Fragment QRCodeFragment = new QRCodeFragment();

                // Lấy instance của FragmentManager
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                // Bắt đầu transaction để thay thế fragment hiện tại bằng fragment mới
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_layout_main, QRCodeFragment);
                fragmentTransaction.addToBackStack(null); // Thêm transaction này vào stack back để quay trở lại fragment trước đó (nếu cần)
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    // Kiểm tra người dùng hiện tại đã được follow chưa
    private boolean check_user_Followed() {
        int i = 0;
        while(i < userResponseList.size()) {
            if (userId.equals(userResponseList.get(i).getId())) {
                System.out.println(userResponseList.get(i).getId());
                return true;
            }
            i++;
        }
        return false;
    }

    public void setTextBtn(Button btnFollow, String text){
        btnFollow.setText(text);
        // Get the reference to the Drawable you want to assign
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.custom_buttom_selected_follows);

        // Assign Drawable to Button
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            // For Android versions below API 16
            btnFollow.setBackgroundDrawable(drawable);
        } else {
            // For Android versions from API 16 and above
            btnFollow.setBackground(drawable);
        }
    }

    private void handleGetQuantityFollows(String idFollows,String type){
        followsViewModel.getQuantityFollows(idFollows).observe(getViewLifecycleOwner(), new Observer<ApiResponse<GetQuantityResponse>>() {
            @Override
            public void onChanged(ApiResponse<GetQuantityResponse> response) {
                if(response.getMessage().equals("Success") && response.getStatus()){
                    GetQuantityResponse getQuantityResponse = response.getData();
                    // update follower của người được theo dõi
                    if(type.equals("follower")){
                        int countFollower = getQuantityResponse.getQuantityFollower() + 1;
                        RequestUpdateFollows requestUpdateFollowsByFollower = new RequestUpdateFollows(
                                getQuantityResponse.getId(), countFollower,getQuantityResponse.getQuantityFollowing()
                        );
                        handleUpdateFollow(requestUpdateFollowsByFollower);
                    } // update thông tin của người dùng hiện tại ( tăng số người đang theo dõi)
                    else if (type.equals("following")) {
                        int countFollowing = getQuantityResponse.getQuantityFollowing() + 1;
                        RequestUpdateFollows requestUpdateFollowsByFollower = new RequestUpdateFollows(
                                getQuantityResponse.getId(), getQuantityResponse.getQuantityFollower(),countFollowing
                        );
                        handleUpdateFollow(requestUpdateFollowsByFollower);
                    }
                }
            }
        });
    }

    private void handleUpdateFollow(RequestUpdateFollows requestUpdateFollows){
        followsViewModel.updateFollows(requestUpdateFollows);
    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            selectedFiles = new ArrayList<>();
//            if (data != null && data.getData() != null) {
//                Uri fileUri = data.getData();
//                selectedFiles.add(fileUri);
//                uploadFilesToFirebaseStorage();
//            }
//        }
//    }
//
//    private void uploadFilesToFirebaseStorage() {
//        int totalFiles = selectedFiles.size();
//        final int[] uploadedFiles = {0};
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String timestamp = sdf.format(new Date());
//
//        for (int i = 0; i < selectedFiles.size(); i++) {
//            Uri fileUri = selectedFiles.get(i);
//            String fileName = "file_" + timestamp + "_" + new File(fileUri.getPath()).getName() + ".jpg";
//            FirebaseStorageUploader.uploadFileToFirebaseStorage(fileUri, fileName, new FirebaseStorageUploader.OnUploadCompleteListener() {
//                @Override
//                public void onUploadComplete(List<String> fileUrls) {
//
//                }
//
//                @Override
//                public void onUploadComplete(String fileUrl) {
//                    uploadedFiles[0]++;
//                    if (uploadedFiles[0] == totalFiles) {
//                        RequestUpdateUser requestUpdateUser = new RequestUpdateUser(
//                                userId, "", "", "", fileUrl, "", "", "", ""
//                        );
//                        updateUser(requestUpdateUser);
//                    }
//                }
//
//                @Override
//                public void onUploadFailed(String errorMessage) {
//                    Toast.makeText(getContext(), "Upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    private void updateUser(RequestUpdateUser requestUpdateUser) {
//        userViewModel.updateUser(requestUpdateUser).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
//            @Override
//            public void onChanged(ApiResponse<UserResponse> response) {
//                if (response.getMessage().equals("Update Success!") && response.getStatus()) {
//                    UserResponse userResponse = response.getData();
//                    Picasso.get().load(userResponse.getAvatarImg()).into(profileImage);
//                    Toast.makeText(getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        ;
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Hiển thị nút "Back" trên ActionBar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Xử lý sự kiện khi nút "Back" được nhấn
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại FragmentA khi nút "Back" được nhấn
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
