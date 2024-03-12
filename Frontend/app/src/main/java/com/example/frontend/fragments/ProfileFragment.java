package com.example.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.utils.SharedPreferenceLocal;

public class ProfileFragment extends Fragment {
    AppCompatButton editprofileBtn, logoutBtn, qrcodeBtn;

    // Button logoutBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logoutBtn = view.findViewById(R.id.logout);
        qrcodeBtn = view.findViewById(R.id.qrcodeBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceLocal.save(getContext(),"userId", "");
                Intent intent = new Intent(getActivity (), FragmentReplacerActivity.class);
                startActivity(intent);
            }
        });


        editprofileBtn = view.findViewById(R.id.edit_profileBtn);
        editprofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity (), FragmentReplacerActivity.class);
                startActivity(intent);

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
}