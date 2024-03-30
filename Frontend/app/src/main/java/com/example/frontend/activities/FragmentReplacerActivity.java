package com.example.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.frontend.R;
import com.example.frontend.fragments.ChangePasswordFragment;
import com.example.frontend.fragments.CreateAccountFragment;
import com.example.frontend.fragments.EditProfileFragment;
import com.example.frontend.fragments.Function_change_password;
import com.example.frontend.fragments.LoginFragment;
import com.example.frontend.fragments.ProfileFragment;
import com.example.frontend.fragments.SettingFragment;
import com.example.frontend.fragments.VerificationCodeFragment;

public class FragmentReplacerActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_replacer);
        frameLayout = findViewById(R.id.frameLayout);
        if (getIntent() != null && getIntent().hasExtra("fragment_to_load")) {
            String fragmentToLoad = getIntent().getStringExtra("fragment_to_load");
            if(fragmentToLoad.equals("createaccount_setting")){
                setFragment(new CreateAccountFragment());
            }else if(fragmentToLoad.equals("verify_code")){
                setFragment(new VerificationCodeFragment());
            }else if(fragmentToLoad.equals("change_pass")){
                setFragment(new ChangePasswordFragment());
            }else if(fragmentToLoad.equals("create_new_pass")){
                setFragment(new LoginFragment());
            }else if(fragmentToLoad.equals("function_change_pass")){
                setFragment(new Function_change_password());
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