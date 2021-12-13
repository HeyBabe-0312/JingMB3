package com.example.jingmb3.view.offline.fragment;

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
import com.example.jingmb3.databinding.FragmentSearchMySongBinding;
import com.example.jingmb3.model.offline.ListSearch;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.offline.MySongsDatabase;
import com.example.jingmb3.view.offline.activity.PlayerSong;
import com.example.jingmb3.view.offline.activity.Search;
import com.example.jingmb3.view.activity.adapter.SearchMySongAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SearchMySong extends Fragment {

    public static FragmentSearchMySongBinding binding;
    ArrayList<MySongObject> listSong;
    public static SearchMySong Instance;
    public static SearchMySong getInstance(){
        if(Instance==null){
            return Instance=new SearchMySong();
        }
        return Instance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentSearchMySongBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listSong=new ArrayList<>();
        binding.rvListsongSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvListsongSearch.setAdapter(SearchMySongAdapter.getInstance());
        loadData();

        SearchMySongAdapter.getInstance().ClickItem(new SearchMySongAdapter.ClickItem() {
            @Override
            public void clickItem(int postion) {
                int p=getPostion(SearchMySongAdapter.getInstance().getListSong().get(postion).getId_song());
                OnClickToPlay(p);
            }
        });

    }

    public void setSearchQuery(String query){
        SearchMySongAdapter.getInstance().getFilter().filter(query);
    }

    public static FragmentSearchMySongBinding getBinding() {
        return binding;
    }

    private void OnClickToPlay(int position) {
        if(!MyMediaPlayer.getInstance().isCheckStopMedia()) MyMediaPlayer.getInstance().stopAudioFile();
        MyMediaPlayer.getInstance().setCheckSongAlbum(false);
        MyMediaPlayer.getInstance().setCheckSongArtist(false);
        MyMediaPlayer.getInstance().setCheckFavSong(false);
        Intent intent=new Intent(getActivity(), PlayerSong.class);
        startActivity(intent.putExtra("pos",position));
        getActivity().overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
    }


    private int getPostion(int id_song) {
        int position=0;
        for(int i=0;i<listSong.size();i++){
            if(listSong.get(i).getId_song()==id_song){
                position=i;
                break;
            }
        }
        return position;
    }

    public void loadData(){
        listSong= ListSearch.getInstance().getListSong();
        SearchMySongAdapter.getInstance().setData(listSong);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(ListSearch.getInstance().isCheckUpdateListSong()){
            ListSearch.getInstance().setCheckUpdateListSong(false);
            loadData();
            ArrayList<MySongObject> arrayList=new ArrayList<>();
            for(MySongObject mySongObject:listSong){
                if(mySongObject.getNameSong().toLowerCase().contains(Search.getBinding().search
                        .getQuery().toString().trim().toLowerCase()))
                    arrayList.add(mySongObject);
            }
            SearchMySongAdapter.getInstance().setListSong(arrayList);
            SearchMySongAdapter.getInstance().notifyDataSetChanged();
        }
    }
}