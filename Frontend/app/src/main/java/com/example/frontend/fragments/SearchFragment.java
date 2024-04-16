package com.example.frontend.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.frontend.R;
import com.example.frontend.adapter.SearchHistoryAdapter;
import com.example.frontend.adapter.SearchUserAdapter;
import com.example.frontend.adapter.SearchViewPagerAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Search.SearchHistoryResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.utils.SharedPreference_SearchHistory;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.tabs.TabLayout;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


public class SearchFragment extends Fragment {

    private SearchBar searchBar;
    private SearchView searchView;
    private RecyclerView recyclerViewHistory;
    private RecyclerView recyclerViewUser;
    private Button btnSearch;
    private ProgressBar progressBar;
    private RelativeLayout performLayout, searchUserLayout;
    private TabLayout searchTabLayout;
    private ViewPager2 viewPager2;
    private SearchHistoryAdapter searchHistoryAdapter;
    private SearchUserAdapter searchUserAdapter;
    private SearchViewPagerAdapter searchViewPagerAdapter;
    private SharedPreference_SearchHistory sharedPreferenceSearchHistory;
    private List<SearchHistoryResponse> listSearchHistory;
    private List<UserResponse> userList = new ArrayList<>();
    private List<UserResponse> user_searchList;
    private List<UserResponse> listFollowing = new ArrayList<>();
    private UserViewModel userViewModel;
    private FollowsViewModel followsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Khởi tạo search Query ViewModel
        followsViewModel = new ViewModelProvider(requireActivity()).get(FollowsViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBar = view.findViewById(R.id.search_bar);
        searchView = view.findViewById(R.id.search_view);
        recyclerViewHistory = view.findViewById(R.id.recyclerviewHistory);
        recyclerViewUser = view.findViewById(R.id.recyclerViewUser);
        btnSearch = view.findViewById(R.id.btnSearch);
        progressBar = view.findViewById(R.id.progressBar);
        performLayout = view.findViewById(R.id.performLayout);
        searchUserLayout = view.findViewById(R.id.searchUserLayout);
        searchTabLayout = view.findViewById(R.id.search_tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager);

        sharedPreferenceSearchHistory = new SharedPreference_SearchHistory(getContext());

        showListHistory();
        takeFollowingUsers();
        startSearch();
        clickButtonSearch();
    }

    private void takeFollowingUsers() {
        String id = SharedPreferenceLocal.read(getContext(), "userId");
        progressBar.setVisibility(View.VISIBLE);
        followsViewModel.getUserFollowingById(id).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
            @Override
            public void onChanged(ApiResponse<List<UserResponse>> listApiResponse) {
                listFollowing = listApiResponse.getData();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // Hien thi lich su tim kiem
    private void showListHistory() {
        listSearchHistory = sharedPreferenceSearchHistory.read_SearchHistoryList(listSearchHistory);

        // Dua du lieu history vao recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewHistory.setLayoutManager(layoutManager);
        searchHistoryAdapter = new SearchHistoryAdapter(getContext(), listSearchHistory);
        recyclerViewHistory.setAdapter(searchHistoryAdapter);

        clickOneSearchHistory();
    }

    private void clickOneSearchHistory() {

        searchHistoryAdapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClickToRemove(int position) {
                deleteOneSearchHistory(position);
            }

            @Override
            public void onItemClickToSearch(int position) {
                clickOneSearchHistoryToSearch(position);
            }
        });
    }

    private void deleteOneSearchHistory(int position) {

        // Xoa mot item
        // Xoa item o vi tri position
        listSearchHistory.remove(position);

        // Cap nhat lai lich su tim kiem trong shared preference
        sharedPreferenceSearchHistory.save_SearchHistoryList(listSearchHistory);

        // Thong bao cho Adapter khi du lieu bi thay doi
        searchHistoryAdapter.notifyItemRemoved(position);
    }

