package com.example.frontend.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.request.Post.RequestCreatePost;
import com.example.frontend.utils.CameraX;
import com.example.frontend.utils.FirebaseStorageUploader;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {

    private ImageView img_createPost;
    private Button btn_sharePost;
    private EditText edt_description;
    private ImageView btn_backCreatePost;
    private PostViewModel postViewModel;
    private List<Uri> selectedFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        img_createPost = findViewById(R.id.img_createPost);
        btn_sharePost = findViewById(R.id.btn_sharePost);
        btn_backCreatePost = findViewById(R.id.btn_backCreatePost);
        edt_description = findViewById(R.id.edt_description);

        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");
        selectedFiles = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String imagePath = extras.getString("imagePath");
            if (imagePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                if (bitmap != null) {
                    img_createPost.setImageBitmap(bitmap);
                    selectedFiles.add(Uri.parse(imagePath));
                } else {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        btn_sharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị Dialog progress
                final Dialog dialog = new Dialog(CreatePostActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_progress_bar);
                dialog.setCancelable(false);
                dialog.show();
                if (img_createPost != null) {
                    Bitmap bitmap = ((BitmapDrawable) img_createPost.getDrawable()).getBitmap();

                    uploadFilesToFirebaseStorage(bitmap, new FirebaseStorageUploader.OnUploadCompleteListener() {
                        @Override
                        public void onUploadComplete(String fileUrl) {
                            String description = edt_description.getText().toString();
                            Date createAt = new Date();

                            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                            String isoDateString = isoFormat.format(createAt);

                            RequestCreatePost requestCreatePost = new RequestCreatePost(fileUrl, "65e8a525714ccc3a3caa7f77", description, "", isoDateString);

                            postViewModel.createPost(requestCreatePost, "65e8a525714ccc3a3caa7f77");

                            DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
                            String postId = postsRef.push().getKey();
                            String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");

                            Map<String, Object> postData = new HashMap<>();
                            postData.put("imageUrl", fileUrl);
                            postData.put("userId", "65e8a525714ccc3a3caa7f77");
                            postData.put("description", description);
                            postData.put("createdAt", isoDateString);

                            postsRef.child(postId).setValue(postData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "Post created successfully in Realtime Database");
                                    startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
                                    // Đóng Dialog khi tác vụ hoàn thành
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("TAG", "Failed to create post in Realtime Database: " + e.getMessage());
                                }
                            });
                            // Đóng Dialog khi tác vụ hoàn thành
                            dialog.dismiss();

                        }

                        @Override
                        public void onUploadFailed(String errorMessage) {
                            Toast.makeText(CreatePostActivity.this, "Failed to upload image: " + errorMessage, Toast.LENGTH_SHORT).show();
                            // Đóng Dialog khi tác vụ hoàn thành
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(CreatePostActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                    // Đóng Dialog khi tác vụ hoàn thành
                    dialog.dismiss();
                }
            }
        });

        btn_backCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, CameraX.class);
        startActivity(intent);
    }

    private void uploadFilesToFirebaseStorage(Bitmap bitmap, final FirebaseStorageUploader.OnUploadCompleteListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://noteapp-f20f4.appspot.com");
        StorageReference storageRef = storage.getReference();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        String fileName = "temp_image_" + timestamp + ".jpg";

        StorageReference fileRef = storageRef.child("uploadFiles/" + fileName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = fileRef.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        String fileUrl = downloadUri.toString();
                        Log.d("TAG success", "File uploaded successfully. URL: " + fileUrl);
                        listener.onUploadComplete(fileUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("TAG Err", "File upload failed: " + exception.getMessage());
                listener.onUploadFailed(exception.getMessage());
            }
        });
    }
}
