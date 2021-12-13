package com.example.jingmb3.view.offline.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivitySongsOfAlbumBinding;
import com.example.jingmb3.model.offline.FavoriteDatabase;
import com.example.jingmb3.model.offline.FavoriteObject;
import com.example.jingmb3.model.offline.ListSearch;
import com.example.jingmb3.model.offline.MyAlbumDatabase;
import com.example.jingmb3.model.offline.MyAlbumObject;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.offline.MySongsDatabase;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.activity.adapter.SongsOfAlbumAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SongsOfAlbum extends AppCompatActivity {

    private  ArrayList<MySongObject> ListSong;
    private ArrayList<MySongObject> ListSongOfAlbum;
    private MyAlbumObject myAlbumObject;
    private ArrayList<MyAlbumObject> listAlbum;
    private int IdAlbum;
    private SongsOfAlbumAdapter songsOfAlbumAdapter;
    ActivitySongsOfAlbumBinding binding;
    private int reqestEditSong=20;
    private ArrayList<MySongObject> FavList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySongsOfAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(23);
                finish();
                overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
            }
        });
        ListSong=new ArrayList<>();
        FavList=new ArrayList<>();
        IdAlbum=getIntent().getIntExtra("IdAlbum",0);
        songsOfAlbumAdapter=new SongsOfAlbumAdapter();
        ListSongOfAlbum=new ArrayList<>();
        songsOfAlbumAdapter.setData(ListSongOfAlbum);
        myAlbumObject=MyAlbumDatabase.getInstance(this).myAlbumDAO().getAlbumById(IdAlbum);
        Bitmap bitmap= BitmapFactory.decodeByteArray(myAlbumObject.getImageAlbum(),0,myAlbumObject.getImageAlbum().length);
        binding.ImgAlbum.setImageBitmap(bitmap);
        binding.nameAlbum.setText(myAlbumObject.getNameAlbum());
        binding.nameAlbum.setSelected(true);

        if(myAlbumObject.getId_song()==null) {
            binding.playMusic.setVisibility(View.INVISIBLE);
            binding.countSong.setVisibility(View.INVISIBLE);
            binding.countSong.setText("0 bài hát");
        }
        else if(myAlbumObject.getId_song().isEmpty()){
            binding.playMusic.setVisibility(View.INVISIBLE);
            binding.countSong.setVisibility(View.INVISIBLE);
            binding.countSong.setText("0 bài hát");
        }
        else {
            binding.playMusic.setVisibility(View.VISIBLE);
            binding.countSong.setVisibility(View.VISIBLE);
            loadUISong();
        }
        binding.rvSongAlbum.setLayoutManager(new LinearLayoutManager(this));

        binding.rvSongAlbum.setAdapter(songsOfAlbumAdapter);
        songsOfAlbumAdapter.ClickToPlay(new SongsOfAlbumAdapter.ClickItemToPlay() {
            @Override
            public void clickToPlay(int position) {
                if(!MediaOnline.getInstance().isCheckStop()){
                    MediaOnline.getInstance().stopPlaySong();
                }
                Intent intent=new Intent(SongsOfAlbum.this,PlayerSong.class);
                intent.putExtra("pos",position);
                if(!MyMediaPlayer.getInstance().isCheckStopMedia()) MyMediaPlayer.getInstance().stopAudioFile();
                MyMediaPlayer.getInstance().setCheckSongAlbum(true);
                MyMediaPlayer.getInstance().setCheckSongArtist(false);
                MyMediaPlayer.getInstance().setCheckFavSong(false);
                MyMediaPlayer.getInstance().setIdAlbum(myAlbumObject.getId_album());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
            }
        });

        binding.playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MediaOnline.getInstance().isCheckStop()){
                    MediaOnline.getInstance().stopPlaySong();
                }
                Intent intent=new Intent(SongsOfAlbum.this,PlayerSong.class);
                intent.putExtra("pos",0);
                if(!MyMediaPlayer.getInstance().isCheckStopMedia()) MyMediaPlayer.getInstance().stopAudioFile();
                MyMediaPlayer.getInstance().setCheckSongAlbum(true);
                MyMediaPlayer.getInstance().setCheckSongArtist(false);
                MyMediaPlayer.getInstance().setCheckFavSong(false);
                MyMediaPlayer.getInstance().setIdAlbum(myAlbumObject.getId_album());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
            }
        });

        songsOfAlbumAdapter.ClickOption(new SongsOfAlbumAdapter.ClickOption() {
            @Override
            public void clickOption(int postion) {
                openDialog(postion);
            }
        });
    }

    private void openDialog(int Position) {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_option_soa);
        Window window=dialog.getWindow();
        if(window==null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes=window.getAttributes();
        windowAttributes.gravity= Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        LinearLayout RemoveFromAlbum,addListFavorite,Edit;
        ImageView imgSong,icon_fav;
        TextView nameSong,nameArtist,text_fav;
        nameSong=dialog.findViewById(R.id.nameSOA_option);
        nameArtist=dialog.findViewById(R.id.artistSOA_option);
        imgSong=dialog.findViewById(R.id.imgSOA_option);
        text_fav=dialog.findViewById(R.id.FavText);
        icon_fav=dialog.findViewById(R.id.favIcon);
        RemoveFromAlbum=dialog.findViewById(R.id.removeSongFromAlbum);
        addListFavorite=dialog.findViewById(R.id.AddSOAtoListFavorite);
        Edit=dialog.findViewById(R.id.EditSongOfAlbum);

        nameSong.setText(ListSongOfAlbum.get(Position).getNameSong());
        nameArtist.setText(ListSongOfAlbum.get(Position).getNameArtist());
        if(ListSongOfAlbum.get(Position).getImageSong()!=null){
            Bitmap bitmap=BitmapFactory.decodeByteArray(ListSongOfAlbum.get(Position).getImageSong(),0,
                    ListSongOfAlbum.get(Position).getImageSong().length);
            imgSong.setImageBitmap(bitmap);
        }

        ArrayList<Integer> IdFavSong=new ArrayList<>();
        IdFavSong= (ArrayList<Integer>) FavoriteDatabase.getInstance(this).favoriteDAO().getListIdSong();
        if (!IdFavSong.isEmpty()){
            if(IdFavSong.contains(ListSongOfAlbum.get(Position).getId_song())){
                text_fav.setText("Xóa khỏi danh sách yêu thích");
                icon_fav.setImageResource(R.drawable.ic_heart_broken);
            }
            else {
                text_fav.setText("Thêm vào danh sách yêu thích");
                icon_fav.setImageResource(R.drawable.ic_favorite);
            }
        }

        addListFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text_fav.getText().equals("Thêm vào danh sách yêu thích")){
                    String nameSong=ListSongOfAlbum.get(Position).getNameSong();
                    String nameArtist=ListSongOfAlbum.get(Position).getNameArtist();
                    byte[] imgSong=ListSongOfAlbum.get(Position).getImageSong();
                    String uriSong=ListSongOfAlbum.get(Position).getLinkSong();
                    int IdSong=ListSongOfAlbum.get(Position).getId_song();
                    FavoriteObject favoriteObject;
                    favoriteObject=new FavoriteObject(nameSong,nameArtist,imgSong,uriSong,IdSong);
                    FavoriteDatabase.getInstance(SongsOfAlbum.this).favoriteDAO().insertSong(favoriteObject);
                    Toast.makeText(SongsOfAlbum.this,"Đã thêm vào danh sách yêu thích!",Toast.LENGTH_SHORT).show();
                    MyMediaPlayer.getInstance().setCheckUpdateFavorite(true);
                    if(MyMediaPlayer.getInstance().isCheckFavSong()){
                        FavList=MyMediaPlayer.getInstance().getListPlaySong();
                        FavList.add(ListSongOfAlbum.get(Position));
                        ArrangeSong(FavList);
                        for(int i=0;i<FavList.size();i++){
                            if(FavList.get(i).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                                MyMediaPlayer.getInstance().setPosition(i);
                                break;
                            }
                        }
                        MyMediaPlayer.getInstance().setListPlaySong(FavList);
                    }
                    dialog.dismiss();
                }
                else{
                    FavoriteObject favoriteObject=FavoriteDatabase.getInstance(SongsOfAlbum.this).favoriteDAO().getMyFavSongByID
                            (ListSongOfAlbum.get(Position).getId_song());
                    FavoriteDatabase.getInstance(SongsOfAlbum.this).favoriteDAO().deleteSong(favoriteObject);
                    Toast.makeText(SongsOfAlbum.this,"Đã xóa khỏi danh sách yêu thích!",Toast.LENGTH_SHORT).show();
                    MyMediaPlayer.getInstance().setCheckUpdateFavorite(true);
                    if(MyMediaPlayer.getInstance().isCheckFavSong()){
                        if(MyMediaPlayer.getInstance().getIdSong()==ListSongOfAlbum.get(Position).getId_song()){
                            MyMediaPlayer.getInstance().stopAudioFile();
                            FavList=MyMediaPlayer.getInstance().getListPlaySong();
                            for(int i=0;i<FavList.size();i++){
                                if(FavList.get(i).getId_song()==ListSongOfAlbum.get(Position).getId_song()){
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
                                if(FavList.get(i).getId_song()==ListSongOfAlbum.get(Position).getId_song()){
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
                    dialog.dismiss();
                }
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SongsOfAlbum.this, EditMySong.class);
                intent.putExtra("idSong",ListSongOfAlbum.get(Position).getId_song());
                startActivityForResult(intent,reqestEditSong);
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
                dialog.dismiss();
            }
        });
        dialog.show();

        RemoveFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int IdSong=ListSongOfAlbum.get(Position).getId_song();
                ArrayList<String> IdSongList=new ArrayList<>();
                IdSongList= myAlbumObject.getId_song();
                IdSongList.remove(String.valueOf(IdSong));
                myAlbumObject.setId_song(IdSongList);
                MyAlbumDatabase.getInstance(SongsOfAlbum.this).myAlbumDAO().editAlbum(myAlbumObject);
                if(ListSearch.getInstance().isCheckSearch()){
                    ListSearch.getInstance().setCheckUpdateListAlbum(true);
                    listAlbum= (ArrayList<MyAlbumObject>) MyAlbumDatabase.getInstance(SongsOfAlbum.this).myAlbumDAO().getMyAlbum();
                    ArrangeAlbum();
                    ListSearch.getInstance().setListAlbum(listAlbum);
                }
                loadUISong();
                MyMediaPlayer.getInstance().setCheckUpdateAlbum(true);
                Toast.makeText(SongsOfAlbum.this,"Đã xóa bài hát khỏi Album!",Toast.LENGTH_SHORT).show();
                if(MyMediaPlayer.getInstance().isCheckSongAlbum()){
                    if(IdAlbum==MyMediaPlayer.getInstance().getIdAlbum()){
                        if(IdSong==MyMediaPlayer.getInstance().getIdSong()){
                            MyMediaPlayer.getInstance().stopAudioFile();
                            if(ListSongOfAlbum.size()==0) MyMediaPlayer.getInstance().setCheckSongAlbum(false);
                            MyMediaPlayer.getInstance().setListPlaySong(ListSongOfAlbum);
                            MyMediaPlayer.getInstance().setPosition(0);
                        }
                        else{
                            for(int i=0;i<ListSongOfAlbum.size();i++)
                            {
                                if(ListSongOfAlbum.get(i).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                                    MyMediaPlayer.getInstance().setPosition(i);
                                    break;
                                }
                            }
                            MyMediaPlayer.getInstance().setListPlaySong(ListSongOfAlbum);
                        }
                    }
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(reqestEditSong==requestCode && resultCode==Activity.RESULT_OK){
            Toast.makeText(SongsOfAlbum.this,"Đã chỉnh sửa bài hát!",Toast.LENGTH_SHORT).show();
            loadUISong();
        }
    }

    public void loadUISong(){
        ListSong= (ArrayList<MySongObject>) MySongsDatabase.getInstance(this).mySongsDAO().getListSong();
        if(!ListSongOfAlbum.isEmpty()) ListSongOfAlbum.clear();
        for(int i=0;i<ListSong.size();i++){
            if(myAlbumObject.getId_song().contains(String.valueOf(ListSong.get(i).getId_song()))){
                ListSongOfAlbum.add(ListSong.get(i));
            }
        }
        if(ListSongOfAlbum==null) {
            binding.countSong.setText("0 bài hát");
            binding.playMusic.setVisibility(View.INVISIBLE);
            binding.countSong.setVisibility(View.INVISIBLE);
        }
        else if(ListSongOfAlbum.isEmpty()){
            binding.countSong.setText("0 bài hát");
            binding.playMusic.setVisibility(View.INVISIBLE);
            binding.countSong.setVisibility(View.INVISIBLE);
        }
        else {
            binding.countSong.setText(ListSongOfAlbum.size()+" bài hát");
        }
        ArrangeSong(ListSongOfAlbum);
        songsOfAlbumAdapter.setData(ListSongOfAlbum);
    }
    public void ArrangeSong(ArrayList<MySongObject> arrayList){
        Collections.sort(arrayList, new Comparator<MySongObject>() {
            @Override
            public int compare(MySongObject mySongObject, MySongObject t1) {
                return mySongObject.getNameSong().compareToIgnoreCase(t1.getNameSong());
            }
        });
    }

    public void ArrangeAlbum(){
        Collections.sort(listAlbum, new Comparator<MyAlbumObject>() {
            @Override
            public int compare(MyAlbumObject myAlbumObject, MyAlbumObject t1) {
                return myAlbumObject.getNameAlbum().compareToIgnoreCase(t1.getNameAlbum());
            }
        });
    }


    @Override
    public void onBackPressed() {
        setResult(23);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        songsOfAlbumAdapter.setData(ListSongOfAlbum);
    }
}