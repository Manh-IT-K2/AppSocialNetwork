package com.example.frontend.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.fragments.HomeFragment;
import com.example.frontend.fragments.CreateAccountFragment;
import com.example.frontend.fragments.EditProfileFragment;
import com.example.frontend.fragments.HomeFragment;
import com.example.frontend.fragments.NotificationFragment;
import com.example.frontend.fragments.ProfileFragment;
import com.example.frontend.fragments.SearchFragment;
import com.example.frontend.fragments.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // innit variable
    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment = null;
    private FloatingActionButton btn_createPost;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // innit view
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        btn_createPost = findViewById(R.id.btn_createPost);

        // action bottom bar
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemReselectedListener);
         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout_main,new HomeFragment()).commit();
        handleIntentData();

        // action click create post
        btn_createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PostActivity.class);
                startActivity(intent);
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemReselectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.menu_home) {
                       selectedFragment = new HomeFragment();
                    } else if (item.getItemId() == R.id.menu_search) {
                        selectedFragment = new SearchFragment();
                    }else if (item.getItemId() == R.id.menu_post) {
                        selectedFragment = null;
                    } else if (item.getItemId() == R.id.menu_notification) {
                        selectedFragment = new NotificationFragment();
                    } else if (item.getItemId() == R.id.menu_profile) {
                        if ("setting_btn".equals(getIntent().getStringExtra("fragment_to_load"))) {
                            selectedFragment = new SettingFragment();
                            return true; // Return true without changing the fragment
                        }
                        selectedFragment = new ProfileFragment();
                    }
                    if(selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout_main,selectedFragment).commit();
                    }
                    return true;
                }
            };

    private void handleIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String fragmentToLoad = intent.getStringExtra("fragment_to_load");
            if (fragmentToLoad != null) {
                switch (fragmentToLoad) {
                    case "edit_profile_done":
                    case "edit_profile_cancel":
                        selectedFragment = new ProfileFragment();
                        break;
                    case "back_setting":
                        selectedFragment = new ProfileFragment();
                        break;
                    case "setting_btn":
                        selectedFragment = new SettingFragment();
                        break;
                }
                // Replace the fragment with the selected one
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout_main, selectedFragment).commit();
                    bottomNavigationView.setSelectedItemId(R.id.menu_profile);

                }
            }
        }
    }
}