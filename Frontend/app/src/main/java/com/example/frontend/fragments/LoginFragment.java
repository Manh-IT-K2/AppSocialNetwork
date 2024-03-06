package com.example.frontend.fragments;

import android.content.Intent;
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
import com.example.frontend.activities.MainActivity;
import com.example.frontend.R;
import com.example.frontend.request.User.RequestLogin;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.UserService;
import com.example.frontend.utils.CallApi;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginFragment extends Fragment {

    private UserViewModel userViewModel;
    private EditText emailET, passwordET;
    private TextView forgotTV, signUpTV;
    private Button loginBtn, googleSignInBtn;
    private ProgressBar progressBar;

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Khởi tạo Google Sign-In Client
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        init(view);
        clickListener();
    }

    private void clickListener() {
        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentReplacerActivity) getActivity()).setFragment(new CreateAccountFragment());
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if(!email.contains("@gmail.com")){
                    emailET.setError("Please input valid email");
                    return;
                }

                if(password.isEmpty()){
                    passwordET.setError("Please input valid password");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                userViewModel.login(new RequestLogin(email, "", "", password, false)).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
                    @Override
                    public void onChanged(ApiResponse<UserResponse> response) {
                        if(response.isStatus() == false)
                            Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        else{
                            SharedPreferenceLocal.save(getContext(), "userId", response.getData().getId());

                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                            getActivity().finish();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        forgotTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("LoginFragment", "Google sign in failed", e);
                Toast.makeText(requireContext(), "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            userViewModel.login(new RequestLogin(user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString(), "", true)).observe(getViewLifecycleOwner(), new Observer<ApiResponse<UserResponse>>() {
                                @Override
                                public void onChanged(ApiResponse<UserResponse> response) {
                                    if(response.isStatus()){
                                        SharedPreferenceLocal.save(getContext(), "userId", response.getData().getId());
                                        Toast.makeText(requireContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);

                                        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                        getActivity().finish();
                                    }else Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Log.w("LoginFragment", "signInWithCredential:failure", task.getException());
                            Toast.makeText(requireContext(), "Đăng nhập Firebase thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void init(View view) {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        emailET = view.findViewById(R.id.emailET);
        passwordET = view.findViewById(R.id.passwordET);
        forgotTV = view.findViewById(R.id.forgotTV);
        signUpTV = view.findViewById(R.id.signUpTV);
        loginBtn = view.findViewById(R.id.loginBtn);
        googleSignInBtn = view.findViewById(R.id.googleSignInBtn);
        progressBar = view.findViewById(R.id.progressBar);
    }
}