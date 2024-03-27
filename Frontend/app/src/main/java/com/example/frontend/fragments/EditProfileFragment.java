package com.example.frontend.fragments;

import static android.app.Activity.RESULT_OK;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.adapter.PostAdapter;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.request.User.RequestUpdateUser;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.FirebaseStorageUploader;
import com.example.frontend.viewModel.Story.StoryViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.Console;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EditProfileFragment extends Fragment {
    Button doneBtn,cancelBtn,editBtn,edit_profile_photo_Btn;
    EditText usernameEditText,websiteEditText,bioEditText,nameEditText,editTextEmail,editTextPhone,editTextGender;
    ImageView profileImage;
    UserViewModel userViewModel;
    private Uri selectedFile;
    String userID;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        doneBtn = view.findViewById(R.id.Done_btn);
        cancelBtn = view.findViewById(R.id.Cancel_btn);
        editBtn = view.findViewById(R.id.editBtn);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        websiteEditText = view.findViewById(R.id.websiteEditText);
        bioEditText = view.findViewById(R.id.bioEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextGender = view.findViewById(R.id.editTextGender);
        profileImage = view.findViewById(R.id.profileImage);
        edit_profile_photo_Btn = view.findViewById(R.id.edit_profile_photo_Btn);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userID = getArguments().getString("userId");

        // HANDLE CHOOSE IMAGE
        edit_profile_photo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/* video/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 1);
            }
        });

        // HANDLE BTN DONE
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity (), MainActivity.class);
                intent.putExtra("fragment_to_load", "edit_profile_done");
                startActivity(intent);
            }
        });

        // HANDLE BTN CANCEL
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity (), MainActivity.class);
                intent.putExtra("fragment_to_load", "edit_profile_cancel");
                startActivity(intent);
            }
        });
        // HANDLE BTN EDIT PROFILE
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = usernameEditText.getText().toString();
                String website = websiteEditText.getText().toString();
                String bio = bioEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String email = editTextEmail.getText().toString();
                String phone = editTextPhone.getText().toString();
                String gender = editTextGender.getText().toString();
                uploadFilesToFirebaseStorage(userName,website,bio,name,email,phone,gender);
            }
        });
        // HANDLE GET DETAIL USER BY ID
        getDetailUserById(userID);
        return view;
    }
    // Phương thức giúp thiết lập giá trị của EditText
    private void setEditTextValue(EditText editText, String value, String defaultValue) {
        if(value != null && !value.isEmpty()){
            editText.setText(value);
        } else {
            editText.setHint(defaultValue);
        }
    }

    // Phương thức giúp tải ảnh bằng Glide
    private void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : R.drawable.logo)
                .placeholder(R.drawable.logo) // Ảnh thay thế khi đang load
                .error(R.drawable.logo) // Ảnh thay thế khi có lỗi
                .into(imageView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1  && resultCode == RESULT_OK){
            if(data != null && data.getData() != null) {
                Uri fileUri = data.getData();
                selectedFile = fileUri;
                loadImage(profileImage, String.valueOf(fileUri));
            }
        }
    }


    // HANDLE UPLOAD TO FIREBASE STORAGE
    private void uploadFilesToFirebaseStorage(String userName, String website, String bio, String name, String email, String phone, String gender) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        Uri fileUri = selectedFile;
        String fileName = "file_" + timestamp+ "_"+ new File(fileUri.getPath()).getName() + ".jpg";
        FirebaseStorageUploader.uploadFileToFirebaseStorage(fileUri, fileName, new FirebaseStorageUploader.OnUploadCompleteListener() {
            @Override
            public void onUploadComplete(String fileUrl) {
                if( !fileUrl.isEmpty()) {
                    RequestUpdateUser requestUpdateUser = new RequestUpdateUser(
                            userID,
                            !userName.isEmpty() ? userName : "",
                            !email.isEmpty() ? email : "",
                            !phone.isEmpty() ? phone : "",
                            fileUrl,
                            !bio.isEmpty() ? bio : "",
                            !website.isEmpty() ? website : "",
                            !gender.isEmpty() ? gender : "",
                            !name.isEmpty() ? name : ""
                    );
                    updateUser(requestUpdateUser);
                };
            }
            @Override
            public void onUploadFailed(String errorMessage) {
                Toast.makeText(getContext(), "Upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // HANDLE UPDATE USER
    private void updateUser(RequestUpdateUser requestUpdateUser){
        userViewModel.updateUser(requestUpdateUser).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                if(response.getStatus() && response.getMessage().equals("Update Success!")){
                    Toast.makeText(getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // HANDLE GET DETAIL USER BY ID
    private void getDetailUserById (String userID) {
        userViewModel.getDetailUserById(userID).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
            @Override
            public void onChanged(ApiResponse<UserResponse> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                if (response.getData() != null && response.getMessage().equals("Success") && response.getStatus()) {
                    UserResponse userResponse = response.getData();
                    setEditTextValue(usernameEditText, userResponse.getUsername(), "Enter username");
                    setEditTextValue(editTextEmail, userResponse.getEmail(), "Enter email");
                    setEditTextValue(editTextPhone, userResponse.getPhoneNumber(), "Enter phone");
                    setEditTextValue(websiteEditText, userResponse.getWebsite(), "Enter website");
                    setEditTextValue(bioEditText, userResponse.getBio(), "Enter bio");
                    setEditTextValue(nameEditText, userResponse.getName(), "Enter name");
                    setEditTextValue(editTextGender, userResponse.getGender(), "Enter gender");
                    loadImage(profileImage, userResponse.getAvatarImg());
                } else {
                    // Xử lý khi không có dữ liệu hoặc có lỗi
                }
            }
        });
    }
}
