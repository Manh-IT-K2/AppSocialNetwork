package com.example.frontend.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frontend.R;
import com.example.frontend.adapter.ImagePostAdapter;
import com.example.frontend.utils.CameraX;
import com.example.frontend.utils.ImageGallery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private ImageView btn_closePost,  btn_cameraPost, img_mainPost;
    private RecyclerView list_mainPost;
    private ImagePostAdapter adapter;

    private List<String> imageGallery;
    private static final int MY_READ_PERMISSION_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Init views
        btn_closePost = findViewById(R.id.btn_closePost);
        list_mainPost = findViewById(R.id.list_mainPost);
        btn_cameraPost = findViewById(R.id.btn_cameraPost);
        img_mainPost = findViewById(R.id.img_mainPost);

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
                Intent intent = new Intent(PostActivity.this, CameraX.class);
                startActivity(intent);
            }
        });

        // check permission
        checkPermissionAndLoadImages();
        //
        String imageMain = imageGallery.get(0);
        Glide.with(getApplicationContext()).load(imageMain).into(img_mainPost);
    }

    private void checkPermissionAndLoadImages() {
        if (ContextCompat.checkSelfPermission(PostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it from the user
            ActivityCompat.requestPermissions(PostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        } else {
            // Permission granted, load images
            loadImages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_READ_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, load images
                loadImages();
            } else {
                Toast.makeText(PostActivity.this, "Read external storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadImages() {
        list_mainPost.setHasFixedSize(true);
        list_mainPost.setLayoutManager(new GridLayoutManager(this, 3));
        imageGallery = ImageGallery.listOfImage(this);
        adapter = new ImagePostAdapter(this, imageGallery, new ImagePostAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                // Load selected image into img_mainPost
                Glide.with(PostActivity.this).load(path).into(img_mainPost);
            }
        });
        list_mainPost.setAdapter(adapter);
    }
}
