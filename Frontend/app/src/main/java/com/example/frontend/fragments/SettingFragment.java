package com.example.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.utils.SharedPreferenceLocal;


public class SettingFragment extends Fragment {
    ImageButton backBtn;
    Button logoutBtn,craeteaccountBtn,function_change_pass_Btn;
    public SettingFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        backBtn = view.findViewById(R.id.back_btn);
        logoutBtn = view.findViewById(R.id.logout_setting_Btn);
        craeteaccountBtn = view.findViewById(R.id.createaccount_setting_Btn);
        function_change_pass_Btn = view.findViewById(R.id.function_change_pass);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment Profile = new ProfileFragment();

                // Lấy instance của FragmentManager
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Bắt đầu transaction để thay thế fragment hiện tại bằng fragment mới
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_layout_main, Profile);
                fragmentTransaction.addToBackStack(null); // Thêm transaction này vào stack back để quay trở lại fragment trước đó (nếu cần)
                fragmentTransaction.commit();
            }
        });
        craeteaccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity (),  FragmentReplacerActivity.class);
                intent.putExtra("fragment_to_load", "createaccount_setting");
                startActivity(intent);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceLocal.save(getContext(),"userId", "");
                Intent intent = new Intent(getActivity (), FragmentReplacerActivity.class);
                startActivity(intent);
            }
        });
        function_change_pass_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue = getArguments().getString("username");
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameValue);
                Function_change_password functionChangePasswordFragment = new Function_change_password();
                functionChangePasswordFragment.setArguments(bundle);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_layout_main, functionChangePasswordFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return (view);
    }
}