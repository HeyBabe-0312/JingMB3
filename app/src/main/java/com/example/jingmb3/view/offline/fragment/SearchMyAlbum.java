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
import com.example.jingmb3.databinding.FragmentSearchMyAlbumBinding;
import com.example.jingmb3.model.offline.ListSearch;
import com.example.jingmb3.model.offline.MyAlbumDatabase;
import com.example.jingmb3.model.offline.MyAlbumObject;
import com.example.jingmb3.view.offline.activity.Search;
import com.example.jingmb3.view.offline.activity.SongsOfAlbum;
import com.example.jingmb3.view.activity.adapter.SearchMyAlbumAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchMyAlbum extends Fragment {
    FragmentSearchMyAlbumBinding binding;

    public static SearchMyAlbum Instance;
    public static SearchMyAlbum  getInstance(){
        if(Instance==null){
            return Instance=new SearchMyAlbum ();
        }
        return Instance;
    }
    ArrayList<MyAlbumObject> listAlbum;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentSearchMyAlbumBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listAlbum=new ArrayList<>();
        binding.rvListAlbumSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvListAlbumSearch.setAdapter(SearchMyAlbumAdapter.getInstance());
        loadData();

        SearchMyAlbumAdapter.getInstance().ClickItem(new SearchMyAlbumAdapter.ClickItem() {
            @Override
            public void clickItem(int position) {
                Intent intent=new Intent(getActivity(), SongsOfAlbum.class);
                intent.putExtra("IdAlbum",SearchMyAlbumAdapter.getInstance().getListAlbum().get(position).getId_album());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
            }
        });
    }

    public void setSearchQuery(String query){
        SearchMyAlbumAdapter.getInstance().getFilter().filter(query);
    }

    public void loadData(){
        listAlbum= ListSearch.getInstance().getListAlbum();
        SearchMyAlbumAdapter.getInstance().setData(listAlbum);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(ListSearch.getInstance().isCheckUpdateListAlbum()){
            ListSearch.getInstance().setCheckUpdateListAlbum(false);
            loadData();
            ArrayList<MyAlbumObject> arrayList=new ArrayList<>();
            for(MyAlbumObject myAlbumObject :listAlbum){
                if(myAlbumObject.getNameAlbum().toLowerCase().contains(Search.getBinding().search
                        .getQuery().toString().trim().toLowerCase()))
                    arrayList.add(myAlbumObject);
            }
            SearchMyAlbumAdapter.getInstance().setListAlbum(arrayList);
            SearchMyAlbumAdapter.getInstance().notifyDataSetChanged();
        }
    }
}