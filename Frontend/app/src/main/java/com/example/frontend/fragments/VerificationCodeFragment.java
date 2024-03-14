package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;

public class VerificationCodeFragment extends Fragment {

    private Button verifyBtn;

    public VerificationCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_verification_code, container, false);

        // Tìm và gán trình nghe sự kiện cho nút "Verify"
        verifyBtn = rootView.findViewById(R.id.verifyBtn);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang fragment quên mật khẩu
                ((FragmentReplacerActivity) requireActivity()).setFragment(new ChangePasswordFragment());
            }
        });

        return rootView;
    }
}
