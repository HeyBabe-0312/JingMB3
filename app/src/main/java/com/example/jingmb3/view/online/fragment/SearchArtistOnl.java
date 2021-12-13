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

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.FragmentSearchArtistOnlBinding;
import com.example.jingmb3.model.online.ArtistOnline;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.activity.adapter.SearchAlbumOnlAdapter;
import com.example.jingmb3.view.activity.adapter.SearchArtistOnlAdapter;
import com.example.jingmb3.view.online.activity.SongOfArtistOnl;

import java.util.ArrayList;
import java.util.List;

public class SearchArtistOnl extends Fragment {
    FragmentSearchArtistOnlBinding binding;
    List<ArtistOnline> artistsOnlineList;
    public static SearchArtistOnl instance;
    public static SearchArtistOnl getInstance(){
        if(instance==null) return instance = new SearchArtistOnl();
        return instance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchArtistOnlBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        artistsOnlineList = new ArrayList<>();
        artistsOnlineList = MediaOnline.getInstance().getArtistOnlineList();
        SearchArtistOnlAdapter.getInstance().setGetArtist(artistsOnlineList,getContext());
        binding.rvListArtistSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvListArtistSearch.setAdapter(SearchArtistOnlAdapter.getInstance());
        SearchArtistOnlAdapter.getInstance().setClickArtist(new SearchArtistOnlAdapter.clickArtistOnline() {
            @Override
            public void clickArtistOnline(int position) {
                Intent intent = new Intent(getContext(), SongOfArtistOnl.class);
                intent.putExtra("name",SearchArtistOnlAdapter.getInstance().getArtistList().get(position).getArtist());
                intent.putExtra("url",SearchArtistOnlAdapter.getInstance().getArtistList().get(position).getUrl());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
            }
        });
    }
    public void setSearchQuery(String query){
        SearchArtistOnlAdapter.getInstance().getFilter().filter(query);
    }
}