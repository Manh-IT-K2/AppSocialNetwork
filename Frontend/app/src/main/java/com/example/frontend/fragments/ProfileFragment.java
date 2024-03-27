package com.example.frontend.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.activities.CreatePostActivity;
import com.example.frontend.activities.FollowsActivity;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.request.User.RequestUpdateUser;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.FirebaseStorageUploader;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    ImageButton editprofileImageBtn,menuSetting;
    Button editprofileBtn, logoutBtn, qrcodeBtn;
    TextView username,followingCount,followerCount,postCount;
    LinearLayout openFollows,openFollowing;
    UserViewModel userViewModel;
    String userId;
    CircleImageView profileImage;
    TextView nameTV;
    private List<Uri> selectedFiles;

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

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Nhận dữ liệu email từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null){
            userId = bundle.getString("userId", "");
            qrcodeBtn.setVisibility(View.INVISIBLE);
            logoutBtn.setVisibility(View.INVISIBLE);
            menuSetting.setVisibility(View.INVISIBLE);
            editprofileBtn.setVisibility(View.INVISIBLE);
        }
        else userId = SharedPreferenceLocal.read(getContext(),"userId");
        userViewModel.getDetailUserById(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Success") && response.getStatus()){
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
                // Bắt đầu activity với Intent đã tạo
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceLocal.save(getContext(),"userId", "");
                Intent intent = new Intent(getActivity (), FragmentReplacerActivity.class);
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
                settingFragment.setArguments(bundle); // Thiết lập arguments cho SettingFragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1  && resultCode == RESULT_OK){
            selectedFiles = new ArrayList<>();
            if(data != null && data.getData() != null) {
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
            String fileName = "file_" + timestamp+ "_"+ new File(fileUri.getPath()).getName() + ".jpg";
            FirebaseStorageUploader.uploadFileToFirebaseStorage(fileUri, fileName, new FirebaseStorageUploader.OnUploadCompleteListener() {
                @Override
                public void onUploadComplete(List<String> fileUrls) {

                }

                @Override
                public void onUploadComplete(String fileUrl) {
                    uploadedFiles[0]++;
                    if (uploadedFiles[0] == totalFiles) {
                        RequestUpdateUser requestUpdateUser = new RequestUpdateUser(
                                userId, "","","",fileUrl,"","","",""
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

    private void updateUser (RequestUpdateUser requestUpdateUser){
        userViewModel.updateUser(requestUpdateUser).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if (response.getMessage().equals("Update Success!") && response.getStatus()){
                    UserResponse userResponse = response.getData();
                    Picasso.get().load(userResponse.getAvatarImg()).into(profileImage);
                    Toast.makeText(getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });;
    }
}