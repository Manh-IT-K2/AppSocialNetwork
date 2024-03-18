package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.frontend.R;
import com.example.frontend.adapter.SearchUserAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.viewModel.Search.SearchQuery_ViewModel;
import com.example.frontend.viewModel.User.UserViewModel;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Fragment_searchUser extends Fragment {

    private UserViewModel userViewModel;
    private SearchQuery_ViewModel searchQueryViewModel;
    private RecyclerView recyclerView_User;
    private ProgressBar progressBar;
    private Button btnSearch;
    private List<UserResponse> userList = new ArrayList<>();
    private List<UserResponse> user_searchList;

    public Fragment_searchUser() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khởi tạo search Query ViewModel
        searchQueryViewModel = new ViewModelProvider(requireActivity()).get(SearchQuery_ViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_view, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView_User = view.findViewById(R.id.recyclerViewUser);
        btnSearch = view.findViewById(R.id.btnSearch);

        btnSearch.setVisibility(View.GONE);

    }

    // Kết quả tìm kiếm user và hiển thị trên recyclerView
    public void resultList() {
        if(userList.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            userViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
                @Override
                public void onChanged(ApiResponse<List<UserResponse>> userResponses) {
                    userList = userResponses.getData();
                    // Ẩn ProgressBar khi dữ liệu đã được cập nhật
                    progressBar.setVisibility(View.GONE);
                    search_User(searchQueryViewModel.getSearchQuery());
//                    recyclerView_User.setLayoutManager(new LinearLayoutManager(getContext()));
//                    SearchUserAdapter adapter = new SearchUserAdapter(getContext(), userList);
//                    recyclerView_User.setAdapter(adapter);
                }
            });
        }
        search_User(searchQueryViewModel.getSearchQuery());
    }



    // Ham chuyen doi tieng viet co dau thanh tieng viet khong dau
    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    // Tìm kiếm user và hiển thị trên recyclerView
    public void search_User(String query) {
        user_searchList = new ArrayList<>();

        if (query.length() > 0) { // Co nhap noi dung tim kiem

            recyclerView_User.setVisibility(View.VISIBLE);

            btnSearch.setVisibility(View.VISIBLE);

            // Tim kiem theo ten user
            // Them vao user_searchList
            for (int i = 0; i < userList.size(); i++) {
                // Chuyen username thanh tieng viet khong dau
                String a = removeAccent(userList.get(i).getUsername().toUpperCase());
                // Chuyen noi dung tim kiem thanh tieng viet khong dau
                String b = removeAccent(query.toUpperCase());

                if (a.contains(b)) {
                    user_searchList.add(userList.get(i));
                }
            }

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView_User.setLayoutManager(layoutManager);

            // Khoi tao Adapter
            // Chi hien thi toi da 5 phan tu trong user_searchList
            if (user_searchList.size() >= 5) {
                SearchUserAdapter searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList.subList(0, 4));
                recyclerView_User.setAdapter(searchUserAdapter);
            } else {
                SearchUserAdapter searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList);
                recyclerView_User.setAdapter(searchUserAdapter);
            }

        } else { // Khong nhap noi dung tim kiem
            recyclerView_User.setVisibility(View.GONE);
            // Khong hien thi button Tim kiem
            btnSearch.setVisibility(View.GONE);
        }
    }



}