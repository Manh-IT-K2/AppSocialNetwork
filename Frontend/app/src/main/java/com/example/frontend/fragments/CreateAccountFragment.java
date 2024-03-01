package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.R;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountFragment extends Fragment {

    private EditText nameET, emailET, passwordET, confirmPassET;
    private TextView loginTV;
    private Button signUpBtn;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    public static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clickListener();
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

                if(name.isEmpty() || name.equals(" ")){
                    nameET.setError("Please input name");
                    return;
                }

                if(email.isEmpty() || !email.matches(EMAIL_REGEX)){
                    emailET.setError("Please input valid email");
                    return;
                }

                if(password.isEmpty()){
                    passwordET.setError("Please input valid password");
                    return;
                }

                if(!password.equals(confirmPassword)){
                    passwordET.setError("Password not match");
                    return;
                }

                //progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init(View view) {
        nameET = view.findViewById(R.id.nameET);
        emailET = view.findViewById(R.id.emailET);
        passwordET = view.findViewById(R.id.passwordET);
        confirmPassET = view.findViewById(R.id.confirmPassET);

        loginTV = view.findViewById(R.id.loginTV);

        signUpBtn = view.findViewById(R.id.signUpBtn);

        progressBar = view.findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
    }
}