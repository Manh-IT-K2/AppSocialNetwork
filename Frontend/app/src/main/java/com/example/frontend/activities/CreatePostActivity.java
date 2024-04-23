package com.example.frontend.activities;

import static com.example.frontend.activities.PostActivity.checkDataIntent;
import static com.example.frontend.activities.PostActivity.checkScreen;
import static com.example.frontend.activities.PostActivity.chooseMoreClickCount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.frontend.R;
import com.example.frontend.request.Notification.Notification;
import com.example.frontend.request.Post.RequestCreatePost;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Post.ResponseCreatePost;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.service.NotificationService;
import com.example.frontend.utils.CameraX;
import com.example.frontend.utils.CustomSlideModel;
import com.example.frontend.utils.FirebaseStorageUploader;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CreatePostActivity extends AppCompatActivity {

    private ImageView img_createPost;
    private Button btn_sharePost;
    private EditText edt_description;
    private ImageView btn_backCreatePost;
    private PostViewModel postViewModel;
    private UserViewModel userViewModel;
    private List<String> selectedFileChoseMore;
    private LinearLayout linear_layout_drag_createPost;
    private ImageSlider image_sliderCreatePost;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        image_sliderCreatePost = findViewById(R.id.image_sliderCreatePost);
        btn_sharePost = findViewById(R.id.btn_sharePost);
        btn_backCreatePost = findViewById(R.id.btn_backCreatePost);
        edt_description = findViewById(R.id.edt_description);
        //linear_layout_drag_createPost = findViewById(R.id.linear_layout_drag_createPost);

        userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");
        selectedFileChoseMore = new ArrayList<>();

        if (checkDataIntent == 0) {
            // get image take a shot camera
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String imagePath = extras.getString("imagePath");
                if (imagePath != null) {
                    selectedFileChoseMore.add(imagePath);
                    File file = new File(imagePath);
                    Uri uri = Uri.fromFile(file);
                    String imgUrl = uri.toString();
                    ArrayList<SlideModel> slideModels = new ArrayList<>();
                    slideModels.add(new SlideModel(imgUrl, ScaleTypes.CENTER_CROP));
                    image_sliderCreatePost.setImageList(slideModels);
                } else {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }

            }
        } else if (checkDataIntent == 1) {
            //
            selectedFileChoseMore = getIntent().getStringArrayListExtra("selectedImages");
            if (selectedFileChoseMore != null && !selectedFileChoseMore.isEmpty()) {
                //loadImageSelectedCreatePost();
                File file;
                Uri uri;
                String imageUrl;
                ArrayList<SlideModel> slideModels = new ArrayList<>();
                for (int i = 0; i <= selectedFileChoseMore.size() - 1; i++) {
                    file = new File(selectedFileChoseMore.get(i));
                    uri = Uri.fromFile(file);
                    imageUrl = uri.toString();
                    slideModels.add(new SlideModel(imageUrl, ScaleTypes.CENTER_CROP));
                    Log.e("selectedFile", String.valueOf(selectedFileChoseMore.get(i)));
                }
                image_sliderCreatePost.setImageList(slideModels);
            } else {
                Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Get the file path from intent extras
            String imagePath = getIntent().getStringExtra("imagePaths");
            if (imagePath != null) {
                selectedFileChoseMore.add(imagePath);
                // Check if the file exists
                File imageFile = new File(imagePath);
                Uri uri = Uri.fromFile(imageFile);
                String imgUrl = uri.toString();
                ArrayList<SlideModel> slideModels = new ArrayList<>();
                slideModels.add(new SlideModel(imgUrl, ScaleTypes.CENTER_CROP));
                image_sliderCreatePost.setImageList(slideModels);
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        }

        // init post view model
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // action click btn_sharePost
        btn_sharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CreatePostActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_progress_bar);
                dialog.setCancelable(false);
                dialog.show();

                // Counter to keep track of successful uploads
                AtomicInteger uploadCounter = new AtomicInteger(0);

                // Tạo danh sách các URL của các file đã chọn
                List<String> fileUrls = new ArrayList<>();
                List<Bitmap> bitmaps = new ArrayList<>();
                for (String imagePath : selectedFileChoseMore) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    bitmaps.add(bitmap);
                }

                uploadFilesToFirebaseStorage(bitmaps, new FirebaseStorageUploader.OnUploadCompleteListener() {
                    @Override
                    public void onUploadComplete(List<String> fileUrls) {
                        // Kiểm tra nếu danh sách URL đã có đủ phần tử thì tiến hành tạo bài viết
                        createPostWithUrls(fileUrls);
                        dialog.dismiss();
                    }

                    @Override
                    public void onUploadComplete(String fileUrl) {

                    }

                    @Override
                    public void onUploadFailed(String errorMessage) {
                        Toast.makeText(CreatePostActivity.this, "Failed to upload image: " + errorMessage, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });


        btn_backCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkScreen == 1 && chooseMoreClickCount == 1) {
                    Intent intent = new Intent(CreatePostActivity.this, PostActivity.class);
                    intent.putExtra("buttonColor", R.color.blue1);
                    startActivity(intent);
                } else if (checkScreen == 1 && chooseMoreClickCount == 0) {
                    Intent intent = new Intent(CreatePostActivity.this, PostActivity.class);
                    startActivity(intent);
                } else {
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, CameraX.class);
        startActivity(intent);
    }

    private void createPostWithUrls(List<String> fileUrls) {
        String description = edt_description.getText().toString();
        Date createAt = new Date();

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String isoDateString = isoFormat.format(createAt);

        String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");

        // Sử dụng danh sách các URL trong việc tạo yêu cầu đăng bài viết
        RequestCreatePost requestCreatePost = new RequestCreatePost(fileUrls, userId, description, "", isoDateString);
        postViewModel.createPost(requestCreatePost, userId).observe(this, new Observer<ApiResponse<ResponseCreatePost>>() {
            @Override
            public void onChanged(ApiResponse<ResponseCreatePost> responseCreatePostApiResponse) {
                ResponseCreatePost responseCreatePost = responseCreatePostApiResponse.getData();
                userViewModel = new UserViewModel();
                Log.e("abc", new Gson().toJson(responseCreatePost));
                Log.e("abc", new Gson().toJson(responseCreatePost.getPostId()));
                //Tạo thông báo
                Notification notification = new Notification();
                notification.setPostId(responseCreatePost.getPostId());
                notification.setUserId(SharedPreferenceLocal.read(getApplicationContext(), "userId"));
                String userName = SharedPreferenceLocal.read(getApplicationContext(), "userName");
                notification.setText(userName+" vừa đăng một bài viết");
                Log.e("abc", new Gson().toJson(notification.getPostId()));
                for(UserResponse user : responseCreatePost.getListFollowers()){
                    if(user.getTokenFCM() != null){
                        notification.setIdRecipient(user.getId());
                        NotificationService.sendNotification(getApplicationContext(), notification.getText(), user.getTokenFCM());
                        userViewModel.addNotification(notification);
                    }
                }
            }
        });

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
        String postId = postsRef.push().getKey();

        // Tạo dữ liệu cho bài viết trong Realtime Database
        Map<String, Object> postData = new HashMap<>();
        postData.put("imageUrl", fileUrls); // Lưu danh sách URL vào Firebase
        postData.put("userId", userId);
        postData.put("description", description);
        postData.put("createdAt", isoDateString);

        postsRef.child(postId).setValue(postData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "Post created successfully in Realtime Database");
                startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Failed to create post in Realtime Database: " + e.getMessage());
            }
        });
    }

    //
    private void uploadFilesToFirebaseStorage(List<Bitmap> bitmaps, final FirebaseStorageUploader.OnUploadCompleteListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://noteapp-f20f4.appspot.com");
        StorageReference storageRef = storage.getReference();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        AtomicInteger uploadCounter = new AtomicInteger(0);
        List<String> fileUrls = new ArrayList<>();

        for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bitmap = bitmaps.get(i);

            String timestamp = sdf.format(new Date());
            String fileName = "temp_image_" + timestamp + "_" + i + ".jpg";

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
                            fileUrls.add(fileUrl);

                            int count = uploadCounter.incrementAndGet();

                            if (count == bitmaps.size()) {
                                listener.onUploadComplete(fileUrls);
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("TAG Err", "File upload failed: " + exception.getMessage());
                    listener.onUploadFailed(exception.getMessage()); // Gọi phương thức onUploadFailed khi quá trình tải lên thất bại
                }
            });
        }
    }


    //
    private void loadImageSelectedCreatePost() {
        linear_layout_drag_createPost.removeAllViews(); // Xóa tất cả các view con trong linear_layout_drag_createPost trước khi thêm các hình ảnh mới

        for (String imageUrl : selectedFileChoseMore) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1200 // Set chiều cao mong muốn ở đây (350px)
            );
            layoutParams.setMargins(0, 0, 16, 0); // Cài đặt khoảng cách giữa các ImageView
            imageView.setLayoutParams(layoutParams);

            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);

            // Thêm ImageView vào linear_layout_drag_createPost
            linear_layout_drag_createPost.addView(imageView);
        }
    }


}

