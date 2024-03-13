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
    private ArrayList<UserResponse> user_searchList;


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
        progressBar.setVisibility(View.GONE);
        recyclerView_User = view.findViewById(R.id.recyclerViewUser);
        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                user_searchList = new ArrayList<>();
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

        if (query.length() > 0) { // Co nhap noi dung tim kiem
            // Neu userList rong
            // Lay tat ca du lieu user va dua vao userList
            if(userList.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                userViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
                    @Override
                    public void onChanged(ApiResponse<List<UserResponse>> listApiResponse) {
                        if (listApiResponse.isStatus() == false)
                            Toast.makeText(getContext(), listApiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        else {
                            userList = listApiResponse.getData();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            // Hien thi recyclerview
            recyclerView_User.setVisibility(View.VISIBLE);

            int i = 0, count_account = 0;

            // Chi tim toi da 6 tai khoan
            while (count_account < 6 && i < userList.size()) {
                if (userList.get(i).getUsername().toUpperCase().contains(query.toUpperCase())) {
                    user_searchList.add(userList.get(i));
                    count_account++;
                }
            }

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView_User.setLayoutManager(layoutManager);

            // Khoi tao Adapter
            SearchUserAdapter searchUserAdapter = new SearchUserAdapter(getContext(), (ArrayList<UserResponse>) userList);
            System.out.println(searchUserAdapter.getItemCount());
            recyclerView_User.setAdapter(searchUserAdapter);
        } else {
            // Khong nhap noi dung tim kiem
            // Khong hien thi recyclerview
            recyclerView_User.setVisibility(View.GONE);
        }
    }
}