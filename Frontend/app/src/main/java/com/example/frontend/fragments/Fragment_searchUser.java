package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.adapter.SearchUserAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Search.SearchHistoryResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreference_SearchHistory;
import com.example.frontend.viewModel.Search.SearchQuery_ViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Fragment_searchUser extends Fragment {

    UserViewModel userViewModel;
    SearchQuery_ViewModel searchQueryViewModel;
    RecyclerView recyclerView_User;
    ProgressBar progressBar;
    TextView search_noResult;
    Toolbar toolbar_profile;
    private List<UserResponse> userList = new ArrayList<>();
    private List<UserResponse> user_searchList;
    Fragment_searchHistory fragment_searchHistory;
    private SearchUserAdapter searchUserAdapter;
    private SharedPreference_SearchHistory sharedPreferences;
    private ArrayList<SearchHistoryResponse> searchHistoryResponseArrayList;
    private Gson gson;

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

        gson = new Gson();
        sharedPreferences = new SharedPreference_SearchHistory(getActivity());
        getSearchHistoryListFromSharedPreference();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView_User = view.findViewById(R.id.recyclerViewUser);
        search_noResult = view.findViewById(R.id.search_noResults);
        toolbar_profile = getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar_profile);

        resultList();
    }

    // Lấy dữ liệu allUsers và kết quả tìm kiếm user thong qua ham searchUser
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
            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_Search_Container);

            if (user_searchList.isEmpty() && !userList.isEmpty()) {
                if (currentFragment instanceof Fragment_searchUser)
                    search_noResult.setText("");
                else
                    search_noResult.setText(R.string.noResults_wereFound);
                recyclerView_User.setVisibility(View.GONE);
            } else {
                search_noResult.setText("");

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView_User.setLayoutManager(layoutManager);

                // Khoi tao Adapter
                if (currentFragment instanceof Fragment_searchUser) {
                    // Đang ở Fragment_searchUser
                    // Chi hien thi toi da 5 phan tu trong user_searchList
                    if (user_searchList.size() >= 5) {
                        searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList.subList(0, 4));
                        recyclerView_User.setAdapter(searchUserAdapter);
                    } else {
                        searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList);
                        recyclerView_User.setAdapter(searchUserAdapter);
                    }

                } else {
                    // Đang ở Fragment_performSearch
                    // Hiển thị toàn bộ user_searchList
                    searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList);
                    recyclerView_User.setAdapter(searchUserAdapter);
                }

                clickUserToProfile();

            }


        } else { // Khong nhap noi dung tim kiem

            // Hiển thị fragment_searchHistory cho container fragment_Search_Container
            fragment_searchHistory = new Fragment_searchHistory();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_Search_Container, fragment_searchHistory)
                    .commit();
        }
    }

    private void clickUserToProfile() {
        searchUserAdapter.setOnItemClickListener(new SearchUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                // Khi nhan tim kiem -> put data vao Shared preferences
                SearchHistoryResponse searchHistoryResponse = new SearchHistoryResponse(user_searchList.get(position).getUsername(), user_searchList.get(position).getAvatarImg(), true, user_searchList.get(position).getId(), new java.util.Date());
                // Luu vao shared preference
                saveToSearchHistory(searchHistoryResponse, user_searchList.get(position).getUsername());

                Bundle args = new Bundle();
                args.putString("userId", user_searchList.get(position).getId());

                Fragment_search_ClickAccount clickAccount = new Fragment_search_ClickAccount();
                clickAccount.setArguments(args);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_layout_main, clickAccount).addToBackStack("perform_search").commit();

            }
        });
    }

    private void getSearchHistoryListFromSharedPreference() {
        String jsonHistory = sharedPreferences.read_SearchHistoryList();
        Type type = new TypeToken<List<SearchHistoryResponse>>() {}.getType();
        searchHistoryResponseArrayList = gson.fromJson(jsonHistory, type);

        if (searchHistoryResponseArrayList == null) {
            searchHistoryResponseArrayList = new ArrayList<>();
        }
    }

    private void saveSearchHistoryListToSharedPreference(ArrayList<SearchHistoryResponse> searchHistoryList) {
        // convert object to String by Gson
        String jsonHistory = gson.toJson(searchHistoryList);

        // save to shared preference
        sharedPreferences.save_SearchHistoryList(jsonHistory);
    }

    private void saveToSearchHistory(SearchHistoryResponse searchHistoryResponse, String query) {
        if (searchHistoryResponseArrayList.isEmpty())
            searchHistoryResponseArrayList.add(searchHistoryResponse);
        else {
            // Xoa object trong shared preference co text trung voi query
            int i = 0;
            while (i < searchHistoryResponseArrayList.size()) {
                if (searchHistoryResponseArrayList.get(i).getText().equals(query) && searchHistoryResponseArrayList.get(i).getAccount() == searchHistoryResponse.getAccount()) {
                    searchHistoryResponseArrayList.remove(searchHistoryResponseArrayList.get(i));
                    break;
                }
                i++;
            }
            searchHistoryResponseArrayList.add(0, searchHistoryResponse);
        }

        // Luu vao shared preference
        saveSearchHistoryListToSharedPreference(searchHistoryResponseArrayList);
    }
}