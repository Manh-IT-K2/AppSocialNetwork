package com.example.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frontend.R;
import com.example.frontend.adapter.postAdapter;
import com.example.frontend.repository.PostRepository;
import com.example.frontend.request.Post.RequestPostByUserId;
import com.example.frontend.utils.SharedPreferenceLocal;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private postAdapter adapter;
    private List<RequestPostByUserId> postList;
    private PostRepository postRepository;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.list_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postRepository = new PostRepository();
        String userId = SharedPreferenceLocal.read(getContext(),"userId");
        postRepository.getListPostByUserId("65e8a525714ccc3a3caa7f77").observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                postList = response.getData();
                adapter = new postAdapter(getContext(), postList);
                recyclerView.setAdapter(adapter);
            } else {
                // Xử lý khi không có dữ liệu hoặc có lỗi
            }
        });

        return view;
    }
}