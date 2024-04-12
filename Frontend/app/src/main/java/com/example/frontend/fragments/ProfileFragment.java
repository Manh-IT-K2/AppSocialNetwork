package com.example.frontend.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.ChatActivity;
import com.example.frontend.activities.CreatePostActivity;
import com.example.frontend.activities.FollowsActivity;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.adapter.PostAdapter;
import com.example.frontend.request.Follows.RequestCreateFollows;
import com.example.frontend.request.Follows.RequestUpdateFollows;
import com.example.frontend.request.Notification.Notification;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.request.User.RequestUpdateUser;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Follows.GetQuantityResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.NotificationService;
import com.example.frontend.utils.FirebaseStorageUploader;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    ImageButton editprofileImageBtn, menuSetting;
    Button editprofileBtn, logoutBtn, qrcodeBtn;
    Button followBtn, messageBtn, QRCodeBtn;
    TextView username, followingCount, followerCount, postCount;
    LinearLayout openFollows, openFollowing;
    LinearLayout followLayout, follow_QRCode_messageLayout, menu_downLayout, menuLayout;
    UserViewModel userViewModel;
    FollowsViewModel followsViewModel;
    private List<UserResponse> userFollowingList = new ArrayList<>();
    String userId;
    CircleImageView profileImage;
    TextView nameTV;
    private List<Uri> selectedFiles;
    Fragment selectedFragment = null;
    FrameLayout fragment_layout_main;
    BottomNavigationView bottomNavigationView;
    private UserResponse userResponse;
    Toolbar toolbar;

    @Override
    public void onResume() {
        super.onResume();
        if (userId.equals(SharedPreferenceLocal.read(getContext(), "userId"))) {
            follow_QRCode_messageLayout.setVisibility(View.GONE);
        } else {
            followLayout.setVisibility(View.GONE);
            menu_downLayout.setVisibility(View.GONE);
            menuLayout.setVisibility(View.GONE);
            followLayout.setVisibility(View.GONE);
            editprofileImageBtn.setVisibility(View.GONE);
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
        }

        userViewModel.getDetailUserById(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logoutBtn = view.findViewById(R.id.logout);
        qrcodeBtn = view.findViewById(R.id.qrcodeBtn);
        username = view.findViewById(R.id.toolbarNameTV);
        followingCount = view.findViewById(R.id.followingCount);
        followerCount = view.findViewById(R.id.followerCount);
        postCount = view.findViewById(R.id.postCount);
        openFollows = view.findViewById(R.id.openFollows);
        openFollowing = view.findViewById(R.id.openFollowing);
        editprofileBtn = view.findViewById(R.id.edit_profileBtn);
        editprofileImageBtn = view.findViewById(R.id.edit_profileImage);
        menuSetting = view.findViewById(R.id.menu_btn);
        profileImage = view.findViewById(R.id.profileImage);
        nameTV = view.findViewById(R.id.nameTV);
        followBtn = view.findViewById(R.id.followBtn);
        QRCodeBtn = view.findViewById(R.id.QRCodeBtn);
        messageBtn = view.findViewById(R.id.messageBtn);
        followLayout = view.findViewById(R.id.followLayout);
        follow_QRCode_messageLayout = view.findViewById(R.id.follow_QRCode_message_Layout);
        menu_downLayout = view.findViewById(R.id.menu_downLayout);
        menuLayout = view.findViewById(R.id.menuLayout);
        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        fragment_layout_main = view.findViewById(R.id.fragment_layout_main);
        toolbar = view.findViewById(R.id.toolbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemReselectedListener);
        bottomNavigationView.setSelectedItemId(R.id.menu_Posts);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        followsViewModel = new ViewModelProvider(this).get(FollowsViewModel.class);

        // Nhận dữ liệu email từ Bundle
//        Bundle bundle = getArguments();
//        if (bundle.getString("userId") != null){
//            userId = bundle.getString("userId", "");
//            qrcodeBtn.setVisibility(View.INVISIBLE);
//            logoutBtn.setVisibility(View.INVISIBLE);
//            menuSetting.setVisibility(View.INVISIBLE);
//            editprofileBtn.setVisibility(View.INVISIBLE);
//        } else if (bundle.getString("userIdLiked") != null) {
//            userId = bundle.getString("userIdLiked");
//        } else userId = SharedPreferenceLocal.read(getContext(),"userId");
        userId = SharedPreferenceLocal.read(getContext(), "userId");
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("userId") != null) {
                userId = bundle.getString("userId", "");
            }
        }
        Log.e("iddau", userId);

        userViewModel.getDetailUserById(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()) {
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


        handleClickListener();

        return view;
    }

    public void handleClickListener() {
        editprofileImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/* video/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 1);
            }
        });

        openFollows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowsActivity.class);
                // Thêm dữ liệu cho Intent để FragmentReplacerActivity biết cần thay thế fragment nào
                intent.putExtra("fragment_to_load", "openFollows");
                intent.putExtra("userName", username.getText().toString());
                if (!userId.equals(SharedPreferenceLocal.read(getContext(), "userId")))
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
                if (!userId.equals(SharedPreferenceLocal.read(getContext(), "userId")))
                    intent.putExtra("userId", userId);
                // Bắt đầu activity với Intent đã tạo
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceLocal.save(getContext(), "userId", "");
                Intent intent = new Intent(getActivity(), FragmentReplacerActivity.class);
                startActivity(intent);
            }
        });

        editprofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                editProfileFragment.setArguments(bundle); // Thiết lập arguments cho SettingFragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_layout_main, editProfileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        menuSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue = username.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameValue);
                SettingFragment settingFragment = new SettingFragment();
                settingFragment.setArguments(bundle);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                // Sử dụng animation slide_in_left khi thay đổi Fragment
                transaction.setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_in_left);

                transaction.replace(R.id.fragment_layout_main, settingFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        qrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo instance của fragment mới
                Fragment QRCodeFragment = new QRCodeFragment();

                // Lấy instance của FragmentManager
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Bắt đầu transaction để thay thế fragment hiện tại bằng fragment mới
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_layout_main, QRCodeFragment);
                fragmentTransaction.addToBackStack(null); // Thêm transaction này vào stack back để quay trở lại fragment trước đó (nếu cần)
                fragmentTransaction.commit();
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

                    Notification notification = new Notification();
                    notification.setUserId(SharedPreferenceLocal.read(getContext(), "userId"));
                    String userName = SharedPreferenceLocal.read(getContext(), "userName");
                    notification.setText(userName + " vừa theo dõi bạn");
                    notification.setIdRecipient(userId);

                    NotificationService.sendNotification(getContext(), notification.getText(), userResponse.getTokenFCM());

                    userViewModel.addNotification(notification);

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

        QRCodeBtn.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selectedFiles = new ArrayList<>();
            if (data != null && data.getData() != null) {
                Uri fileUri = data.getData();
                selectedFiles.add(fileUri);
                uploadFilesToFirebaseStorage();
            }
        }
    }

    private void uploadFilesToFirebaseStorage() {
        int totalFiles = selectedFiles.size();
        final int[] uploadedFiles = {0};

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());

        for (int i = 0; i < selectedFiles.size(); i++) {
            Uri fileUri = selectedFiles.get(i);
            String fileName = "file_" + timestamp + "_" + new File(fileUri.getPath()).getName() + ".jpg";
            FirebaseStorageUploader.uploadFileToFirebaseStorage(fileUri, fileName, new FirebaseStorageUploader.OnUploadCompleteListener() {
                @Override
                public void onUploadComplete(List<String> fileUrls) {

                }

                @Override
                public void onUploadComplete(String fileUrl) {
                    uploadedFiles[0]++;
                    if (uploadedFiles[0] == totalFiles) {
                        RequestUpdateUser requestUpdateUser = new RequestUpdateUser(
                                userId, "", "", "", fileUrl, "", "", "", ""
                        );
                        updateUser(requestUpdateUser);
                    }
                }

                @Override
                public void onUploadFailed(String errorMessage) {
                    Toast.makeText(getContext(), "Upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateUser(RequestUpdateUser requestUpdateUser) {
        userViewModel.updateUser(requestUpdateUser).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Update Success!") && response.getStatus()) {
                    UserResponse userResponse = response.getData();
                    Picasso.get().load(userResponse.getAvatarImg()).into(profileImage);
                    Toast.makeText(getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemReselectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.menu_Posts) {
                selectedFragment = new PostProfileFragment();
            } else if (item.getItemId() == R.id.menu_Watch) {
                selectedFragment = new WatchProfileFragment();
            } else if (item.getItemId() == R.id.menu_YouLiked) {
                selectedFragment = new SubscriptionsFragment();
            }
            if (selectedFragment != null) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_layout_main, selectedFragment).commit();
            }
            return true;
        }
    };

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        if (fragment instanceof CreateAccountFragment) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(fragment_layout_main.getId(), fragment);
        fragmentTransaction.commit();
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
        // Neu profile cua user khong phai user da dang nhap
        // Hiển thị nút "Back" trên ActionBar
        if (!userId.equals(SharedPreferenceLocal.read(getContext(), "userId"))) {
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
        else toolbar.setNavigationIcon(null);
    }


}
