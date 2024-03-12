package com.example.frontend.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.activities.FragmentReplacerActivity;
import com.example.frontend.activities.MainActivity;
import com.example.frontend.utils.SharedPreferenceLocal;

public class ProfileFragment extends Fragment {
    AppCompatButton editprofileBtn;

    // Button logoutBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        editprofileBtn = view.findViewById(R.id.edit_profileBtn);
        editprofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity (), FragmentReplacerActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }
}



      //  logoutBtn = view.findViewById(R.id.logout);
//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferenceLocal.save(getContext(),"userId", "");
//                Intent intent = new Intent(getActivity (), FragmentReplacerActivity.class);
//                startActivity(intent);
//            }
//        });
//        return view;
//  }
//}