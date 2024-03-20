package com.example.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.frontend.R;
import com.example.frontend.utils.CameraX;

import java.util.ResourceBundle;

public class CreatePostActivity extends AppCompatActivity {

    // Khai báo biến
    private ImageView img_createPost;
    private Button btn_sharePost;
    private ImageView btn_backCreatePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // Khởi tạo view
        img_createPost = findViewById(R.id.img_createPost);
        btn_sharePost = findViewById(R.id.btn_sharePost);
        btn_backCreatePost = findViewById(R.id.btn_backCreatePost);

        // Nhận đường dẫn tệp ảnh từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String imagePath = extras.getString("imagePath");
            if (imagePath != null) {
                // Tạo một đối tượng Bitmap từ đường dẫn tệp ảnh
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                // Kiểm tra xem bitmap có null hay không trước khi gán vào ImageView
                if (bitmap != null) {
                    // Gán bitmap vào ImageView
                    img_createPost.setImageBitmap(bitmap);
                } else {
                    // Nếu không thể tạo được bitmap từ đường dẫn, có thể thông báo lỗi hoặc thực hiện xử lý phù hợp khác
                    // Ví dụ: Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }

        //action btn_backCreatePost
        btn_backCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // back screen cameraX
    @Override
    public void onBackPressed() {
        super.onBackPressed();
         Intent intent = new Intent(this, CameraX.class);
         startActivity(intent);
    }
}
