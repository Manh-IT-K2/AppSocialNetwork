package com.example.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.request.User.RequestChangePass;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.User.UserViewModel;


public class Function_change_password extends Fragment {
    ImageButton back_settingBtn;
    Button change_passBtn;
    private UserViewModel userViewModel;
    EditText currentpass, newpass, confirmpass;
    TextView err_data_null,err_confirm_pass,changed_mess;
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
        err_data_null = view.findViewById(R.id.err_data_null);
        err_confirm_pass = view.findViewById(R.id.err_confirm_pass);
        changed_mess = view.findViewById(R.id.success_mess);
        // Khởi tạo UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        back_settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment profile = new SettingFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_layout_main, profile);
                fragmentTransaction.addToBackStack(null); // Thêm transaction này vào stack back để quay trở lại fragment trước đó (nếu cần)
                fragmentTransaction.commit();
            }
        });
        change_passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
//                String usernameValue="";
//                if (bundle != null) {
//                    usernameValue  = bundle.getString("username");
//                }
                String id = SharedPreferenceLocal.read(getContext(), "userId");

                String current = currentpass.getText().toString();
                String new_p = newpass.getText().toString();
                String confirm = confirmpass.getText().toString();
               // Toast.makeText(getContext(), usernameValue, Toast.LENGTH_LONG).show();
                if (current.isEmpty() || new_p.isEmpty() || confirm.isEmpty()) {
                    err_data_null.setText("Please fill in all required fields.");
                    changed_mess.setText("");
                    err_confirm_pass.setText("");
                }else if(!new_p.equals(confirm)){
                    err_confirm_pass.setText("Passwords do not match. Please try again.");
                    err_data_null.setText("");
                    changed_mess.setText("");
                }
                 else {
                    userViewModel.changePass(new RequestChangePass( id, current, new_p)).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
                        @Override
                        public void onChanged(ApiResponse<UserResponse> userResponseApiResponse) {
                            if (userResponseApiResponse.isStatus() == false) {
                                err_confirm_pass.setText(userResponseApiResponse.getMessage());
                                changed_mess.setText("");
                                err_data_null.setText("");
                               // Toast.makeText(getContext(), userResponseApiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                changed_mess.setText("Password changed");
                                err_confirm_pass.setText("");
                                err_data_null.setText("");
                            }
                        }
                    });
                    }

            }
        });

        return view;
    }
}
