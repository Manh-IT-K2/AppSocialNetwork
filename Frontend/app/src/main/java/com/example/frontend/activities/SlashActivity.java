package com.example.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.frontend.R;
import com.example.frontend.utils.SharedPreferenceLocal;

public class SlashActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);

        imageView = findViewById(R.id.image_icon);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_splash);
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = originalBitmap.getPixel(i, j);

                // Kiểm tra nếu pixel là màu nền (ví dụ: màu trắng)
                if (pixel == Color.WHITE) {
                    resultBitmap.setPixel(i, j, Color.BLACK); // Hoặc đặt thành màu khác
                } else {
                    resultBitmap.setPixel(i, j, pixel);
                }
            }
        }

        imageView.setImageBitmap(resultBitmap);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userId = SharedPreferenceLocal.read(getApplicationContext(), "userId");

                if(userId.equals("")){
                    startActivity(new Intent(SlashActivity.this, FragmentReplacerActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SlashActivity.this, MainActivity.class));
                }

            }
        }, 2000);
    }
}