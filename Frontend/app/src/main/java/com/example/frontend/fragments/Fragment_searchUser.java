package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.adapter.SearchUserAdapter;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.response.Search.SearchHistoryResponse;
import com.example.frontend.response.User.UserResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.utils.SharedPreference_SearchHistory;
import com.example.frontend.viewModel.Follows.FollowsViewModel;
import com.example.frontend.viewModel.User.UserViewModel;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Fragment_searchUser extends Fragment {

    UserViewModel userViewModel;
    FollowsViewModel followsViewModel;
    RecyclerView recyclerView_User;
    ProgressBar progressBar;
    TextView search_noResult;
    private List<UserResponse> userList = new ArrayList<>();
    private List<UserResponse> user_searchList;
    private SearchUserAdapter searchUserAdapter;
    private SharedPreference_SearchHistory sharedPreferenceSearchHistory;
    private List<SearchHistoryResponse> listSearchHistory;
    private List<UserResponse> listFollowing = new ArrayList<>();
    private String search_query;
    private boolean getUserList, getFollowingList = false;

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
        //searchQueryViewModel = new ViewModelProvider(requireActivity()).get(SearchQuery_ViewModel.class);
        followsViewModel = new ViewModelProvider(requireActivity()).get(FollowsViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_user, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("search_query") != null) {
                search_query = bundle.getString("search_query");
            }
        }

        sharedPreferenceSearchHistory = new SharedPreference_SearchHistory(getActivity());

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView_User = view.findViewById(R.id.recyclerViewUser);
        search_noResult = view.findViewById(R.id.search_noResults);

        resultList();
    }

    private void takeFollowingUsers() {
        if (listFollowing.isEmpty()) {
            String id = SharedPreferenceLocal.read(getContext(), "userId");
            progressBar.setVisibility(View.VISIBLE);
            followsViewModel.getUserFollowingById(id).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
                @Override
                public void onChanged(ApiResponse<List<UserResponse>> listApiResponse) {
                    listFollowing = listApiResponse.getData();
                    //progressBar.setVisibility(View.GONE);
                    getFollowingList = true;
                    if(getUserList == true) {
                        // Bat dau tim kiem theo danh sach dang follow
                        searchUserFollowing(search_query);
                        // Xoa cac tai khoan da duoc tim trong danh sach dang theo doi
                        removeAccountsSearched();
                        // Bắt đầu tìm kiếm theo search query và hiển thị trên recyclerView
                        search_User(search_query);
                    }
                }
            });
        }
        else searchUserFollowing(search_query);
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
    }

    // Lấy dữ liệu allUsers và kết quả tìm kiếm user thong qua ham searchUser
    public void resultList() {
        // Lay danh sach following
        takeFollowingUsers();

        if (userList.isEmpty()) {
            //progressBar.setVisibility(View.VISIBLE);
            userViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<UserResponse>>>() {
                @Override
                public void onChanged(ApiResponse<List<UserResponse>> userResponses) {
                    userList = userResponses.getData();

                    // Xóa tài khoản người dùng hiện tại trong userList
                    removeAccount(SharedPreferenceLocal.read(getContext(), "userId"));

                    // Ẩn ProgressBar khi dữ liệu đã được cập nhật
                    progressBar.setVisibility(View.GONE);

                    getUserList = true;
                    if(getFollowingList == true) {
                        // Bat dau tim kiem theo danh sach dang follow
                        searchUserFollowing(search_query);
                        // Xoa cac tai khoan da duoc tim trong danh sach dang theo doi
                        removeAccountsSearched();
                        // Bắt đầu tìm kiếm theo search query và hiển thị trên recyclerView
                        search_User(search_query);
                    }
                }
            });
        } else
            search_User(search_query);
    }

    private void removeAccountsSearched() {
        for (int i = 0; i < user_searchList.size(); i++) {
            removeAccount(user_searchList.get(i).getId());
        }
    }

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

    // Tìm kiếm user và hiển thị trên recyclerView
    public void search_User(String query) {
        //user_searchList = new ArrayList<>();

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

        if (user_searchList.isEmpty()) {
            search_noResult.setText(R.string.noResults_wereFound);
        } else {
            search_noResult.setText("");

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView_User.setLayoutManager(layoutManager);
            searchUserAdapter = new SearchUserAdapter(getContext(), user_searchList);
            recyclerView_User.setAdapter(searchUserAdapter);

            searchUserAdapter.setOnItemClickListener(new SearchUserAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    SearchHistoryResponse searchHistoryResponse = new SearchHistoryResponse(user_searchList.get(position).getUsername(), user_searchList.get(position).getAvatarImg(), true, user_searchList.get(position).getId(), user_searchList.get(position).getName(), new java.util.Date());
                    clickUserToProfile(searchHistoryResponse);
                }
            });
        }
    }

    private void clickUserToProfile(SearchHistoryResponse history) {

        listSearchHistory = sharedPreferenceSearchHistory.read_SearchHistoryList(listSearchHistory);
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
}