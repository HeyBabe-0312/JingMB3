package com.example.jingmb3.view.offline.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivitySongOfMyArtistBinding;
import com.example.jingmb3.model.offline.FavoriteDatabase;
import com.example.jingmb3.model.offline.FavoriteObject;
import com.example.jingmb3.model.offline.ListSearch;
import com.example.jingmb3.model.offline.MyArtistDatabase;
import com.example.jingmb3.model.offline.MyArtistObject;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.offline.MySongsDatabase;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.activity.adapter.SongOfArtistAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SongOfMyArtist extends AppCompatActivity {

    private MyArtistObject myArtistObject;
    private SongOfArtistAdapter songOfArtistAdapter;
    private ArrayList<MySongObject> ListSongOfArtist;
    private ArrayList<MyArtistObject> listArtist;
    ActivitySongOfMyArtistBinding binding;
    private int REQUEST_GALLERY=100;
    private ArrayList<MySongObject> FavList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySongOfMyArtistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
            }
        });
        int IdArtist=getIntent().getIntExtra("Id",0);
        myArtistObject=new MyArtistObject();
        FavList=new ArrayList<>();
        myArtistObject= MyArtistDatabase.getInstance(this).myArtistDAO().getArtistById(IdArtist);
        binding.NameArtist.setText(myArtistObject.getNameArtist());
        binding.NameArtist.setSelected(true);
        if(myArtistObject.getImageArtist()!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(myArtistObject.getImageArtist(),0,
                    myArtistObject.getImageArtist().length);
            binding.ImgArtist.setImageBitmap(bitmap);
        }

        ListSongOfArtist=new ArrayList<>();
        ListSongOfArtist= (ArrayList<MySongObject>) MySongsDatabase.getInstance(this).
                mySongsDAO().getListSongByArtist(myArtistObject.getNameArtist());
        songOfArtistAdapter=new SongOfArtistAdapter(this);
        LoadUI();
        binding.rvSongArtist.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSongArtist.setAdapter(songOfArtistAdapter);
        songOfArtistAdapter.ClickFavBtn(new SongOfArtistAdapter.ClickFavBtn() {
            @Override
            public void clickFavBtn(int ID) {
                if(FavoriteDatabase.getInstance(SongOfMyArtist.this).favoriteDAO().getListIdSong().contains(ID)){
                    FavoriteObject favoriteObject=FavoriteDatabase.getInstance(SongOfMyArtist.this).favoriteDAO()
                            .getMyFavSongByID(ID);
                    FavoriteDatabase.getInstance(SongOfMyArtist.this).favoriteDAO().deleteSong(favoriteObject);
                    Toast.makeText(SongOfMyArtist.this,"Đã xóa khỏi danh sách yêu thích!",Toast.LENGTH_SHORT).show();
                    MyMediaPlayer.getInstance().setCheckUpdateFavorite(true);
                    if(MyMediaPlayer.getInstance().isCheckFavSong()){
                        MySongObject mySongObject=MySongsDatabase.getInstance(SongOfMyArtist.this).mySongsDAO()
                                .getMySongByID(ID);
                        if(MyMediaPlayer.getInstance().getIdSong()==mySongObject.getId_song()){
                            MyMediaPlayer.getInstance().stopAudioFile();
                            FavList=MyMediaPlayer.getInstance().getListPlaySong();
                            for(int i=0;i<FavList.size();i++){
                                if(FavList.get(i).getId_song()==mySongObject.getId_song()){
                                    FavList.remove( FavList.get(i));
                                    break;
                                }
                            }
                            if(FavList.size()==0) MyMediaPlayer.getInstance().setCheckFavSong(false);
                            MyMediaPlayer.getInstance().setPosition(0);
                            MyMediaPlayer.getInstance().setListPlaySong(FavList);
                        }
                        else{
                            FavList=MyMediaPlayer.getInstance().getListPlaySong();
                            for(int i=0;i<FavList.size();i++){
                                if(FavList.get(i).getId_song()==mySongObject.getId_song()){
                                    FavList.remove( FavList.get(i));
                                    break;
                                }
                            }
                            for(int i=0;i<FavList.size();i++){
                                if(FavList.get(i).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                                    MyMediaPlayer.getInstance().setPosition(i);
                                    break;
                                }
                            }
                            MyMediaPlayer.getInstance().setListPlaySong(FavList);
                        }
                    }
                }
                else{
                    MySongObject mySongObject=MySongsDatabase.getInstance(SongOfMyArtist.this).mySongsDAO()
                            .getMySongByID(ID);
                    FavoriteObject favoriteObject=new FavoriteObject(mySongObject.getNameSong(),mySongObject.getNameArtist(),
                            mySongObject.getImageSong(),mySongObject.getLinkSong(),mySongObject.getId_song());
                    FavoriteDatabase.getInstance(SongOfMyArtist.this).favoriteDAO().insertSong(favoriteObject);
                    Toast.makeText(SongOfMyArtist.this,"Đã thêm vào danh sách yêu thích!",Toast.LENGTH_SHORT).show();
                    MyMediaPlayer.getInstance().setCheckUpdateFavorite(true);
                    if(MyMediaPlayer.getInstance().isCheckFavSong()){
                        FavList=MyMediaPlayer.getInstance().getListPlaySong();
                        FavList.add(mySongObject);
                        Arrange(FavList);
                        for(int i=0;i<FavList.size();i++){
                            if(FavList.get(i).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                                MyMediaPlayer.getInstance().setPosition(i);
                                break;
                            }
                        }
                        MyMediaPlayer.getInstance().setListPlaySong(FavList);
                    }
                }
            }
        });
        songOfArtistAdapter.ClickToPlay(new SongOfArtistAdapter.ClickItemToPlay() {
            @Override
            public void clickToPlay(int position) {
                if(!MediaOnline.getInstance().isCheckStop()){
                    MediaOnline.getInstance().stopPlaySong();
                }
                Intent intent=new Intent(SongOfMyArtist.this,PlayerSong.class);
                intent.putExtra("pos",position);
                if(!MyMediaPlayer.getInstance().isCheckStopMedia()) MyMediaPlayer.getInstance().stopAudioFile();
                MyMediaPlayer.getInstance().setCheckSongArtist(true);
                MyMediaPlayer.getInstance().setCheckSongAlbum(false);
                MyMediaPlayer.getInstance().setCheckFavSong(false);
                MyMediaPlayer.getInstance().setIdArtist(myArtistObject.getId_artist());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
            }
        });
        binding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_GALLERY);
            }
        });

        binding.playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MediaOnline.getInstance().isCheckStop()){
                    MediaOnline.getInstance().stopPlaySong();
                }
                Intent intent=new Intent(SongOfMyArtist.this,PlayerSong.class);
                intent.putExtra("pos",0);
                if(!MyMediaPlayer.getInstance().isCheckStopMedia()) MyMediaPlayer.getInstance().stopAudioFile();
                MyMediaPlayer.getInstance().setCheckSongArtist(true);
                MyMediaPlayer.getInstance().setCheckSongAlbum(false);
                MyMediaPlayer.getInstance().setCheckFavSong(false);
                MyMediaPlayer.getInstance().setIdArtist(myArtistObject.getId_artist());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
            }
        });
    }

    public void LoadUI(){
        ListSongOfArtist= (ArrayList<MySongObject>) MySongsDatabase.getInstance(this).
                mySongsDAO().getListSongByArtist(myArtistObject.getNameArtist());
        Arrange(ListSongOfArtist);
        binding.countSong.setText(ListSongOfArtist.size()+" Bài hát");
        songOfArtistAdapter.setData(ListSongOfArtist);
    }
    public void Arrange(ArrayList<MySongObject> ListSongOfArtist){
        Collections.sort(ListSongOfArtist, new Comparator<MySongObject>() {
            @Override
            public int compare(MySongObject mySongObject, MySongObject t1) {
                return mySongObject.getNameSong().compareToIgnoreCase(t1.getNameSong());
            }
        });
    }
    public byte[] ImageView_to_Byte(){
        binding.ImgArtist.setDrawingCacheEnabled(true);
        binding.ImgArtist.buildDrawingCache();
        Bitmap bmp=Bitmap.createBitmap(binding.ImgArtist.getDrawingCache());
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[]  byteArray=stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_GALLERY && resultCode==AddMySong.RESULT_OK && data!=null){
            Uri uri =data.getData();
            Bitmap bitmapImg= null;
            try {
                bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.ImgArtist.setImageBitmap(bitmapImg);
        }
        myArtistObject.setImageArtist(ImageView_to_Byte());
        MyArtistDatabase.getInstance(this).myArtistDAO().editArtist(myArtistObject);
        MyMediaPlayer.getInstance().setCheckUpdateArtist(true);
        if(ListSearch.getInstance().isCheckSearch()){
            ListSearch.getInstance().setCheckUpdateListArtist(true);
            listArtist= (ArrayList<MyArtistObject>) MyArtistDatabase.getInstance(this).myArtistDAO().getMyArtist();
            ArrangeArtist();
            ListSearch.getInstance().setListArtist(listArtist);
        }
    }

    private void ArrangeArtist() {
        Collections.sort(listArtist, new Comparator<MyArtistObject>() {
            @Override
            public int compare(MyArtistObject myArtistObject, MyArtistObject t1) {
                return myArtistObject.getNameArtist().compareToIgnoreCase(t1.getNameArtist());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        songOfArtistAdapter.setData(ListSongOfArtist);
    }
}