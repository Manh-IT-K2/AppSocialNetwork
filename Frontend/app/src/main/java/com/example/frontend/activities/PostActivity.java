package com.example.frontend.activities;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.example.frontend.R;
import com.example.frontend.adapter.ImagePostAdapter;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private ImageView btn_closePost;
    private RecyclerView list_mainPost;
    private ImagePostAdapter adapter;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Init views
        btn_closePost = findViewById(R.id.btn_closePost);
        list_mainPost = findViewById(R.id.list_mainPost);

        // Close page post
        btn_closePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Quyền chưa được cấp, yêu cầu quyền từ người dùng
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Quyền đã được cấp, tiến hành load ảnh
            loadImages();
        }

    }

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
    }
}
