package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.frontend.fragments.CreateAccountFragment;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        name = findViewById(R.id.name);

        Intent intent = getIntent();
        String str = intent.getStringExtra("user");
        if (str != null) {
            name.setText(str);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FragmentReplacerActivity.class);
                startActivity(intent);
            }
        });
    }
}