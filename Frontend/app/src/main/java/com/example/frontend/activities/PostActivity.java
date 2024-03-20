package com.example.frontend.activities;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
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

import android.os.Environment;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.frontend.R;
import com.example.frontend.adapter.ImagePostAdapter;
import com.example.frontend.utils.CameraX;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private ImageView btn_closePost,  btn_cameraPost;
    private RecyclerView list_mainPost;
    private ImagePostAdapter adapter;

    private FloatingActionButton btn_createPost;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String imageFilePath;

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
//                Log.d("test","not open camera");
//                // Gọi hàm để mở camera ở đây
//                dispatchTakePictureIntent();
//                Log.d("test","opened camera");
                Intent intent = new Intent(PostActivity.this, CameraX.class);
                startActivity(intent);
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


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws Exception {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Ảnh đã được chụp và lưu tại đường dẫn `imageFilePath`
            Toast.makeText(this, "Ảnh đã được chụp và lưu", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu người dùng không chụp ảnh
            Toast.makeText(this, "Chụp ảnh không thành công", Toast.LENGTH_SHORT).show();
        }
    }
    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, tiến hành mở camera
                dispatchTakePictureIntent();
            } else {
                // Quyền bị từ chối, thông báo cho người dùng hoặc xử lý một cách phù hợp
                Toast.makeText(this, "Quyền truy cập camera bị từ chối", Toast.LENGTH_SHORT).show();
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