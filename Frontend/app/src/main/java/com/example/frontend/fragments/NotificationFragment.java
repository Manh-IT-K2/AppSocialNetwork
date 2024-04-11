package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.frontend.R;
import com.example.frontend.adapter.NotificationAdapter;
import com.example.frontend.request.Notification.NotificationResponse;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    List<NotificationResponse> notificationResponseList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        notificationResponseList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getContext(), notificationResponseList);
        recyclerView.setAdapter(notificationAdapter);
        return view;
    }
}