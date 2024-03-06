package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.R;
import com.example.frontend.request.User.RequestCreateAccount;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountFragment extends Fragment {
    private UserViewModel userViewModel;

    private EditText nameET, emailET, passwordET, confirmPassET, otpET;
    private TextView loginTV;
    private Button signUpBtn, sendOTPBtn;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String checkOTP;

    public static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    public CreateAccountFragment() {
        Log.d("abc1","check1");
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("check","ok");
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        init(view);
        clickListener();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }


    private void clickListener() {
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentReplacerActivity) getActivity()).setFragment(new LoginFragment());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String confirmPassword = confirmPassET.getText().toString();
                String otp = otpET.getText().toString();

                if(name.isEmpty() || name.equals(" ")){
                    nameET.setError("Please input name");
                    return;
                }

//                if(email.isEmpty() || !email.matches(EMAIL_REGEX)){
//                    emailET.setError("Please input valid email");
//                    return;
//                }

                if(password.isEmpty()){
                    passwordET.setError("Please input valid password");
                    return;
                }

                if(!password.equals(confirmPassword)){
                    passwordET.setError("Password not match");
                    return;
                }

                if(otp.isEmpty()){
                    otpET.setError("Please input otp");
                    return;
                }

//                if(!otp.equals(checkOTP) && !checkOTP.isEmpty()){
//                    Toast.makeText(getActivity(), "OTP is invalid", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                progressBar.setVisibility(View.VISIBLE);

                userViewModel.registerUser(new RequestCreateAccount(name, email,password));
                Toast.makeText(getActivity(), "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                ((FragmentReplacerActivity) getActivity()).setFragment(new LoginFragment());
            }
        });

        sendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                if(email.isEmpty() || !email.matches(EMAIL_REGEX)){
                    emailET.setError("Please input valid email");
                    return;
                }
            }
        });
    }

    private void init(View view) {
        nameET = view.findViewById(R.id.nameET);
        emailET = view.findViewById(R.id.emailET);
        passwordET = view.findViewById(R.id.passwordET);
        confirmPassET = view.findViewById(R.id.confirmPassET);
        otpET = view.findViewById(R.id.otpET);

        loginTV = view.findViewById(R.id.loginTV);

        signUpBtn = view.findViewById(R.id.signUpBtn);
        sendOTPBtn = view.findViewById(R.id.sendOTPBtn);

        progressBar = view.findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
    }
}