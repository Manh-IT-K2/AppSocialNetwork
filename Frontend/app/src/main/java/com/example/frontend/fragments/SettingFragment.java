package com.example.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.utils.SharedPreferenceLocal;


public class SettingFragment extends Fragment {
    ImageButton backBtn;
    Button logoutBtn,craeteaccountBtn;
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
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity (),  MainActivity.class);
                intent.putExtra("fragment_to_load", "back_setting");
                startActivity(intent);
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
        return (view);
    }
}