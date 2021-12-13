package com.example.jingmb3.view.online.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivityPlaySongOnlineBinding;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.online.GetSongs;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.online.fragment.MusicOnline;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class playSong_Online extends AppCompatActivity {
    ActivityPlaySongOnlineBinding binding;
    List<GetSongs> getSongsList;
    Thread updateSeekBar;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaySongOnlineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSongsList = new ArrayList<>();
        position = getIntent().getIntExtra("idSong",0);
        if(MediaOnline.getInstance().isCheckSongAlbum()) getSongsList = MediaOnline.getInstance().getGetSongAlbumList();
        else if(MediaOnline.getInstance().isCheckSongArtist()) getSongsList = MediaOnline.getInstance().getGetSongArtistList();
        else getSongsList = MediaOnline.getInstance().getGetSongsList();
        binding.artistName.setText(getSongsList.get(position).getArtist());
        binding.songName.setText(getSongsList.get(position).getSongTitle());
        binding.songName.setSelected(true);
        binding.artistName.setSelected(true);
        if(getSongsList.get(position).getAlbum_art()!=null && !getSongsList.get(position).getAlbum_art().equals("")){
            Glide.with(this)
                    .asBitmap()
                    .load(getSongsList.get(position).getAlbum_art())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.imageSong.setImageBitmap(resource);
                            MediaOnline.getInstance().setUrlImage(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }else{
            Bitmap bitmap = ((BitmapDrawable) binding.imageSong.getDrawable()).getBitmap();
            MediaOnline.getInstance().setUrlImage(bitmap);
        }
        if(MediaOnline.getInstance().getMediaPlayer()==null){
            if(MusicOnline.isPressMiniPlay()){
                binding.playBtn.setImageResource(R.drawable.icon_play);
                MediaOnline.getInstance().playSongOnline(this,getSongsList.get(position).getSongLink(),position);
                MusicOnline.setPressMiniPlay(false);
            }
            else{
                ImageAnimation();
                MediaOnline.getInstance().playSongOnline(this,getSongsList.get(position).getSongLink(),position);
                MediaOnline.getInstance().startPlaySong();
                binding.playBtn.setImageResource(R.drawable.icon_pause);
            }
        }
        else if (MediaOnline.getInstance().isCheckStop()){
           if(MusicOnline.isPressMiniPlay()){
               binding.playBtn.setImageResource(R.drawable.icon_play);
               MediaOnline.getInstance().playSongOnline(this,getSongsList.get(position).getSongLink(),position);
               MusicOnline.setPressMiniPlay(false);
           }
           else{
               MediaOnline.getInstance().setCheckStop(false);
               binding.playBtn.setImageResource(R.drawable.icon_pause);
               ImageAnimation();
               MediaOnline.getInstance().playSongOnline(this,getSongsList.get(position).getSongLink(),position);
               MediaOnline.getInstance().startPlaySong();
           }
        }
        else {
            if(MusicOnline.isPressMiniPlay()){
                MusicOnline.setPressMiniPlay(false);
            }
            if(MediaOnline.getInstance().checkPlaying()){
                binding.playBtn.setImageResource(R.drawable.icon_pause);
                ImageAnimation();
            }
            else{
                binding.playBtn.setImageResource(R.drawable.icon_play);
            }
        }
        binding.closePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
            }
        });
        updateSeekBar = new Thread(){
            @Override
            public void run() {
                super.run();
                int totalDuration = MediaOnline.getInstance().getMediaPlayer().getDuration();
                int currentPos = 0;
                while (currentPos < totalDuration){
                    try {
                        sleep(500);
                        currentPos = MediaOnline.getInstance().getMediaPlayer().getCurrentPosition();
                        binding.seekBar.setProgress(currentPos);
                    }catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        Seekbar();
        ScreenBackground();
        completeSong();
        binding.timeEnd.setText(createTime(MediaOnline.getInstance().getMediaPlayer().getDuration()));
        binding.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MediaOnline.getInstance().checkPlaying()){
                    binding.playBtn.setImageResource(R.drawable.icon_play);
                    MediaOnline.getInstance().pauseSong();
                    binding.imageSong.clearAnimation();
                }
                else {
                    if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
                        MyMediaPlayer.getInstance().stopAudioFile();
                    }
                    ImageAnimation();
                    binding.playBtn.setImageResource(R.drawable.icon_pause);
                    MediaOnline.getInstance().getMediaPlayer().start();
                    MediaOnline.getInstance().setCheckStop(false);
                }
            }
        });
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MediaOnline.getInstance().isCheckRepeat()){
                    reloopSong();
                    return;
                }
                if(MediaOnline.getInstance().isCheckRand()){
                    randomSong();
                    return;
                }
                nextSong();
            }
        });
        binding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MediaOnline.getInstance().isCheckRepeat()){
                    reloopSong();
                    return;
                }
                if(MediaOnline.getInstance().isCheckRand()){
                    randomSong();
                    return;
                }
                previSong();
            }
        });
        if(MediaOnline.getInstance().isCheckRand()){
            binding.randomBtn.setImageResource(R.drawable.ic_shuffle_on);
        }
        if(MediaOnline.getInstance().isCheckRepeat()){
            binding.repeatBtn.setImageResource(R.drawable.ic_current_repeat);
        }
        binding.randomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MediaOnline.getInstance().isCheckRand()){
                    binding.randomBtn.setImageResource(R.drawable.ic_shuffle_on);
                    MediaOnline.getInstance().setCheckRand(true);
                    Toast.makeText(playSong_Online.this,"Phát ngẫu nhiên BẬT",Toast.LENGTH_SHORT).show();
                }else {
                    binding.randomBtn.setImageResource(R.drawable.ic_shuffle);
                    MediaOnline.getInstance().setCheckRand(false);
                    Toast.makeText(playSong_Online.this,"Phát ngẫu nhiên TẮT",Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MediaOnline.getInstance().isCheckRepeat()){
                    binding.repeatBtn.setImageResource(R.drawable.ic_current_repeat);
                    MediaOnline.getInstance().setCheckRepeat(true);
                    Toast.makeText(playSong_Online.this,"Lặp bài hát hiện tại BẬT",Toast.LENGTH_SHORT).show();
                }else {
                    binding.repeatBtn.setImageResource(R.drawable.ic_repeat);
                    MediaOnline.getInstance().setCheckRepeat(false);
                    Toast.makeText(playSong_Online.this,"Lặp bài hát hiện tại TẮT",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void ScreenBackground() {
        Bitmap bitmap = MediaOnline.getInstance().getUrlImage();
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    int color=palette.getDominantColor(1);
                    binding.screenPlay.setBackgroundColor(color);
                    binding.screenPlay.getBackground().setAlpha(185);
                    int color1=((ColorDrawable) binding.screenPlay.getBackground()).getColor();
                    binding.screenPlay.getBackground().setAlpha(230);
                    int color2=((ColorDrawable) binding.screenPlay.getBackground()).getColor();
                    int[] colors = {color2,color1};


                    GradientDrawable gd = new GradientDrawable(
                            GradientDrawable.Orientation.BOTTOM_TOP, colors);

                    binding.screenPlay.setBackground(gd);
                    if(isColorDark(color2)) {
                        binding.playBtn.setColorFilter(Color.WHITE);
                        binding.nextBtn.setColorFilter(Color.WHITE);
                        binding.prevBtn.setColorFilter(Color.WHITE);
                        binding.randomBtn.setColorFilter(Color.WHITE);
                        binding.repeatBtn.setColorFilter(Color.WHITE);
                        binding.timeStart.setTextColor(Color.WHITE);
                        binding.timeEnd.setTextColor(Color.WHITE);
                        binding.songName.setTextColor(Color.WHITE);
                        binding.artistName.setTextColor(Color.WHITE);
                        binding.textPlay.setTextColor(Color.WHITE);
                        binding.closePlayer.setColorFilter(Color.WHITE);
                        binding.seekBar.getThumb().setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                        binding.seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
                    }else {
                        binding.playBtn.setColorFilter(Color.BLACK);
                        binding.nextBtn.setColorFilter(Color.BLACK);
                        binding.prevBtn.setColorFilter(Color.BLACK);
                        binding.randomBtn.setColorFilter(Color.BLACK);
                        binding.repeatBtn.setColorFilter(Color.BLACK);
                        binding.timeStart.setTextColor(Color.BLACK);
                        binding.timeEnd.setTextColor(Color.BLACK);
                        binding.songName.setTextColor(Color.BLACK);
                        binding.artistName.setTextColor(Color.BLACK);
                        binding.textPlay.setTextColor(Color.BLACK);
                        binding.closePlayer.setColorFilter(Color.BLACK);
                        binding.seekBar.getThumb().setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                        binding.seekBar.getThumb().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.MULTIPLY);
                        binding.seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.MULTIPLY);
                    }
                }
            });
    }
    public boolean isColorDark(int color){
        return ColorUtils.calculateLuminance(color) < 0.35;
    }
    private void ImageAnimation(){
        RotateAnimation rotateAnimation = new RotateAnimation(0,360f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        binding.imageSong.startAnimation(rotateAnimation);
    }
    private void nextSong(){
        ImageAnimation();
        if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
            MyMediaPlayer.getInstance().stopAudioFile();
        }
        MediaOnline.getInstance().stopPlaySong();
        MediaOnline.getInstance().setCheckStop(false);
        binding.playBtn.setImageResource(R.drawable.icon_pause);
        position = (position+1)%getSongsList.size();
        MediaOnline.getInstance().playSongOnline(this,getSongsList.get(position).getSongLink(),position);
        MediaOnline.getInstance().startPlaySong();
        completeSong();
        binding.seekBar.setMax(MediaOnline.getInstance().getMediaPlayer().getDuration());
        binding.timeEnd.setText(createTime(MediaOnline.getInstance().getMediaPlayer().getDuration()));
        binding.songName.setText(getSongsList.get(position).getSongTitle());
        binding.artistName.setText(getSongsList.get(position).getArtist());
        if(getSongsList.get(position).getAlbum_art()!=null&&!getSongsList.get(position).getAlbum_art().equals("")){
            Glide.with(this)
                    .asBitmap()
                    .load(getSongsList.get(position).getAlbum_art())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.imageSong.setImageBitmap(resource);
                            MediaOnline.getInstance().setUrlImage(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        else{
            binding.imageSong.setImageResource(R.drawable.icon_music);
            MediaOnline.getInstance().setUrlImage(((BitmapDrawable) binding.imageSong.getDrawable()).getBitmap());
        }
        ScreenBackground();
    }
    private void previSong(){
        ImageAnimation();
        if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
            MyMediaPlayer.getInstance().stopAudioFile();
        }
        MediaOnline.getInstance().stopPlaySong();
        MediaOnline.getInstance().setCheckStop(false);
        binding.playBtn.setImageResource(R.drawable.icon_pause);
        position = (position-1)<0?(getSongsList.size()-1):(position-1);
        MediaOnline.getInstance().playSongOnline(this,getSongsList.get(position).getSongLink(),position);
        MediaOnline.getInstance().startPlaySong();
        binding.seekBar.setMax(MediaOnline.getInstance().getMediaPlayer().getDuration());
        completeSong();
        binding.timeEnd.setText(createTime(MediaOnline.getInstance().getMediaPlayer().getDuration()));
        binding.songName.setText(getSongsList.get(position).getSongTitle());
        binding.artistName.setText(getSongsList.get(position).getArtist());
        if(getSongsList.get(position).getAlbum_art()!=null&&!getSongsList.get(position).getAlbum_art().equals("")){
            Glide.with(this)
                    .asBitmap()
                    .load(getSongsList.get(position).getAlbum_art())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.imageSong.setImageBitmap(resource);
                            MediaOnline.getInstance().setUrlImage(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        else{
            binding.imageSong.setImageResource(R.drawable.icon_music);
            MediaOnline.getInstance().setUrlImage(((BitmapDrawable) binding.imageSong.getDrawable()).getBitmap());
        }
        ScreenBackground();
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
        }while (position!=0&&valueRand==position);
        position = valueRand;
        MediaOnline.getInstance().playSongOnline(this,getSongsList.get(position).getSongLink(),position);
        MediaOnline.getInstance().startPlaySong();
        binding.seekBar.setMax(MediaOnline.getInstance().getMediaPlayer().getDuration());
        completeSong();
        binding.playBtn.setImageResource(R.drawable.icon_pause);
        binding.timeEnd.setText(createTime(MediaOnline.getInstance().getMediaPlayer().getDuration()));
        binding.songName.setText(getSongsList.get(position).getSongTitle());
        binding.artistName.setText(getSongsList.get(position).getArtist());
        if(getSongsList.get(position).getAlbum_art()!=null&&!getSongsList.get(position).getAlbum_art().equals("")){
            Glide.with(this)
                    .asBitmap()
                    .load(getSongsList.get(position).getAlbum_art())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.imageSong.setImageBitmap(resource);
                            MediaOnline.getInstance().setUrlImage(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        else{
            binding.imageSong.setImageResource(R.drawable.icon_music);
            MediaOnline.getInstance().setUrlImage(((BitmapDrawable) binding.imageSong.getDrawable()).getBitmap());
        }
        ScreenBackground();
    }
    private void reloopSong(){
        ImageAnimation();
        if(!MyMediaPlayer.getInstance().isCheckStopMedia()){
            MyMediaPlayer.getInstance().stopAudioFile();
        }
        MediaOnline.getInstance().stopPlaySong();
        MediaOnline.getInstance().setCheckStop(false);
        binding.playBtn.setImageResource(R.drawable.icon_pause);
        MediaOnline.getInstance().playSongOnline(this,getSongsList.get(position).getSongLink(),position);
        MediaOnline.getInstance().startPlaySong();
        binding.seekBar.setMax(MediaOnline.getInstance().getMediaPlayer().getDuration());
        completeSong();
        if(getSongsList.get(position).getAlbum_art()!=null&&!getSongsList.get(position).getAlbum_art().equals("")){
            Glide.with(this)
                    .asBitmap()
                    .load(getSongsList.get(position).getAlbum_art())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.imageSong.setImageBitmap(resource);
                            MediaOnline.getInstance().setUrlImage(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        else{
            binding.imageSong.setImageResource(R.drawable.icon_music);
            MediaOnline.getInstance().setUrlImage(((BitmapDrawable) binding.imageSong.getDrawable()).getBitmap());
        }
        ScreenBackground();
    }
    private void completeSong(){
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
                    nextSong();
                }
            }
        });
    }
    public String createTime(int duration){
        String time = "";
        int min = duration/1000/60;
        int second = duration/1000%60;
        time = String.format("%d:%02d",min,second);
        return time;
    }
    private void Seekbar() {
        binding.seekBar.setMax(MediaOnline.getInstance().getMediaPlayer().getDuration());
        updateSeekBar.start();
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.timeStart.setText(createTime(MediaOnline.getInstance().getMediaPlayer().getCurrentPosition()));
                if(b){
                    MediaOnline.getInstance().getMediaPlayer().seekTo(i);
                    seekBar.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MediaOnline.getInstance().getMediaPlayer().seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_OK);
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
    }
}