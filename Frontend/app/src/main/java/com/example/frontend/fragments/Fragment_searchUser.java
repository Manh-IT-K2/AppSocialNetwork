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
import android.widget.TextView;

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

    UserViewModel userViewModel;
    SearchQuery_ViewModel searchQueryViewModel;
    RecyclerView recyclerView_User;
    ProgressBar progressBar;
    Button btnSearch;
    TextView search_noResult;
    List<UserResponse> userList = new ArrayList<>();
    List<UserResponse> user_searchList;
    Fragment_searchHistory fragment_searchHistory;

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
        return inflater.inflate(R.layout.fragment_search_user, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView_User = view.findViewById(R.id.recyclerViewUser);
        btnSearch = view.findViewById(R.id.btnSearch);
        search_noResult = view.findViewById(R.id.search_noResults);

        if (searchQueryViewModel.getSearchQuery() != null) {
            resultList();
        }
    }

    // Lấy dữ liệu allUsers và kết quả tìm kiếm user
    public void resultList() {
        if (userList.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            userViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
                @Override
                public void onChanged(ApiResponse<List<UserResponse>> userResponses) {
                    userList = userResponses.getData();
                    // Ẩn ProgressBar khi dữ liệu đã được cập nhật
                    progressBar.setVisibility(View.GONE);

                    // Bắt đầu tìm kiếm theo search query và hiển thị trên recyclerView
                    search_User(searchQueryViewModel.getSearchQuery());
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

            if (user_searchList.isEmpty() && !userList.isEmpty()) {
                search_noResult.setText(R.string.noResults_wereFound);
                recyclerView_User.setVisibility(View.GONE);
                btnSearch.setVisibility(View.GONE);

            } else {
                search_noResult.setText("");
                Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_Search_Container);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView_User.setLayoutManager(layoutManager);

                // Khoi tao Adapter
                if (currentFragment instanceof Fragment_searchUser) {
                    // Đang ở Fragment_searchUser
                    // Chi hien thi toi da 5 phan tu trong user_searchList
                    if (user_searchList.size() >= 5) {
                        SearchUserAdapter searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList.subList(0, 4));
                        recyclerView_User.setAdapter(searchUserAdapter);
                    } else {
                        SearchUserAdapter searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList);
                        recyclerView_User.setAdapter(searchUserAdapter);
                    }
                    btnSearch.setVisibility(View.VISIBLE);

                } else {
                    // Đang ở Fragment_performSearch
                    // Hiển thị toàn bộ user_searchList
                    SearchUserAdapter searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList);
                    recyclerView_User.setAdapter(searchUserAdapter);
                    btnSearch.setVisibility(View.GONE);
                }
            }


        } else { // Khong nhap noi dung tim kiem

            // Hiển thị fragment_searchHistory cho container fragment_Search_Container
            fragment_searchHistory = new Fragment_searchHistory();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_Search_Container, fragment_searchHistory)
                    .commit();
        }
    }


}