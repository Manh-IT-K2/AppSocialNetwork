package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.frontend.R;
import com.example.frontend.adapter.LikedAdapter;
import com.example.frontend.response.User.UserResponse;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LikeFragment extends Fragment {

    private RecyclerView listUserLiked;
    private LikedAdapter likedAdapter;
    private ImageView btn_backLike;
    private ImageButton btn_cleanTextSearchLiked;
    private EditText edt_searchUserLiked;

    private List<UserResponse> originalLikes; // Danh sách gốc
    private List<UserResponse> filteredLikes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_like, container, false);

        // init view
        listUserLiked = view.findViewById(R.id.list_userLiked);
        btn_backLike = view.findViewById(R.id.btn_backLike);
        btn_cleanTextSearchLiked = view.findViewById(R.id.btn_cleanTextSearchLiked);
        edt_searchUserLiked = view.findViewById(R.id.edt_searchUserLiked);

        // init list liked
        listUserLiked.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve data passed from previous fragment/activity
        String likesJson = getArguments().getString("likes");
        originalLikes = new Gson().fromJson(likesJson, new TypeToken<List<UserResponse>>(){}.getType());
        if (originalLikes != null){
            filteredLikes = new ArrayList<>(originalLikes); // Khởi tạo danh sách đã lọc bằng danh sách gốc ban đầu
            likedAdapter = new LikedAdapter(getContext(), filteredLikes);
            Log.e("nooooooo", new Gson().toJson(originalLikes));
            // Set the adapter with the data
            listUserLiked.setAdapter(likedAdapter);
        }

        // hidden btn_cleanTextSearchLiked when edt_searchUserLiked is empty
        btn_cleanTextSearchLiked.setVisibility(View.INVISIBLE);

        // remove text in edt_searchUserLiked
        btn_cleanTextSearchLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_searchUserLiked.setText("");
                btn_cleanTextSearchLiked.setVisibility(View.INVISIBLE);
            }
        });

        // set action click back
        btn_backLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the HomeFragment
                HomeFragment homeFragment = new HomeFragment();
                // Navigate to the HomeFragment by replacing the current fragment
                FragmentTransaction transaction = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_layout_main, homeFragment); // Replace 'fragment_container' with your container ID
                transaction.addToBackStack(null); // Add transaction to the back stack
                transaction.commit();
            }
        });

        // search user liked
        edt_searchUserLiked.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    // Method to filter list of user favorites based on entered text
    private void filter(String searchText) {
        filteredLikes.clear();
        if (searchText.isEmpty()) {
            filteredLikes.addAll(originalLikes);
            btn_cleanTextSearchLiked.setVisibility(View.INVISIBLE);
        } else {
            btn_cleanTextSearchLiked.setVisibility(View.VISIBLE);
            for (UserResponse user : originalLikes) {
                if (user.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredLikes.add(user);
                }
            }
        }
        likedAdapter.notifyDataSetChanged();
    }
}