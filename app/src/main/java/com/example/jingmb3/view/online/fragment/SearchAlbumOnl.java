package com.example.jingmb3.view.online.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.FragmentSearchAlbumOnlBinding;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.model.online.Upload;
import com.example.jingmb3.view.activity.adapter.SearchAlbumOnlAdapter;
import com.example.jingmb3.view.activity.adapter.SearchSongOnlAdapter;
import com.example.jingmb3.view.online.activity.SongOfAlbumOnl;

import java.util.ArrayList;
import java.util.List;

public class SearchAlbumOnl extends Fragment {
    FragmentSearchAlbumOnlBinding binding;
    List<Upload> uploadList;
    public static SearchAlbumOnl instance;
    public static SearchAlbumOnl getInstance(){
        if(instance==null) return instance = new SearchAlbumOnl();
        return instance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchAlbumOnlBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadList = new ArrayList<>();
        uploadList = MediaOnline.getInstance().getAlbumList();
        SearchAlbumOnlAdapter.getInstance().setGetAlbum(uploadList,getContext());
        binding.rvListAlbumOnlSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvListAlbumOnlSearch.setAdapter(SearchAlbumOnlAdapter.getInstance());
        SearchAlbumOnlAdapter.getInstance().setClickAlbum(new SearchAlbumOnlAdapter.clickAlbumOnline() {
            @Override
            public void clickAlbumOnline(int position) {
                Intent intent = new Intent(getContext(), SongOfAlbumOnl.class);
                intent.putExtra("album",SearchAlbumOnlAdapter.getInstance().getListAlbum().get(position).getName());
                intent.putExtra("url",SearchAlbumOnlAdapter.getInstance().getListAlbum().get(position).getUrl());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
            }
        });
    }
    public void setSearchQuery(String query){
        SearchAlbumOnlAdapter.getInstance().getFilter().filter(query);
    }
}