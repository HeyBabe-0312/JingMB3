package com.example.jingmb3.view.offline.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivityPlayerSongBinding;
import com.example.jingmb3.model.offline.FavoriteDatabase;
import com.example.jingmb3.model.offline.FavoriteObject;
import com.example.jingmb3.model.offline.MyAlbumDatabase;
import com.example.jingmb3.model.offline.MyAlbumObject;
import com.example.jingmb3.model.offline.MyArtistDatabase;
import com.example.jingmb3.model.offline.MyArtistObject;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.offline.MySongsDatabase;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.offline.fragment.MyMusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.core.Flowable;

public class PlayerSong extends AppCompatActivity {

    private  Bitmap bitmap=null;
    private ActivityPlayerSongBinding binding;
    private ArrayList<MySongObject> myListSong;
    private int position;
    boolean fav=false;
    Thread updateSeekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Sự kiện khi nhấn nút đóng màn hình chơi nhạc
        binding.closePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, R.anim.slide_down_out);
            }
        });

        //Kiểm tra chế độ lặp lại đang bật hay tắt để set background cho nút tương ứng
        if(MyMediaPlayer.getInstance().isCheckRepeat())
            binding.repeatBtn.setImageResource(R.drawable.ic_current_repeat);
        //Sự kiện nhấn nút lặp lại
        binding.repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MyMediaPlayer.getInstance().isCheckRepeat()){
                    binding.repeatBtn.setImageResource(R.drawable.ic_current_repeat);
                    MyMediaPlayer.getInstance().setCheckRepeat(true);
                    Toast.makeText(PlayerSong.this,"Lặp lại hiện tại",Toast.LENGTH_SHORT).show();
                }else {
                    binding.repeatBtn.setImageResource(R.drawable.ic_repeat);
                    MyMediaPlayer.getInstance().setCheckRepeat(false);
                    Toast.makeText(PlayerSong.this,"Lặp lại BẬT",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Kiểm tra chế độ ngẫu nhiên đang bật hay tắt để set background cho nút tương ứng
        if(MyMediaPlayer.getInstance().isCheckRandom())
            binding.randomBtn.setImageResource(R.drawable.ic_shuffle_on);
        //Sự kiện nhấn nút ngẫu nhiên
        binding.randomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MyMediaPlayer.getInstance().isCheckRandom()){
                    binding.randomBtn.setImageResource(R.drawable.ic_shuffle_on);
                    MyMediaPlayer.getInstance().setCheckRandom(true);
                    Toast.makeText(PlayerSong.this,"Phát ngẫu nhiên BẬT",Toast.LENGTH_SHORT).show();
                }else {
                    binding.randomBtn.setImageResource(R.drawable.ic_shuffle);
                    MyMediaPlayer.getInstance().setCheckRandom(false);
                    Toast.makeText(PlayerSong.this,"Phát ngẫu nhiên TẮT",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //KIỂM TRA DANH SÁCH CHƠI NHẠC
        //Kiểm tra danh sách nhạc đang phát trong một album
        if(MyMediaPlayer.getInstance().isCheckSongAlbum()){
            int IdAlbum=MyMediaPlayer.getInstance().getIdAlbum();
            MyAlbumObject myAlbumObject=MyAlbumDatabase.getInstance(this).myAlbumDAO().getAlbumById(IdAlbum);
            ArrayList<String> listIdAlbum=new ArrayList<>();
            listIdAlbum=myAlbumObject.getId_song();
            myListSong=new ArrayList<>();
            for(String id:listIdAlbum) {
                myListSong.add(MySongsDatabase.getInstance(this).mySongsDAO().getMySongByID(Integer.valueOf(id)));
            }
            if(myListSong.isEmpty()) return;
            Arrange(myListSong);
            MyMediaPlayer.getInstance().setListPlaySong(myListSong);
        }
        //Kiểm tra danh sách nhạc đang phát trong một nghệ sĩ
        else if(MyMediaPlayer.getInstance().isCheckSongArtist()){
            int IdArtist=MyMediaPlayer.getInstance().getIdArtist();
            MyArtistObject myArtistObject= MyArtistDatabase.getInstance(this).myArtistDAO().getArtistById(IdArtist);
            myListSong=new ArrayList<>();
            myListSong= (ArrayList<MySongObject>) MySongsDatabase.getInstance(this).mySongsDAO().getListSongByArtist(myArtistObject.getNameArtist());
            if(myListSong.isEmpty()) return;
            Arrange(myListSong);
            MyMediaPlayer.getInstance().setListPlaySong(myListSong);
        }
        //Kiểm tra danh sách nhạc đang phát trong danh sách các bài hát yêu thích
        else if(MyMediaPlayer.getInstance().isCheckFavSong()){
            myListSong=new ArrayList<>();
            List<Integer> IdSong=FavoriteDatabase.getInstance(this).favoriteDAO().getListIdSong();
            for(int id:IdSong){
                myListSong.add(MySongsDatabase.getInstance(this).mySongsDAO().getMySongByID(id));
            }
            Arrange(myListSong);
            MyMediaPlayer.getInstance().setListPlaySong(myListSong);
        }
        //Danh sách chơi nhạc là danh sách tất cả các bài hát
        else{
            myListSong= (ArrayList<MySongObject>) MySongsDatabase.getInstance(this).mySongsDAO().getListSong();
            Arrange(myListSong);
            MyMediaPlayer.getInstance().setListPlaySong(myListSong);
        }


        //Lấy vị trí của một bài hát trong danh sách phát và hiển thị lên màn hình tương ứng
        position=getIntent().getIntExtra("pos",0);
        binding.songName.setSelected(true);
        binding.songName.setText(myListSong.get(position).getNameSong());
        binding.artistName.setText(myListSong.get(position).getNameArtist());
        bitmap = BitmapFactory.decodeByteArray(myListSong.get(position).getImageSong(),
                    0,myListSong.get(position).getImageSong().length);
        binding.imageSong.setImageBitmap(bitmap);

        //Đặt màu nền màn hình chơi nhạc theo màu chủ đạo trong ảnh của bài hát
        ScreenBackground();

        //Kiểm tra xem bài hát đang phát có được yêu htisch hay không
        favoriteSong();


        //Kiểm tra logic khi đi vào màn hình chơi nhạc để phát bài hát hoặc không
        if(MyMediaPlayer.getInstance().getMediaPlayer()==null){
            if(MyMusic.isPressMiniPlay()){
                binding.playBtn.setImageResource(R.drawable.icon_play);
                MyMediaPlayer.getInstance().playAudioFile(getApplicationContext(),myListSong.get(position).getLinkSong(),position);
                MyMusic.setPressMiniPlay(false);
            }
            else{
                MyMediaPlayer.getInstance().playAudioFile(getApplicationContext(),myListSong.get(position).getLinkSong(),position);
                MyMediaPlayer.getInstance().Start();
                imageAnimation();
                binding.playBtn.setImageResource(R.drawable.icon_pause);
            }
        }
        else if(MyMediaPlayer.getInstance().isCheckStopMedia()){
            if(MyMusic.isPressMiniPlay()){
                binding.playBtn.setImageResource(R.drawable.icon_play);
                MyMediaPlayer.getInstance().playAudioFile(getApplicationContext(),myListSong.get(position).getLinkSong(),position);
                MyMusic.setPressMiniPlay(false);
            }
            else{
                MyMediaPlayer.getInstance().playAudioFile(getApplicationContext(),myListSong.get(position).getLinkSong(),position);
                MyMediaPlayer.getInstance().Start();
                MyMediaPlayer.getInstance().setStopMedia();
                imageAnimation();
                binding.playBtn.setImageResource(R.drawable.icon_pause);
            }
        }
        else
        {
            if(MyMusic.isPressMiniPlay()) MyMusic.setPressMiniPlay(false);
            if(MyMediaPlayer.getInstance().chechMedia()){
                binding.playBtn.setImageResource(R.drawable.icon_pause);
                imageAnimation();
            }
            else{
                binding.playBtn.setImageResource(R.drawable.icon_play);
            }
        }


        //sự kiện nhấn nút chơi nhạc hoặc dừng
        binding.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyMediaPlayer.getInstance().chechMedia()){
                    binding.playBtn.setImageResource(R.drawable.icon_play);
                    binding.songName.setSelected(false);
                    MyMediaPlayer.getInstance().pauseAudioFile();
                    binding.imageSong.clearAnimation();
                }
                else{
                    if(!MediaOnline.getInstance().isCheckStop()){
                        MediaOnline.getInstance().stopPlaySong();
                    }
                    binding.playBtn.setImageResource(R.drawable.icon_pause);
                    MyMediaPlayer.getInstance().getMediaPlayer().start();
                    MyMediaPlayer.getInstance().setStopMedia();
                    binding.songName.setSelected(true);
                    imageAnimation();
                }
            }
        });

        //Xử lí chuyển phát bài hát tiếp theo khi bài hát hiện tại hoàn thành
        mediaComplete();

        //Sự kiện nhấn nút chuyển sang bài hát tiếp theo
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Kiểm tra chế độ ngẫu nhiên hay lặp lại có đang bật hay không
                if(MyMediaPlayer.getInstance().isCheckRepeat()){
                    repeatSong();
                    return;
                }
                else if(MyMediaPlayer.getInstance().isCheckRandom()){
                    RandomPlay();
                    return;
                }
                //Nếu hai chế độ trên không bật thì thực hiện chuyển phát bài hát tiếp theo
                nextSong();
            }
        });

        //Sự kiện nhấn nút chuyển sang bài hát ở trước
        binding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Kiểm tra chế độ ngẫu nhiên hay lặp lại có đang bật hay không
                if(MyMediaPlayer.getInstance().isCheckRepeat()){
                    repeatSong();
                    return;
                }
                else if(MyMediaPlayer.getInstance().isCheckRandom()){
                    RandomPlay();
                    return;
                }
                //Nếu hai chế độ trên không bật thì thực hiện chuyển phát bài hát trước đó
               previousSong();
            }
        });

        ///Cập nhật tiến trình của Seekbar
        updateSeekbar=new Thread(){
            @Override
            public void run() {
                super.run();
                int totalDuration=MyMediaPlayer.getInstance().getMediaPlayer().getDuration();
                int currentPosition=0;
                while (currentPosition<totalDuration){
                    try{
                        sleep(500);
                        currentPosition=MyMediaPlayer.getInstance().getMediaPlayer().getCurrentPosition();
                        binding.seekBar.setProgress(currentPosition);
                    }catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        Seekbar();

        //Set thời gian kết thúc bài hát
        binding.timeEnd.setText(createTime(MyMediaPlayer.getInstance().getMediaPlayer().getDuration()));
    }

    //Hàm set màu nền màn hình dựa theo màu chủ đạo trong ảnh của bài hát tương thích
    private void ScreenBackground() {
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
                    binding.favBtn.setColorFilter(Color.WHITE);
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
                    binding.favBtn.setColorFilter(Color.BLACK);
                    binding.closePlayer.setColorFilter(Color.BLACK);
                    binding.seekBar.getThumb().setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                    binding.seekBar.getThumb().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.MULTIPLY);
                    binding.seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.MULTIPLY);
                }
            }
        });
    }

    //Kiểm tra phân biệt màu sáng hay tối
    public boolean isColorDark(int color){
        return ColorUtils.calculateLuminance(color) < 0.35;
    }

    //Hàm kiểm tra bài hát đang phát được yêu thích hay không và xử lí sự kiện khi nhấn vào nsut yêu thích
    private void favoriteSong(){
        ArrayList<Integer> IdFavSong= (ArrayList<Integer>) FavoriteDatabase.getInstance(this).favoriteDAO().getListIdSong();
        if (!IdFavSong.isEmpty()){
            if(IdFavSong.contains(myListSong.get(position).getId_song())){
                binding.favBtn.setImageResource(R.drawable.ic_favorite);
                fav=true;
            }
            else {
                binding.favBtn.setImageResource(R.drawable.ic_favorite_border);
                fav=false;
            }
        }
        binding.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fav){
                    FavoriteObject favoriteObject=FavoriteDatabase.getInstance(PlayerSong.this).favoriteDAO().getMyFavSongByID
                            (myListSong.get(position).getId_song());
                    FavoriteDatabase.getInstance(PlayerSong.this).favoriteDAO().deleteSong(favoriteObject);
                    binding.favBtn.setImageResource(R.drawable.ic_favorite_border);
                    fav=false;
                    Toast.makeText(PlayerSong.this,"Đã xóa khỏi danh sách yêu thích!",Toast.LENGTH_SHORT).show();
                    MyMediaPlayer.getInstance().setCheckUpdateFavorite(true);
                    if(MyMediaPlayer.getInstance().isCheckFavSong()){
                        MyMediaPlayer.getInstance().setCheckStopFavSong(true);
                        for(MySongObject mySongObject:myListSong){
                            if(mySongObject.getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                                myListSong.remove(mySongObject);
                                break;
                            }
                        }
                        MyMediaPlayer.getInstance().setListPlaySong(myListSong);
                        MyMediaPlayer.getInstance().setPosition(0);
                        if(myListSong.size()==0) {
                            MyMediaPlayer.getInstance().setCheckFavSong(false);
                        }
                        finish();
                        overridePendingTransition(0, R.anim.slide_down_out);
                    }
                }
                else {
                    String nameSong=myListSong.get(position).getNameSong();
                    String nameArtist=myListSong.get(position).getNameArtist();
                    byte[] imgSong=myListSong.get(position).getImageSong();
                    String uriSong=myListSong.get(position).getLinkSong();
                    int IdSong=myListSong.get(position).getId_song();
                    FavoriteObject favoriteObject;
                    favoriteObject=new FavoriteObject(nameSong,nameArtist,imgSong,uriSong,IdSong);
                    FavoriteDatabase.getInstance(PlayerSong.this).favoriteDAO().insertSong(favoriteObject);
                    binding.favBtn.setImageResource(R.drawable.ic_favorite);
                    fav=true;
                    Toast.makeText(PlayerSong.this,"Đã thêm vào danh sách yêu thích!",Toast.LENGTH_SHORT).show();
                    MyMediaPlayer.getInstance().setCheckUpdateFavorite(true);
                }
            }
        });
    }

    //Hiệu ứng ảnh chuyển động quay tròn
    public void imageAnimation(){
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        binding.imageSong.startAnimation(rotateAnimation);

    }

    //Hàm tính toán thời gian bắt đầu và kết thúc của bài hát
    public String createTime(int duration){
        String time="";
        int min=duration/1000/60;
        int sec=duration/1000%60;
        time=time+min+":";
        if(sec<10){
            time+=0;
        }
        time+=sec;
        return time;
    }

    //Hàm thiết lập và quản lí thanh Seekbar khi nhạc đang chơi
    private void Seekbar(){
        //set độ rộng thanh seekbar theo tổng thời gian của bài hát
        binding.seekBar.setMax(MyMediaPlayer.getInstance().getMediaPlayer().getDuration());
        updateSeekbar.start();
        //Bắt sự kiện khi nhấn vào thanh Seekbar thay đổi
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //Set thời gian bắt đầu bài hát sẽ được cập nhật liên tục dựa theo tiến trình thay đổi của thanh Seekbar
                binding.timeStart.setText(createTime(MyMediaPlayer.getInstance().getMediaPlayer().getCurrentPosition()));
                if(b){
                    MyMediaPlayer.getInstance().getMediaPlayer().seekTo(i);
                    seekBar.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyMediaPlayer.getInstance().getMediaPlayer().seekTo(seekBar.getProgress());
            }
        });
    }

    //Hàm thực hiện chuyển phát bài hát tiếp theo
    private void nextSong(){
        if(!MediaOnline.getInstance().isCheckStop()){
            MediaOnline.getInstance().stopPlaySong();
        }
        MyMediaPlayer.getInstance().stopAudioFile();
        MyMediaPlayer.getInstance().setStopMedia();
        binding.playBtn.setImageResource(R.drawable.icon_pause);
        imageAnimation();
        binding.songName.setSelected(true);
        position=((position+1)%myListSong.size());
        MyMediaPlayer.getInstance().playAudioFile(getApplicationContext(),myListSong.get(position).getLinkSong(),position);
        MyMediaPlayer.getInstance().Start();
        favoriteSong();
        binding.seekBar.setMax(MyMediaPlayer.getInstance().getMediaPlayer().getDuration());
        mediaComplete();
        binding.timeEnd.setText(createTime(MyMediaPlayer.getInstance().getMediaPlayer().getDuration()));
        binding.songName.setText(myListSong.get(position).getNameSong());
        binding.artistName.setText(myListSong.get(position).getNameArtist());
        bitmap = BitmapFactory.decodeByteArray(myListSong.get(position).getImageSong(),
                0,myListSong.get(position).getImageSong().length);
        binding.imageSong.setImageBitmap(bitmap);
        ScreenBackground();
    }

    //Hàm thực hiện chuyển phát bài hát trước đó
    private void previousSong(){
        if(!MediaOnline.getInstance().isCheckStop()){
            MediaOnline.getInstance().stopPlaySong();
        }
        MyMediaPlayer.getInstance().stopAudioFile();
        MyMediaPlayer.getInstance().setStopMedia();
        binding.playBtn.setImageResource(R.drawable.icon_pause);
        imageAnimation();
        position=((position-1)<0?(myListSong.size()-1):position-1);
        MyMediaPlayer.getInstance().playAudioFile(getApplicationContext(),myListSong.get(position).getLinkSong(),position);
        MyMediaPlayer.getInstance().Start();
        favoriteSong();
        binding.seekBar.setMax(MyMediaPlayer.getInstance().getMediaPlayer().getDuration());
        mediaComplete();
        binding.timeEnd.setText(createTime(MyMediaPlayer.getInstance().getMediaPlayer().getDuration()));
        binding.songName.setSelected(true);
        binding.songName.setText(myListSong.get(position).getNameSong());
        binding.artistName.setText(myListSong.get(position).getNameArtist());
        bitmap = BitmapFactory.decodeByteArray(myListSong.get(position).getImageSong(),
                0,myListSong.get(position).getImageSong().length);
        binding.imageSong.setImageBitmap(bitmap);
        ScreenBackground();
    }

    //Hàm thực hiện chuyển phát bài hát lặp lại
    private void repeatSong(){
        if(!MediaOnline.getInstance().isCheckStop()){
            MediaOnline.getInstance().stopPlaySong();
        }
        MyMediaPlayer.getInstance().stopAudioFile();
        MyMediaPlayer.getInstance().setStopMedia();
        MyMediaPlayer.getInstance().playAudioFile(getApplicationContext(),myListSong.get(position).getLinkSong(),position);
        MyMediaPlayer.getInstance().Start();
        binding.playBtn.setImageResource(R.drawable.icon_pause);
        imageAnimation();
        favoriteSong();
        binding.seekBar.setMax(MyMediaPlayer.getInstance().getMediaPlayer().getDuration());
        mediaComplete();
        binding.timeEnd.setText(createTime(MyMediaPlayer.getInstance().getMediaPlayer().getDuration()));
        binding.songName.setSelected(true);
    }

    //Hàm thực hiện chuyển phát bài hát ngẫu nhiên
    private void RandomPlay(){
        if(!MediaOnline.getInstance().isCheckStop()){
            MediaOnline.getInstance().stopPlaySong();
        }
        MyMediaPlayer.getInstance().stopAudioFile();
        MyMediaPlayer.getInstance().setStopMedia();
        Random random=new Random();
        int valueRandom=0;
        do{
            valueRandom=random.nextInt(myListSong.size());
        }while(valueRandom==position);
        position=valueRandom;
        MyMediaPlayer.getInstance().playAudioFile(getApplicationContext(),myListSong.get(position).getLinkSong(),position);
        MyMediaPlayer.getInstance().Start();
        binding.playBtn.setImageResource(R.drawable.icon_pause);
        imageAnimation();
        favoriteSong();
        binding.seekBar.setMax(MyMediaPlayer.getInstance().getMediaPlayer().getDuration());
        mediaComplete();
        binding.timeEnd.setText(createTime(MyMediaPlayer.getInstance().getMediaPlayer().getDuration()));
        binding.songName.setSelected(true);
        binding.songName.setText(myListSong.get(position).getNameSong());
        binding.artistName.setText(myListSong.get(position).getNameArtist());
        bitmap = BitmapFactory.decodeByteArray(myListSong.get(position).getImageSong(),
                0,myListSong.get(position).getImageSong().length);
        binding.imageSong.setImageBitmap(bitmap);
        ScreenBackground();
    }

    //Hàm xử lí chuyển phát bài hát tiếp theo khi bài hát hiện tại đã hoàn thành
    public void mediaComplete(){
        MyMediaPlayer.getInstance().getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                if(MyMediaPlayer.getInstance().isCheckRepeat()){
                   repeatSong();
                   return;
                }
                else if(MyMediaPlayer.getInstance().isCheckRandom()){
                    RandomPlay();
                    return;
                }
                else binding.nextBtn.performClick();
            }
        });
    }

    //Hàm sắp xếp danh sách phát nhạc theo thứ tự
    public void Arrange(ArrayList<MySongObject> myListSong){
        Collections.sort(myListSong, new Comparator<MySongObject>() {
            @Override
            public int compare(MySongObject mySongObject, MySongObject t1) {
                return mySongObject.getNameSong().compareToIgnoreCase(t1.getNameSong());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("pos",position);
        setResult(23,intent);
        finish();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
    }
}