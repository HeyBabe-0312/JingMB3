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
import com.example.jingmb3.databinding.FragmentSearchSongOnlBinding;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.online.GetSongs;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.activity.adapter.SearchSongOnlAdapter;
import com.example.jingmb3.view.online.activity.playSong_Online;

import java.util.ArrayList;
import java.util.List;

public class SearchSongOnl extends Fragment {
    FragmentSearchSongOnlBinding binding;
    List<GetSongs> getSongsList;
    public static SearchSongOnl intance;
    public static SearchSongOnl getInstance(){
        if(intance==null) return intance = new SearchSongOnl();
        return intance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchSongOnlBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSongsList = new ArrayList<>();
        getSongsList = MediaOnline.getInstance().getGetSongsList();
        SearchSongOnlAdapter.getInstance().setGetSongs(getSongsList,getContext());
        binding.rvListsongonlSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvListsongonlSearch.setAdapter(SearchSongOnlAdapter.getInstance());
        SearchSongOnlAdapter.getInstance().setClickSong(new SearchSongOnlAdapter.clickSongsOnline() {
            @Override
            public void clickSongsOnline(int position) {
                int p = getPosition(SearchSongOnlAdapter.getInstance().getSongsList().get(position).getmKey());
                if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
                    MyMediaPlayer.getInstance().stopAudioFile();
                }
                if(!MediaOnline.getInstance().isCheckStop()){
                    MediaOnline.getInstance().stopPlaySong();
                }
                MediaOnline.getInstance().setCheckSongAlbum(false);
                MediaOnline.getInstance().setCheckSongArtist(false);
                Intent i = new Intent(getContext(), playSong_Online.class);
                i.putExtra("idSong",p);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
            }
        });
    }
    private int getPosition(String keySong){
        int position = 0;
        for(int i = 0; i < getSongsList.size();i++){
            if(getSongsList.get(i).getmKey().equals(keySong)){
                position = i;
                break;
            }
        }
        return position;
    }
    public void setSearchQuery(String query){
        SearchSongOnlAdapter.getInstance().getFilter().filter(query);
    }
}