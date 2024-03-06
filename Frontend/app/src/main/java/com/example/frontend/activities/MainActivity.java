package com.example.frontend.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.fragments.HomeFragment;
import com.example.frontend.fragments.NotificationFragment;
import com.example.frontend.fragments.ProfileFragment;
import com.example.frontend.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    private Button btn;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // innit variable
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        selectedFragment = findViewById(R.id.fragment_layout_main);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemReselectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout_main,new HomeFragment()).commit();
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
                        selectedFragment = new ProfileFragment();
                    }

                    if(selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout_main,selectedFragment).commit();
                    }
                    return true;
                }
            };

}