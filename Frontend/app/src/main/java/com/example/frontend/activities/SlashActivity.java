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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);
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
        }, 1500);
    }
}