package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;


public class ChangePasswordFragment extends Fragment {
    private Button SubmitBtn;
    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);

        // Tìm và gán trình nghe sự kiện cho nút "Verify"
        SubmitBtn = rootView.findViewById(R.id.changePassBtn);
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang fragment quên mật khẩu
                ((FragmentReplacerActivity) requireActivity()).setFragment(new LoginFragment());
            }
        });

        return rootView;
    }
}