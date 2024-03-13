package com.example.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.frontend.R;
import com.example.frontend.fragments.CreateAccountFragment;
import com.example.frontend.fragments.EditProfileFragment;
import com.example.frontend.fragments.LoginFragment;
import com.example.frontend.fragments.ProfileFragment;
import com.example.frontend.fragments.SettingFragment;

public class FragmentReplacerActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_replacer);
        frameLayout = findViewById(R.id.frameLayout);
        if (getIntent() != null && getIntent().hasExtra("fragment_to_load")) {
            String fragmentToLoad = getIntent().getStringExtra("fragment_to_load");
            if (fragmentToLoad.equals("edit_profile")) {
                setFragment(new EditProfileFragment());
            }else if(fragmentToLoad.equals("createaccount_setting")){
                setFragment(new CreateAccountFragment());
            }
        } else {
            setFragment(new LoginFragment());
        }
    }
    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        if (fragment instanceof CreateAccountFragment) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }


}