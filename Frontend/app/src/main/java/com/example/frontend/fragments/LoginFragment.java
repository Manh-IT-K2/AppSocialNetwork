package com.example.frontend.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.request.User.RequestLogin;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.FirebaseStorageUploader;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginFragment extends Fragment {

    private UserViewModel userViewModel;
    private EditText emailET, passwordET;
    private TextView forgotTV, signUpTV;
    private Button loginBtn, googleSignInBtn, chooseFileButton, uploadToFirebase;
    private ProgressBar progressBar;

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private List<Uri> selectedFiles;
    private List<String> urlFromFirebase;

    LinearLayout linear_layout_image_container;


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
        selectedFiles = new ArrayList<>();
        urlFromFirebase = new ArrayList<>();

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
                String email = emailET.getText().toString().trim();

                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailET.setError("Please enter a valid email address");
                    return;
                }

                // Tạo một Bundle để đóng gói dữ liệu email
                Bundle bundle = new Bundle();
                bundle.putString("email", email);

                // Tạo Fragment mới và gắn Bundle vào đó
                VerificationCodeFragment verificationCodeFragment = new VerificationCodeFragment();
                verificationCodeFragment.setArguments(bundle);

                // Chuyển sang fragment xác minh mã và truyền địa chỉ email
                ((FragmentReplacerActivity) requireActivity()).setFragment(verificationCodeFragment);
            }
        });

        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/* video/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 1);
            }
        });

        uploadToFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFiles.size() == 0) Toast.makeText(getContext(), "Chưa chọn file", Toast.LENGTH_SHORT).show();
                else{
                    uploadFilesToFirebaseStorage();
                }
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
        }else if(requestCode == 1  && resultCode == RESULT_OK){
            selectedFiles = new ArrayList<>();
            urlFromFirebase = new ArrayList<>();

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    selectedFiles.add(fileUri);
                }
                loadImage();
            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                selectedFiles.add(fileUri);
                loadImage();
            }
        }
    }

    private void loadImage(){
        linear_layout_image_container.removeAllViews();
        for (Uri imageUrl : selectedFiles) {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 16, 0); // Cài đặt khoảng cách giữa các ImageView
            imageView.setLayoutParams(layoutParams);

            Picasso.get()
                    .load(imageUrl)
                    .into(imageView);

            linear_layout_image_container.addView(imageView);
        }
    }

    private void uploadFilesToFirebaseStorage() {
        int totalFiles = selectedFiles.size();
        final int[] uploadedFiles = {0};

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());

        for (int i = 0; i < selectedFiles.size(); i++) {
            Uri fileUri = selectedFiles.get(i);
            String fileName = "file_" + timestamp+ "_"+ new File(fileUri.getPath()).getName() + ".jpg";
            FirebaseStorageUploader.uploadFileToFirebaseStorage(fileUri, fileName, new FirebaseStorageUploader.OnUploadCompleteListener() {
                @Override
                public void onUploadComplete(String fileUrl) {
                    urlFromFirebase.add(fileUrl);
                    uploadedFiles[0]++;

                    if (uploadedFiles[0] == totalFiles) {
                        Gson gson = new Gson();
                        String str = gson.toJson(urlFromFirebase);
                        Log.d("check", str);
                        Toast.makeText(getContext(), "Upload Successfully", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onUploadFailed(String errorMessage) {
                    Toast.makeText(getContext(), "Upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
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

        chooseFileButton = view.findViewById(R.id.chooseFileButton);
        linear_layout_image_container = view.findViewById(R.id.linear_layout_image_container);
        uploadToFirebase = view.findViewById(R.id.uploadToFirebase);
    }
}