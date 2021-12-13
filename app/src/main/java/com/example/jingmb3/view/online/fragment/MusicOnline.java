package com.example.jingmb3.view.online.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jingmb3.R;
import com.example.jingmb3.databinding.FragmentMusicOnlineBinding;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.online.GetSongs;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.offline.fragment.MyMusic;
import com.example.jingmb3.view.online.activity.playSong_Online;
import com.example.jingmb3.view.online.fragment.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicOnline extends Fragment {
    private List<GetSongs> getSongsList;
    private FragmentMusicOnlineBinding binding;
    private int position = 0;
    public static boolean pressMiniPlay = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentMusicOnlineBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public static boolean isPressMiniPlay() {
        return pressMiniPlay;
    }

    public static void setPressMiniPlay(boolean pressMiniPlay) {
        MusicOnline.pressMiniPlay = pressMiniPlay;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSongsList = new ArrayList<>();
        getSongsList = MediaOnline.getInstance().getGetSongsList();
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getActivity());
        binding.viewpagerMyMusic.setAdapter(viewPagerAdapter);
        binding.miniArtist.setSelected(true);
        binding.miniSName.setSelected(true);
        binding.viewpagerMyMusic.setOffscreenPageLimit(2);
        new TabLayoutMediator(binding.tablayoutMyMS, binding.viewpagerMyMusic, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Bài hát");
                        tab.setIcon(R.drawable.ic_music_search);
                        break;
                    case 1:
                        tab.setText("Album");
                        tab.setIcon(R.drawable.ic_album_search);
                        break;
                    case 2:
                        tab.setText("Nghệ sĩ");
                        tab.setIcon(R.drawable.ic_artist_search);
                        break;
                }
            }
        }).attach();
        binding.playBtnMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MediaOnline.getInstance().isCheckStop()){
                    if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
                        MyMediaPlayer.getInstance().stopAudioFile();
                    }
                    binding.playBtnMini.setImageResource(R.drawable.ic_pause_mini);
                    MediaOnline.getInstance().playSongOnline(getContext(),getSongsList.get(position).getSongLink(),position);
                    MediaOnline.getInstance().startPlaySong();
                    MediaOnline.getInstance().setCheckStop(false);
                }
                else if(MediaOnline.getInstance().checkPlaying()){
                    binding.playBtnMini.setImageResource(R.drawable.ic_play_mini);
                    MediaOnline.getInstance().pauseSong();
                    binding.imgSongMini.clearAnimation();
                }
                else{
                    if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
                        MyMediaPlayer.getInstance().stopAudioFile();
                    }
                    if(MediaOnline.getInstance().getMediaPlayer()!=null){
                        binding.playBtnMini.setImageResource(R.drawable.ic_pause_mini);
                        MediaOnline.getInstance().getMediaPlayer().start();
                        ImageAnimation();
                    }
                    else{
                        binding.playBtnMini.setImageResource(R.drawable.ic_pause_mini);
                        MediaOnline.getInstance().playSongOnline(getContext(),getSongsList.get(position).getSongLink(),position);
                        MediaOnline.getInstance().startPlaySong();
                        ImageAnimation();
                    }
                }
            }
        });
        binding.nextBtnMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MediaOnline.getInstance().isCheckRepeat()){
                    reloopSong();
                    return;
                }
                else if(MediaOnline.getInstance().isCheckRand()){
                    randomSong();
                    return;
                }
                nextSong();
            }
        });
        binding.prevBtnMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MediaOnline.getInstance().isCheckRepeat()){
                    reloopSong();
                    return;
                }
                else if(MediaOnline.getInstance().isCheckRand()){
                    randomSong();
                    return;
                }
                previSong();
            }
        });
        binding.miniPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), playSong_Online.class);
                i.putExtra("idSong",MediaOnline.getInstance().getPosition());
                startActivityForResult(i,10);
                getActivity().overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                pressMiniPlay = true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10 && resultCode== Activity.RESULT_OK){
            loadMediaPlay(MediaOnline.getInstance().getPosition());
            if(MediaOnline.getInstance().getMediaPlayer()!=null) completeSong();
        }
    }

    private void ImageAnimation(){
        RotateAnimation rotateAnimation = new RotateAnimation(0,360f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        binding.imgSongMini.startAnimation(rotateAnimation);
    }
    public void loadMediaPlay(int position){
        if(MediaOnline.getInstance().isCheckSongAlbum()) getSongsList = MediaOnline.getInstance().getGetSongAlbumList();
        else if (MediaOnline.getInstance().isCheckSongArtist()) getSongsList = MediaOnline.getInstance().getGetSongArtistList();
        else getSongsList = MediaOnline.getInstance().getGetSongsList();
        if(getSongsList.size()!=0&&getSongsList!=null) {
            this.position = position;
            binding.miniSName.setText(getSongsList.get(position).getSongTitle());
            binding.miniArtist.setText((getSongsList).get(position).getArtist());
            binding.miniArtist.setSelected(true);
            binding.miniSName.setSelected(true);
            if (getSongsList.get(position).getAlbum_art() != null && !getSongsList.get(position).getAlbum_art().equals("")) {
                Glide.with(this)
                        .asBitmap()
                        .load(getSongsList.get(position).getAlbum_art())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                binding.imgSongMini.setImageBitmap(resource);
                                MediaOnline.getInstance().setUrlImage(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            } else {
                binding.imgSongMini.setImageResource(R.drawable.icon_music);
            }
            if (MediaOnline.getInstance().isCheckStop()) {
                binding.playBtnMini.setImageResource(R.drawable.ic_play_mini);
                binding.imgSongMini.clearAnimation();
                return;
            }
            if (MediaOnline.getInstance().checkPlaying()) {
                ImageAnimation();
                binding.playBtnMini.setImageResource(R.drawable.ic_pause_mini);
            } else {
                binding.playBtnMini.setImageResource(R.drawable.ic_play_mini);
                binding.imgSongMini.clearAnimation();
            }
        }
        else{
            binding.miniPlay.setVisibility(View.INVISIBLE);
        }
    }

    public void completeSong(){
        MediaOnline.getInstance().getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                if(MediaOnline.getInstance().isCheckRepeat()){
                    reloopSong();
                    return;
                }else if (MediaOnline.getInstance().isCheckRand()){
                    randomSong();
                    return;
                }else {
                    binding.nextBtnMini.performClick();
                }
            }
        });
    }private void nextSong(){
        ImageAnimation();
        MediaOnline.getInstance().stopPlaySong();
        if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
            MyMediaPlayer.getInstance().stopAudioFile();
        }
        MediaOnline.getInstance().setCheckStop(false);
        binding.playBtnMini.setImageResource(R.drawable.ic_pause_mini);
        position = (position+1)%getSongsList.size();
        MediaOnline.getInstance().playSongOnline(getContext(),getSongsList.get(position).getSongLink(),position);
        MediaOnline.getInstance().startPlaySong();
        completeSong();
        binding.miniSName.setText(getSongsList.get(position).getSongTitle());
        binding.miniArtist.setText(getSongsList.get(position).getArtist());
        if(getSongsList.get(position).getAlbum_art()!=null&&!getSongsList.get(position).getAlbum_art().equals("")){
            Glide.with(this)
                    .asBitmap()
                    .load(getSongsList.get(position).getAlbum_art())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.imgSongMini.setImageBitmap(resource);
                            MediaOnline.getInstance().setUrlImage(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        else{
            binding.imgSongMini.setImageResource(R.drawable.icon_music);
        }
    }
    private void previSong(){
        ImageAnimation();
        MediaOnline.getInstance().stopPlaySong();
        if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
            MyMediaPlayer.getInstance().stopAudioFile();
        }
        MediaOnline.getInstance().setCheckStop(false);
        binding.playBtnMini.setImageResource(R.drawable.ic_pause_mini);
        position = (position-1)<0?(getSongsList.size()-1):(position-1);
        MediaOnline.getInstance().playSongOnline(getContext(),getSongsList.get(position).getSongLink(),position);
        MediaOnline.getInstance().startPlaySong();
        completeSong();
        binding.miniSName.setText(getSongsList.get(position).getSongTitle());
        binding.miniArtist.setText(getSongsList.get(position).getArtist());
        if(getSongsList.get(position).getAlbum_art()!=null&&!getSongsList.get(position).getAlbum_art().equals("")){
            Glide.with(this)
                    .asBitmap()
                    .load(getSongsList.get(position).getAlbum_art())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.imgSongMini.setImageBitmap(resource);
                            MediaOnline.getInstance().setUrlImage(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        else{
            binding.imgSongMini.setImageResource(R.drawable.icon_music);
        }
    }
    private void randomSong(){
        ImageAnimation();
        if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
            MyMediaPlayer.getInstance().stopAudioFile();
        }
        MediaOnline.getInstance().stopPlaySong();
        MediaOnline.getInstance().setCheckStop(false);
        Random random = new Random();
        int valueRand = 0;
        do{
            valueRand = random.nextInt(getSongsList.size());
        }while (valueRand==position);
        position = valueRand;
        MediaOnline.getInstance().playSongOnline(getContext(),getSongsList.get(position).getSongLink(),position);
        MediaOnline.getInstance().startPlaySong();
        completeSong();
        binding.playBtnMini.setImageResource(R.drawable.ic_pause_mini);
        binding.miniSName.setText(getSongsList.get(position).getSongTitle());
        binding.miniArtist.setText(getSongsList.get(position).getArtist());
        if(getSongsList.get(position).getAlbum_art()!=null&&!getSongsList.get(position).getAlbum_art().equals("")){
            Glide.with(this)
                    .asBitmap()
                    .load(getSongsList.get(position).getAlbum_art())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.imgSongMini.setImageBitmap(resource);
                            MediaOnline.getInstance().setUrlImage(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        else{
            binding.imgSongMini.setImageResource(R.drawable.icon_music);
        }
    }
    private void reloopSong(){
        ImageAnimation();
        if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
            MyMediaPlayer.getInstance().stopAudioFile();
        }
        MediaOnline.getInstance().stopPlaySong();
        MediaOnline.getInstance().setCheckStop(false);
        binding.playBtnMini.setImageResource(R.drawable.ic_pause_mini);
        MediaOnline.getInstance().playSongOnline(getContext(),getSongsList.get(position).getSongLink(),position);
        MediaOnline.getInstance().startPlaySong();
        completeSong();
        if(getSongsList.get(position).getAlbum_art()!=null&&!getSongsList.get(position).getAlbum_art().equals("")){
            Glide.with(this)
                    .asBitmap()
                    .load(getSongsList.get(position).getAlbum_art())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.imgSongMini.setImageBitmap(resource);
                            MediaOnline.getInstance().setUrlImage(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        else{
            binding.imgSongMini.setImageResource(R.drawable.icon_music);
        }
    }
}