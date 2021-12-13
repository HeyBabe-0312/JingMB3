package com.example.jingmb3.view.online.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.FragmentSongsOnlineBinding;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.online.GetSongs;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.model.online.Upload;
import com.example.jingmb3.view.activity.LoadingDialog;
import com.example.jingmb3.view.activity.adapter.SongOnlineAdapter;
import com.example.jingmb3.view.online.activity.playSong_Online;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SongsOnline extends Fragment {
    DatabaseReference databaseReference;
    FragmentSongsOnlineBinding binding;
    SongOnlineAdapter songOnlineAdapter;
    List<GetSongs> getSongsList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSongsOnlineBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSongsList = new ArrayList<>();
        songOnlineAdapter = new SongOnlineAdapter(getSongsList,getContext());
        songOnlineAdapter.setGetSongs(getSongsList);
        binding.rcView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcView.setAdapter(songOnlineAdapter);
        databaseReference = FirebaseDatabase.getInstance("https://musicupload-7dde0-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("songs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getSongsList.clear();
                for(DataSnapshot dss:snapshot.getChildren()){
                    GetSongs getSongs = dss.getValue(GetSongs.class);
                    getSongsList.add(getSongs);
                }
                binding.countSongs.setText(getSongsList.size()+" Bài hát");
                Arrange(getSongsList);
                MediaOnline.getInstance().setGetSongsList(getSongsList);
                songOnlineAdapter.setGetSongs(getSongsList);
                LoadingDialog.getInstance().StopDialog();
                MusicOnline musicOnline = (MusicOnline) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                musicOnline.loadMediaPlay(MediaOnline.getInstance().getPosition());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                LoadingDialog.getInstance().StopDialog();
            }
            });
        songOnlineAdapter.setClickSong(new SongOnlineAdapter.clickSongsOnline() {
            @Override
            public void clickSongsOnline(int position) {
                if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
                    MyMediaPlayer.getInstance().stopAudioFile();
                }
                if(!MediaOnline.getInstance().isCheckStop()){
                    MediaOnline.getInstance().stopPlaySong();
                }
                MediaOnline.getInstance().setCheckSongAlbum(false);
                MediaOnline.getInstance().setCheckSongArtist(false);
                Intent i = new Intent(getContext(), playSong_Online.class);
                i.putExtra("idSong",position);
                startActivityForResult(i,10);
                getActivity().overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10 && resultCode== Activity.RESULT_OK){
            MusicOnline musicOnline = (MusicOnline) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
            musicOnline.loadMediaPlay(MediaOnline.getInstance().getPosition());
            if(MediaOnline.getInstance().getMediaPlayer()!=null){
                musicOnline.completeSong();
            }
        }
    }

    public void Arrange(List<GetSongs> myListSong){
        Collections.sort(myListSong, new Comparator<GetSongs>() {
            @Override
            public int compare(GetSongs mySongObject, GetSongs t1) {
                return mySongObject.getSongTitle().compareToIgnoreCase(t1.getSongTitle());
            }
        });
    }
}