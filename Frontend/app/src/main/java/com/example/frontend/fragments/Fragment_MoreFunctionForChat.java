package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontend.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_MoreFunctionForChat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_MoreFunctionForChat extends Fragment {


    public Fragment_MoreFunctionForChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__more_function_for_chat, container, false);
    }
}