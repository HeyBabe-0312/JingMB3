package com.example.jingmb3.view.online.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivitySongOfAlbumOnlBinding;
import com.example.jingmb3.databinding.ActivitySongOfArtistOnlBinding;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.online.GetSongs;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.activity.adapter.SongOfAlbumOnlAdapter;
import com.example.jingmb3.view.activity.adapter.SongOfArtistOnlAdapter;
import com.example.jingmb3.view.offline.activity.PlayerSong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SongOfArtistOnl extends AppCompatActivity {
    DatabaseReference databaseReference;
    List<GetSongs> getSongsList;
    ActivitySongOfArtistOnlBinding binding;
    SongOfArtistOnlAdapter adapter;
    private String s="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySongOfArtistOnlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSongsList = new ArrayList<>();
        adapter = new SongOfArtistOnlAdapter(this);
        adapter.setData(getSongsList);
        binding.rcView.setLayoutManager(new LinearLayoutManager(this));
        binding.rcView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance("https://musicupload-7dde0-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("songs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getSongsList.clear();
                for(DataSnapshot dss:snapshot.getChildren()){
                    GetSongs getSongs = dss.getValue(GetSongs.class);
                    getSongs.setmKey(dss.getKey());
                    s = getIntent().getExtras().getString("name");
                    if(s.equals(getSongs.getArtist())){
                        getSongsList.add(getSongs);
                    }
                }
                binding.NameArtistOnl.setText(s);
                binding.NameArtistOnl.setSelected(true);
                final String url = getIntent().getExtras().getString("url");
                if(url!=null && !url.equals("")){
                    Glide.with(SongOfArtistOnl.this)
                            .asBitmap()
                            .load(url)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    binding.ImgArtistOnl.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                }
                binding.countSong.setText(getSongsList.size()+" Bài hát");
                Arrange(getSongsList);
                adapter.setData(getSongsList);
//                MusicOnline musicOnline = (MusicOnline) SongOfAlbumOnl.this.getSupportFragmentManager().findFragmentById(R.id.frame_content);
//                musicOnline.loadMediaPlay(MediaOnline.getInstance().getPosition());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaOnline.getInstance().setGetSongArtistList(getSongsList);
                if(!MediaOnline.getInstance().isCheckStop()){
                    MediaOnline.getInstance().stopPlaySong();
                }
                Intent intent=new Intent(SongOfArtistOnl.this, playSong_Online.class);
                intent.putExtra("pos",0);
                if(!MediaOnline.getInstance().isCheckStop()) MediaOnline.getInstance().stopPlaySong();
                MediaOnline.getInstance().setCheckSongAlbum(false);
                MediaOnline.getInstance().setCheckSongArtist(true);
                MediaOnline.getInstance().setPosition(0);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
            }
        });
        adapter.ClickToPlay(new SongOfArtistOnlAdapter.ClickItemToPlay() {
            @Override
            public void clickToPlay(int position) {
                MediaOnline.getInstance().setGetSongArtistList(getSongsList);
                if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
                    MyMediaPlayer.getInstance().stopAudioFile();
                }
                if(!MediaOnline.getInstance().isCheckStop()){
                    MediaOnline.getInstance().stopPlaySong();
                }
                MediaOnline.getInstance().setCheckSongAlbum(false);
                MediaOnline.getInstance().setCheckSongArtist(true);
                MediaOnline.getInstance().setPosition(position);
                Intent i = new Intent(SongOfArtistOnl.this, playSong_Online.class);
                i.putExtra("idSong",position);
                startActivityForResult(i,10);
                SongOfArtistOnl.this.overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
            }
        });
    }
    public void Arrange(List<GetSongs> myListSong){
        Collections.sort(myListSong, new Comparator<GetSongs>() {
            @Override
            public int compare(GetSongs mySongObject, GetSongs t1) {
                return mySongObject.getSongTitle().compareToIgnoreCase(t1.getSongTitle());
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_OK);
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
    }
}