package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontend.R;
import com.example.frontend.adapter.postAdapter;
import com.example.frontend.repository.PostRepository;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.response.ApiResponse.ApiResponse;
import com.example.frontend.utils.SharedPreferenceLocal;
import com.example.frontend.viewModel.Post.PostViewModel;
import com.example.frontend.viewModel.User.UserViewModel;
import com.google.gson.Gson;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private postAdapter adapter;
    private List<RequestPostByUserId> postList;
    private PostViewModel postViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.list_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        String userId = SharedPreferenceLocal.read(getContext(),"userId");

        postViewModel.getListPostByUserId(userId).observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<RequestPostByUserId>>>() {
            @Override
            public void onChanged(ApiResponse<List<RequestPostByUserId>> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response);
                Log.d("check", json);
//                if (response.getData().size() > 0) {
//                    postList = response.getData();
//                    adapter = new postAdapter(getContext(), postList);
//                    recyclerView.setAdapter(adapter);
//                } else {
//                    // Xử lý khi không có dữ liệu hoặc có lỗi
//                }
            }
        });

        return view;
    }
}