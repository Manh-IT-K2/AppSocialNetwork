package com.example.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

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

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.adapter.PostAdapter;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.request.User.RequestUpdateUser;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.viewModel.Story.StoryViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;

import java.io.Console;
import java.util.List;


public class EditProfileFragment extends Fragment {
    Button doneBtn,cancelBtn,editBtn;
    EditText usernameEditText,websiteEditText,bioEditText,nameEditText,editTextEmail,editTextPhone,editTextGender;
    ImageView profileImage;

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

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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
                RequestUpdateUser requestUpdateUser = new RequestUpdateUser(
                        "65e8a525714ccc3a3caa7f77",
                        !userName.isEmpty() ? userName : "",
                        !email.isEmpty() ? email : "",
                        !phone.isEmpty() ? phone : "",
                        "",
                        !bio.isEmpty() ? bio : "",
                        !website.isEmpty() ? website : "",
                        !gender.isEmpty() ? gender : "",
                        !name.isEmpty() ? name : ""
                );
                userViewModel.updateUser(requestUpdateUser).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
                    @Override
                    public void onChanged(ApiResponse<UserResponse> response) {
                        Gson gson = new Gson();
                        String json = gson.toJson(response);
                        Log.d("Update",json);
                    }
                });
            }
        });
        // HANDLE GET DETAIL USER BY ID
        userViewModel.getDetailUserById("65e8a525714ccc3a3caa7f77").observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
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
}
