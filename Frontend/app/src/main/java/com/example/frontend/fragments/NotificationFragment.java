package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontend.R;
import com.example.frontend.adapter.NotificationAdapter;
import com.example.frontend.response.User.NotificationResponse;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.User.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    List<NotificationResponse> notificationResponseList;
    UserViewModel userViewModel;

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

        String userId = SharedPreferenceLocal.read(getContext(), "userId");
        userViewModel = new UserViewModel();

        userViewModel.getNotification(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<NotificationResponse>>>() {
            @Override
            public void onChanged(ApiResponse<List<NotificationResponse>> listApiResponse) {
                notificationResponseList = listApiResponse.getData();
                notificationAdapter = new NotificationAdapter(getContext(), notificationResponseList,getChildFragmentManager());
                recyclerView.setAdapter(notificationAdapter);
            }
        });
        return view;
    }
}