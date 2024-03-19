package com.example.frontend.activities;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.frontend.R;
import com.example.frontend.adapter.ImagePostAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private ImageView btn_closePost,  btn_cameraPost;
    private RecyclerView list_mainPost;
    private ImagePostAdapter adapter;

    private FloatingActionButton btn_createPost;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Init views
        btn_closePost = findViewById(R.id.btn_closePost);
        list_mainPost = findViewById(R.id.list_mainPost);
        btn_cameraPost = findViewById(R.id.btn_cameraPost);
//        btn_createPost = findViewById(R.id.btn_createPost);

        // Close page post
        btn_closePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //
        btn_cameraPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("conc","not open camera");
                // Gọi hàm để mở camera ở đây
                openCamera();
                Log.d("conc","opened camera");
            }
        });

        //
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

                != PackageManager.PERMISSION_GRANTED) {
            //Log.d("errorLoad","e.toString()");
            // Quyền chưa được cấp, yêu cầu quyền từ người dùng
            try {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                //Log.d("errorLoad","e.toString()");
            }catch (Exception e){
                Gson gson = new Gson();
                loadImages();
                e.printStackTrace();
                String json = gson.toJson(e);
                Log.d("errorLoad",json);
            }
        } else {
            //Log.d("errorLoad","e.toString()");
            // Quyền đã được cấp, tiến hành load ảnh
            loadImages();
        }

    }


    //
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Xử lý hình ảnh đã chụp ở đây (nếu cần thiết)
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Ví dụ: Hiển thị hình ảnh đã chụp
            //imageView.setImageBitmap(imageBitmap);
        }
    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, tiến hành load ảnh
                loadImages();
            } else {
                //loadImages();
                // Quyền bị từ chối, thông báo cho người dùng hoặc xử lý một cách phù hợp
                Log.d("PermissionDenied", "Quyền truy cập bộ nhớ bị từ chối");
            }
        }
    }


    private void loadImages() {
        List<Uri> imageList = new ArrayList<>();
        try {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.Media._ID};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    long imageId = cursor.getLong(columnIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId);
                    imageList.add(imageUri);
                }
                cursor.close();
            }
            Log.d("ImageCount", "Number of images: " + imageList.size());
            adapter = new ImagePostAdapter(this, imageList);
            list_mainPost.setAdapter(adapter);
            list_mainPost.setLayoutManager(new GridLayoutManager(this, 3));
        }catch (Exception e){
            e.printStackTrace();
            Log.d("errorLoad",e.toString());
        }
    }
}