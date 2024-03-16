package com.example.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapter.SearchUserAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.viewModel.User.UserViewModel;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private UserViewModel userViewModel;
    private SearchView searchView;
    private RecyclerView recyclerView_User;
    private ProgressBar progressBar;
    private List<UserResponse> userList = new ArrayList<>();
    private List<UserResponse> user_searchList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        searchView = view.findViewById(R.id.searchView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView_User = view.findViewById(R.id.recyclerViewUser);
        //recyclerView_User.setVisibility(View.GONE);
        searchView.setIconified(false);
        searchView.clearFocus();

        if (userList.isEmpty()) {
            userViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
                @Override
                public void onChanged(ApiResponse<List<UserResponse>> userResponses) {
                    // Cập nhật dữ liệu cho adapter và thông báo thay đổi dữ liệu
                    userList = userResponses.getData();
                    // Ẩn ProgressBar khi dữ liệu đã được cập nhật
                    progressBar.setVisibility(View.GONE);
//                    recyclerView_User.setLayoutManager(new LinearLayoutManager(getContext()));
//                    SearchUserAdapter adapter = new SearchUserAdapter(getContext(), userList);
//                    recyclerView_User.setAdapter(adapter);
                }
            });
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search_User(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search_User(newText);
                return false;
            }
        });
    }

    public void search_User(String query) {
        user_searchList = new ArrayList<>();

        if (query.length() > 0) { // Co nhap noi dung tim kiem

            // Hien thi recyclerview
            recyclerView_User.setVisibility(View.VISIBLE);

            int i = 0, count_account = 0;

            System.out.println(userList.size());
            // Chi tim toi da 6 tai khoan
            while (count_account < 6 && i < userList.size()) {
                if (userList.get(i).getUsername().toUpperCase().contains(query.toUpperCase())) {
                    user_searchList.add(userList.get(i));
                    count_account++;
                }
                i++;
            }

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView_User.setLayoutManager(layoutManager);

            // Khoi tao Adapter
            SearchUserAdapter searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList);
            recyclerView_User.setAdapter(searchUserAdapter);
        } else {
            // Khong nhap noi dung tim kiem
            // Khong hien thi recyclerview
            recyclerView_User.setVisibility(View.GONE);
        }
    }
}