    private void clickOneSearchHistoryToSearch(int position) {

        // Nếu lịch sử là text
        if (!listSearchHistory.get(position).getAccount()) {
            // Thiet lap giao dien cho searchview
            searchView.show();
            searchView.setText(listSearchHistory.get(position).getText());
            searchView.setAutoShowKeyboard(false);
            searchView.setFocusable(false);
            searchUserLayout.setVisibility(View.GONE);
            performLayout.setVisibility(View.VISIBLE);

            // Thuc hien tim kiem
            performSearch(listSearchHistory.get(position).getText());

            // Dua text nay vao lich su tim kiem
            searchText(listSearchHistory.get(position).getText());
        }

        // Nếu lịch sử là tài khoản
        else {
            SearchHistoryResponse searchHistoryResponse = new SearchHistoryResponse(listSearchHistory.get(position).getText(), listSearchHistory.get(position).getAvatar(), true, listSearchHistory.get(position).getId(), listSearchHistory.get(position).getName(), new java.util.Date());
            clickUserToProfile(searchHistoryResponse);
        }
    }

    private void startSearch() {
        searchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().isEmpty()) { // Nếu search query không rỗng
                    if (searchView.hasFocus()) {
                        searchUserLayout.setVisibility(View.VISIBLE);
                        performLayout.setVisibility(View.GONE);
                        // Tim kiem theo danh sach following
                        searchUserFollowing(charSequence.toString());

                        if (user_searchList.size() < 5) {
                            // Lay danh sach tat ca user tu internet va hien thi ket qua len recyclerViewUser
                            takeAllUserData_showSearchResult(charSequence.toString());
                        }
                    } else {
                        searchUserLayout.setVisibility(View.GONE);
                        performLayout.setVisibility(View.VISIBLE);
                        performSearch(charSequence.toString());
                    }
                } else { // Nếu search query rỗng
                    searchUserLayout.setVisibility(View.GONE);
                    performLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        searchView.getEditText().setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (!searchView.getText().toString().isEmpty()) {
                        searchView.clearFocusAndHideKeyboard();
                        searchUserLayout.setVisibility(View.GONE);
                        performLayout.setVisibility(View.VISIBLE);
                        performSearch(searchView.getText().toString());

                        // Dua search query nay vao lich su tim kiem
                        searchText(searchView.getText().toString());
                    }
                    return false;
                });
    }

    private void searchUserFollowing(String query) {
        user_searchList = new ArrayList<>();

        // Tim kiem theo ten user -> Them vao user_searchList
        for (int i = 0; i < listFollowing.size(); i++) {
            // Chuyen username thanh tieng viet khong dau
            String a = removeAccent(listFollowing.get(i).getUsername().toUpperCase());
            // Chuyen noi dung tim kiem thanh tieng viet khong dau
            String b = removeAccent(query.toUpperCase());
            if (a.contains(b)) {
                user_searchList.add(listFollowing.get(i));
            }
        }

        // Khoi tao layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewUser.setLayoutManager(layoutManager);

        // Chi hien thi toi da 5 phan tu trong user_searchList
        if (user_searchList.size() >= 5) {
            searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList.subList(0, 4));
            recyclerViewUser.setAdapter(searchUserAdapter);
        } else {
            searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList);
            recyclerViewUser.setAdapter(searchUserAdapter);
        }

        searchUserAdapter.setOnItemClickListener(new SearchUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SearchHistoryResponse searchHistoryResponse = new SearchHistoryResponse(user_searchList.get(position).getUsername(), user_searchList.get(position).getAvatarImg(), true, user_searchList.get(position).getId(), user_searchList.get(position).getName(), new java.util.Date());
                clickUserToProfile(searchHistoryResponse);
            }
        });
    }

    private void clickButtonSearch() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!searchView.getText().toString().isEmpty()) {
                    searchView.clearFocusAndHideKeyboard();
                    searchUserLayout.setVisibility(View.GONE);
                    performLayout.setVisibility(View.VISIBLE);
                    performSearch(searchView.getText().toString());

                    // Dua search query nay vao lich su tim kiem
                    searchText(searchView.getText().toString());
                }
            }
        });
    }

    private void searchText(String query) {
        // Dua text nay vao lich su tim kiem
        SearchHistoryResponse searchHistoryResponse = new SearchHistoryResponse(query, null, false, null, null, new java.util.Date());

        // Xoa text bi trung
        for (int i = 0; i < listSearchHistory.size(); i++) {
            if (!listSearchHistory.get(i).getAccount() && Objects.equals(listSearchHistory.get(i).getText(), query)) {
                listSearchHistory.remove(listSearchHistory.get(i));
                break;
            }
        }
        // Them vao dau danh sach lich su
        listSearchHistory.add(0, searchHistoryResponse);

        // Luu vao shared preference
        sharedPreferenceSearchHistory.save_SearchHistoryList(listSearchHistory);

        // Cap nhat lai danh sach lich su tim kiem
        showListHistory();
    }

    // Lay danh sach user tu internet
    private void takeAllUserData_showSearchResult(String query) {
        if (userList.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            userViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
                @Override
                public void onChanged(ApiResponse<List<UserResponse>> userResponses) {
                    userList = userResponses.getData();

                    // Xóa tài khoản hien dang su dung cua ban
                    removeAccount(SharedPreferenceLocal.read(getContext(), "userId"));

                    // Ẩn ProgressBar khi dữ liệu đã được cập nhật
                    progressBar.setVisibility(View.GONE);

                    // Xoa cac tai khoan da duoc tim trong danh sach dang theo doi
                    removeAccountsSearched();

                    // Hien thi ket qua tim kiem len recyclerViewUser
                    searchUserLayout.setVisibility(View.VISIBLE);
                    searchUserWhenQueryChanges(query);
                }
            });
        } else {
            // Hien thi ket qua tim kiem len recyclerViewUser
            searchUserLayout.setVisibility(View.VISIBLE);
            searchUserWhenQueryChanges(query);
        }
    }

    private void removeAccountsSearched() {
        for (int i = 0; i < user_searchList.size(); i++) {
            removeAccount(user_searchList.get(i).getId());
        }
    }

    // Xóa tài khoản hien dang su dung cua ban
    private void removeAccount(String userId) {
        int i = 0;
        while (i < userList.size()) {
            if (userId.equals(userList.get(i).getId())) {
                userList.remove(userList.get(i));
                break;
            }
            i++;
        }
    }

    // Ham chuyen doi tieng viet co dau thanh tieng viet khong dau
    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    private void searchUserWhenQueryChanges(String query) {
        //user_searchList = new ArrayList<>();

        // Tim kiem theo ten user -> Them vao user_searchList
        for (int i = 0; i < userList.size(); i++) {
            // Chuyen username thanh tieng viet khong dau
            String a = removeAccent(userList.get(i).getUsername().toUpperCase());
            // Chuyen noi dung tim kiem thanh tieng viet khong dau
            String b = removeAccent(query.toUpperCase());
            if (a.contains(b)) {
                user_searchList.add(userList.get(i));
            }
        }

        // Khoi tao layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewUser.setLayoutManager(layoutManager);

        // Chi hien thi toi da 5 phan tu trong user_searchList
        if (user_searchList.size() >= 5) {
            searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList.subList(0, 4));
            recyclerViewUser.setAdapter(searchUserAdapter);
        } else {
            searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList);
            recyclerViewUser.setAdapter(searchUserAdapter);
        }

        searchUserAdapter.setOnItemClickListener(new SearchUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SearchHistoryResponse searchHistoryResponse = new SearchHistoryResponse(user_searchList.get(position).getUsername(), user_searchList.get(position).getAvatarImg(), true, user_searchList.get(position).getId(), user_searchList.get(position).getName(), new java.util.Date());
                clickUserToProfile(searchHistoryResponse);
            }
        });
    }

    private void clickUserToProfile(SearchHistoryResponse history) {

        // Xoa tai khoan bi trung
        for (int i = 0; i < listSearchHistory.size(); i++) {
            if (listSearchHistory.get(i).getAccount() && Objects.equals(listSearchHistory.get(i).getId(), history.getId())) {
                listSearchHistory.remove(listSearchHistory.get(i));
                break;
            }
        }
        // Them vao dau danh sach lich su
        listSearchHistory.add(0, history);

        // Luu vao shared preference
        sharedPreferenceSearchHistory.save_SearchHistoryList(listSearchHistory);

        // Set bundle
        Bundle args = new Bundle();
        args.putString("userId", history.getId());

        ProfileFragment clickAccount = new ProfileFragment();
        clickAccount.setArguments(args);

        // Chuyen sang Profile Fragment
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout_main, clickAccount).addToBackStack(null).commit();
    }

    private void performSearch(String query) {
        searchViewPagerAdapter = new SearchViewPagerAdapter(this, query);
        viewPager2.setAdapter(searchViewPagerAdapter);

        searchTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                searchTabLayout.getTabAt(position).select();
            }
        });
    }

}
