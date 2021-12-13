package com.example.jingmb3.view.online.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.FragmentAlbumsOnlineBinding;
import com.example.jingmb3.databinding.FragmentArtistsOnlineBinding;
import com.example.jingmb3.databinding.ItemArtistOnlineBinding;
import com.example.jingmb3.model.online.ArtistOnline;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.model.online.Upload;
import com.example.jingmb3.view.activity.LoadingDialog;
import com.example.jingmb3.view.activity.adapter.AlbumOnlineAdapter;
import com.example.jingmb3.view.activity.adapter.ArtistOnlineAdapter;
import com.example.jingmb3.view.online.activity.SongOfAlbumOnl;
import com.example.jingmb3.view.online.activity.SongOfArtistOnl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ArtistsOnline extends Fragment {
    DatabaseReference databaseReference;
    FragmentArtistsOnlineBinding binding;
    List<ArtistOnline> artistsOnlineList;
    ArtistOnlineAdapter artistOnlineAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArtistsOnlineBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        artistsOnlineList = new ArrayList<>();
        artistOnlineAdapter = new ArtistOnlineAdapter(artistsOnlineList,getContext());
        artistOnlineAdapter.setGetArtist(artistsOnlineList);
        binding.rcView.setLayoutManager(new GridLayoutManager(getContext(),3));
        binding.rcView.setAdapter(artistOnlineAdapter);
        databaseReference = FirebaseDatabase.getInstance("https://musicupload-7dde0-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("artists");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                artistsOnlineList.clear();
                for(DataSnapshot dss:snapshot.getChildren()){
                    ArtistOnline artist = dss.getValue(ArtistOnline.class);
                    artistsOnlineList.add(artist);
                }
                binding.countArtist.setText(artistsOnlineList.size()+" Artist");
                Arrange(artistsOnlineList);
                MediaOnline.getInstance().setArtistOnlineList(artistsOnlineList);
                artistOnlineAdapter.setGetArtist(artistsOnlineList);
                LoadingDialog.getInstance().StopDialog();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                LoadingDialog.getInstance().StopDialog();
            }
        });
        artistOnlineAdapter.setClickArtist(new ArtistOnlineAdapter.clickArtistOnline(){
            @Override
            public void clickArtistOnline(int position) {
                Intent intent = new Intent(getContext(), SongOfArtistOnl.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name",artistsOnlineList.get(position).getArtist());
                intent.putExtra("url",artistsOnlineList.get(position).getUrl());
                startActivityForResult(intent,30);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
            }
        });
    } public void Arrange(List<ArtistOnline> myListArtist){
        Collections.sort(myListArtist, new Comparator<ArtistOnline>() {
            @Override
            public int compare(ArtistOnline myArtistObject, ArtistOnline t1) {
                return myArtistObject.getArtist().compareToIgnoreCase(t1.getArtist());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==30 && resultCode== Activity.RESULT_OK){
            MusicOnline musicOnline = (MusicOnline) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
            musicOnline.loadMediaPlay(MediaOnline.getInstance().getPosition());
            if(MediaOnline.getInstance().getMediaPlayer()!=null){
                musicOnline.completeSong();
            }
        }
    }
}