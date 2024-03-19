package com.example.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.request.User.RequestChangePW;
import com.example.frontend.request.User.RequestChangePass;
import com.example.frontend.viewModel.User.UserViewModel;


public class ChangePasswordFragment extends Fragment {
    Button submitBtn;
    ImageButton backBtn;
    EditText newpass, confirmpass;
    String email="";
    private UserViewModel userViewModel;
    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        submitBtn = view.findViewById(R.id.changePassBtn);
        backBtn= view.findViewById(R.id.back_btn);
        newpass = view.findViewById(R.id.newPass);
        confirmpass = view.findViewById(R.id.confirmnewPass);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FragmentReplacerActivity.class);
                // Thêm dữ liệu cho Intent để FragmentReplacerActivity biết cần thay thế fragment nào
                intent.putExtra("fragment_to_load", "create_new_pass");
                // Bắt đầu activity với Intent đã tạo
                startActivity(intent);
            }
        });

        // Nhận dữ liệu email từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString("email", "");
        }
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_p = newpass.getText().toString();
                String confirm = confirmpass.getText().toString();
                if (new_p.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else if(!new_p.equals(confirm)){
                    Toast.makeText(getContext(), "Cofirm mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }else {
                    userViewModel.changePW(new RequestChangePW(email, new_p));
                }
            }
        });
        return view;
    }
}