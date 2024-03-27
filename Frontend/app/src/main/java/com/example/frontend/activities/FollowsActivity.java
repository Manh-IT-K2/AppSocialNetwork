package com.example.frontend.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.fragments.CreateAccountFragment;
import com.example.frontend.fragments.FollowersFragment;
import com.example.frontend.fragments.FollowingFragment;
import com.example.frontend.fragments.SubscriptionsFragment;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

public class FollowsActivity extends AppCompatActivity {
    ImageButton iconBack;
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    FrameLayout fragment_layout_main;
    TextView tvUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follows);
        iconBack = findViewById(R.id.iconBack);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragment_layout_main = findViewById(R.id.fragment_layout_main);
        tvUserName = findViewById(R.id.tvUserName);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemReselectedListener);

        if (getIntent() != null && getIntent().hasExtra("fragment_to_load")) {
            String fragmentToLoad = getIntent().getStringExtra("fragment_to_load");
            tvUserName.setText(getIntent().getStringExtra("userName"));
            if (fragmentToLoad.equals("openFollowing")) {
                setFragment(new FollowingFragment());
                bottomNavigationView.setSelectedItemId(R.id.menu_Following);
            }else if(fragmentToLoad.equals("openFollows")){
                setFragment(new FollowersFragment());
                bottomNavigationView.setSelectedItemId(R.id.menu_Followers);
            }
        }

        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemReselectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.menu_Followers) {
                selectedFragment = new FollowersFragment();
            } else if (item.getItemId() == R.id.menu_Following) {
                selectedFragment = new FollowingFragment();
            }else if (item.getItemId() == R.id.menu_Subscriptions) {
                selectedFragment = new SubscriptionsFragment();
            }
            if(selectedFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout_main,selectedFragment).commit();
            }
            return true;
        }
    };
    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        if (fragment instanceof CreateAccountFragment) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(fragment_layout_main.getId(), fragment);
        fragmentTransaction.commit();
    }
}