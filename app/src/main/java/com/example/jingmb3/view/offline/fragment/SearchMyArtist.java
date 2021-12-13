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
import com.example.jingmb3.databinding.FragmentSearchMyArtistBinding;
import com.example.jingmb3.model.offline.ListSearch;
import com.example.jingmb3.model.offline.MyArtistDatabase;
import com.example.jingmb3.model.offline.MyArtistObject;
import com.example.jingmb3.view.offline.activity.Search;
import com.example.jingmb3.view.offline.activity.SongOfMyArtist;
import com.example.jingmb3.view.activity.adapter.SearchMyArtistAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchMyArtist extends Fragment {
    public static SearchMyArtist Instance;
    public static SearchMyArtist  getInstance(){
        if(Instance==null){
            return Instance=new SearchMyArtist();
        }
        return Instance;
    }

    FragmentSearchMyArtistBinding binding;
    private ArrayList<MyArtistObject> listArtist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentSearchMyArtistBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listArtist=new ArrayList<>();
        binding.rvListArtistSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvListArtistSearch.setAdapter(SearchMyArtistAdapter.getInstance());
        loadData();
        SearchMyArtistAdapter.getInstance().ClickItem(new SearchMyArtistAdapter.ClickItem() {
            @Override
            public void clickItem(int position) {
                Intent intent=new Intent(getActivity(), SongOfMyArtist.class);
                intent.putExtra("Id",SearchMyArtistAdapter.getInstance().getListArtists().get(position).getId_artist());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
            }
        });
    }

    public void setSearchQuery(String query){
        SearchMyArtistAdapter.getInstance().getFilter().filter(query);
    }

    public void loadData(){
        listArtist= ListSearch.getInstance().getListArtist();
        SearchMyArtistAdapter.getInstance().setData(listArtist,getContext());
    }


    @Override
    public void onResume() {
        super.onResume();
        if(ListSearch.getInstance().isCheckUpdateListArtist()){
            ListSearch.getInstance().setCheckUpdateListArtist(false);
            loadData();
            ArrayList<MyArtistObject> arrayList=new ArrayList<>();
            for(MyArtistObject myArtistObject:listArtist){
                if(myArtistObject.getNameArtist().toLowerCase().contains(Search.getBinding().search
                        .getQuery().toString().trim().toLowerCase()))
                    arrayList.add(myArtistObject);
            }
            SearchMyArtistAdapter.getInstance().setListArtists(arrayList);
            SearchMyArtistAdapter.getInstance().notifyDataSetChanged();
        }
    }
}