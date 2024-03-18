package com.example.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.request.User.RequestChangePass;
import com.example.frontend.viewModel.User.UserViewModel;


public class Function_change_password extends Fragment {
    ImageButton back_settingBtn;
    Button change_passBtn;
    private UserViewModel userViewModel;
    EditText currentpass, newpass, confirmpass;

    public Function_change_password() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_function_change_password, container, false);
        back_settingBtn = view.findViewById(R.id.back_btn);
        change_passBtn = view.findViewById(R.id.changePassBtn);
        currentpass = view.findViewById(R.id.currentPass);
        newpass = view.findViewById(R.id.newPass);
        confirmpass = view.findViewById(R.id.confirmnewPass);

        // Khởi tạo UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        back_settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FragmentReplacerActivity.class);
                intent.putExtra("fragment_to_load", "back_frame_setting");
                startActivity(intent);
            }
        });

        change_passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current = currentpass.getText().toString();
                String new_p = newpass.getText().toString();
                String confirm = confirmpass.getText().toString();
                if (current.isEmpty() || new_p.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    userViewModel.changePass(new RequestChangePass("myylyy12@gmail.com", "myly123", new_p));
                }
            }
        });

        return view;
    }
}
