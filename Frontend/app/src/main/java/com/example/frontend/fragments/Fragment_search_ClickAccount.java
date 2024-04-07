package com.example.frontend.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.ChatActivity;
import com.example.frontend.activities.FollowsActivity;
import com.example.frontend.adapter.FollowerAdapter;
import com.example.frontend.request.Follows.RequestCreateFollows;
import com.example.frontend.request.Follows.RequestUpdateFollows;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Follows.GetQuantityResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
    public FollowsViewModel followsViewModel;
    private List<Uri> selectedFiles;
    private UserResponse userResponse;
    private List<UserResponse> userFollowingList = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        userViewModel.getDetailUserById(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()) {
                    // UserResponse
                    userResponse = response.getData();
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
                if (response.getMessage().equals("Success!") && response.getStatus()) {
                    userResponse = response.getData();
                    username.setText(userResponse.getUsername());
                    Picasso.get().load(userResponse.getAvatarImg()).into(profileImage);
                    nameTV.setText(userResponse.getName());
                    followerCount.setText(String.valueOf(userResponse.getFollowers()));
                    followingCount.setText(String.valueOf(userResponse.getFollowing()));
                    postCount.setText("0");
                }
            }
        });
        followsViewModel.getUserFollowerById(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
            @Override
            public void onChanged(ApiResponse<List<UserResponse>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.d("follower", json);
                if (response.getMessage().equals("Success!") && response.getStatus()) {
                    userFollowingList = response.getData();
                    // Nếu người dùng hiện tại đã được follow
                    if (check_user_Followed()) {
                        setTextBtn(followBtn, "Following");
                    }
                } else {
                    Log.d("list follower", "no");
                }
            }
        });

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Nếu người dùng hiện tại chưa được follow
                if (followBtn.getText().toString().equals("Follow")) {
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
                } else if (followBtn.getText().toString().equals("Following")) {
                    // Nếu người dùng hiện tại đã được follow
                    openDialogFollowing(Gravity.BOTTOM, SharedPreferenceLocal.read(getContext().getApplicationContext(), "userId"), userId);
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
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("recipientUserId", userResponse.getUsername());
                intent.putExtra("recipientAvater", userResponse.getAvatarImg());
                intent.putExtra("recipientID", userResponse.getId());
                startActivity(intent);

            }
        });

        qrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle1 = new Bundle();
                bundle1.putString("userId", userId);

                // Tạo instance của fragment mới
                Fragment QRCodeFragment = new QRCodeFragment();
                QRCodeFragment.setArguments(bundle1);

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
        String id = SharedPreferenceLocal.read(getContext().getApplicationContext(), "userId");
        while (i < userFollowingList.size()) {
            if (id.equals(userFollowingList.get(i).getId())) {
                return true;
            }
            i++;
        }
        return false;
    }

    public void setTextBtn(Button btnFollow, String text) {
        btnFollow.setText(text);
        // Get the reference to the Drawable you want to assign
        Drawable drawable;
        if (text.equals("Following"))
            drawable = getContext().getResources().getDrawable(R.drawable.custom_buttom_selected_follows);
        else
            drawable = getContext().getResources().getDrawable(R.drawable.custom_button_background);
        // Assign Drawable to Button
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            // For Android versions below API 16
            btnFollow.setBackgroundDrawable(drawable);
        } else {
            // For Android versions from API 16 and above
            btnFollow.setBackground(drawable);
        }
    }

    private void handleGetQuantityFollows(String idFollows, String type) {
        followsViewModel.getQuantityFollows(idFollows).observe(getViewLifecycleOwner(), new Observer<ApiResponse<GetQuantityResponse>>() {
            @Override
            public void onChanged(ApiResponse<GetQuantityResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()) {
                    GetQuantityResponse getQuantityResponse = response.getData();
                    // update follower của người được theo dõi
                    if (type.equals("follower")) {
                        int countFollower = getQuantityResponse.getQuantityFollower() + 1;
                        RequestUpdateFollows requestUpdateFollowsByFollower = new RequestUpdateFollows(
                                getQuantityResponse.getId(), countFollower, getQuantityResponse.getQuantityFollowing()
                        );
                        handleUpdateFollow(requestUpdateFollowsByFollower);
                    } // update thông tin của người dùng hiện tại ( tăng số người đang theo dõi)
                    else if (type.equals("following")) {
                        int countFollowing = getQuantityResponse.getQuantityFollowing() + 1;
                        RequestUpdateFollows requestUpdateFollowsByFollower = new RequestUpdateFollows(
                                getQuantityResponse.getId(), getQuantityResponse.getQuantityFollower(), countFollowing
                        );
                        handleUpdateFollow(requestUpdateFollowsByFollower);
                    }
                }
            }
        });
    }

    private void handleUpdateFollow(RequestUpdateFollows requestUpdateFollows) {
        followsViewModel.updateFollows(requestUpdateFollows);
    }

    private void openDialogFollowing(int gravity, String userId, String userIdOther) {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_custom);
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }

        ImageView imageView = dialog.findViewById(R.id.img_avtUser);
        TextView name = dialog.findViewById(R.id.name);
        Button btnUnFollow = dialog.findViewById(R.id.btnUnFollow);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        name.setText(userResponse.getName());
        // Load hình ảnh từ URL bằng Glide
        Glide.with(getContext())
                .load(userResponse.getAvatarImg())
                .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                .into(imageView);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnUnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFollow(userId, userIdOther, dialog);
            }
        });
        dialog.show();
    }

    private void deleteFollow(String idFollower, String idFollowing, Dialog dialog) {
        followsViewModel.deleteFollows(idFollower, idFollowing).observe(getViewLifecycleOwner(), new Observer<ApiResponse<String>>() {
            @Override
            public void onChanged(ApiResponse<String> response) {
                if (response.getMessage().equals("Delete Success!") && response.getStatus()) {
                    handleUpdateQuantityFollows(idFollower, "following");
                    handleUpdateQuantityFollows(idFollowing, "follower");
                    //followBtn.setText("Follow");
                    setTextBtn(followBtn, "Follow");
                    dialog.dismiss();
                }
            }
        });
        ;
    }

    private void handleUpdateQuantityFollows(String idFollows, String type) {
        handleQuantityUpdate(idFollows, type, -1);
    }

    private void handleQuantityUpdate(String idFollows, String type, int quantityChange) {
        followsViewModel.getQuantityFollows(idFollows).observe(getViewLifecycleOwner(), new Observer<ApiResponse<GetQuantityResponse>>() {
            @Override
            public void onChanged(ApiResponse<GetQuantityResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()) {
                    GetQuantityResponse getQuantityResponse = response.getData();
                    int countFollower = getQuantityResponse.getQuantityFollower();
                    int countFollowing = getQuantityResponse.getQuantityFollowing();

                    if (type.equals("follower")) {
                        countFollower += quantityChange;
                    } else if (type.equals("following")) {
                        countFollowing += quantityChange;
                    }

                    RequestUpdateFollows requestUpdateFollowsByFollower = new RequestUpdateFollows(
                            getQuantityResponse.getId(), countFollower, countFollowing
                    );
                    handleUpdateFollow(requestUpdateFollowsByFollower);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Hiển thị nút "Back" trên ActionBar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Biến để kiểm tra xem hành động nào đã được thực hiện
                boolean actionExecuted = false;
                // Thử popBackStack
                try {
                    requireActivity().getSupportFragmentManager().popBackStack();
                    actionExecuted = true; // Gán cờ là true nếu popBackStack thành công
                } catch (IllegalStateException e) {
                    // Xử lý ngoại lệ nếu popBackStack không thành công
                    e.printStackTrace();
                }
                if (!actionExecuted || requireActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    requireActivity().finish();
                }
            }
        });

    }
}
