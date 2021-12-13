package com.example.jingmb3.view.offline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivitySelectAlbumToAddSongBinding;
import com.example.jingmb3.model.offline.MyAlbumDatabase;
import com.example.jingmb3.model.offline.MyAlbumObject;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.offline.MySongsDatabase;
import com.example.jingmb3.view.offline.fragment.ClickItemAlbum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SelectAlbumToAddSong extends AppCompatActivity {
    private ArrayList<MyAlbumObject> myListAlbum;
    ActivitySelectAlbumToAddSongBinding binding;
    private SelectAlbumAdapter selectAlbumAdapter;
    private int IdSong;
    private MyAlbumObject myAlbumObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySelectAlbumToAddSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.cancleSelectAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
            }
        });
        selectAlbumAdapter=new SelectAlbumAdapter(myListAlbum, new ClickItemAlbum() {
            @Override
            public void ClickItemInAlbum(int position) {
                AddSongtoAlbum(position);
            }
        });
        IdSong=getIntent().getIntExtra("idSong",0);
        selectAlbumAdapter.setData(myListAlbum);
        binding.rvSelectAlbum.setLayoutManager(new GridLayoutManager(this,2));
        binding.rvSelectAlbum.setAdapter(selectAlbumAdapter);
        loadData();
    }

    public void AddSongtoAlbum(int position){
        myAlbumObject=myListAlbum.get(position);
        ArrayList<String> IdSongList=new ArrayList<>();
        if(myAlbumObject.getId_song()!=null)  {
            if(myAlbumObject.getId_song().contains(String.valueOf(IdSong))){
                Toast.makeText(this,"Bài hát đã có trong Album!!",Toast.LENGTH_SHORT).show();
                return;
            }
            IdSongList=myAlbumObject.getId_song();
            IdSongList.add(String.valueOf(IdSong));
        }
        else IdSongList.add(String.valueOf(IdSong));
        myAlbumObject.setId_song(IdSongList);
        MyAlbumDatabase.getInstance(this).myAlbumDAO().editAlbum(myAlbumObject);

        if(MyMediaPlayer.getInstance().isCheckSongAlbum()){
            if(myAlbumObject.getId_album()==MyMediaPlayer.getInstance().getIdAlbum()){
                ArrayList<MySongObject> arrayList=MyMediaPlayer.getInstance().getListPlaySong();
                arrayList.add(MySongsDatabase.getInstance(this).mySongsDAO().getMySongByID(IdSong));
                ArrangeSong(arrayList);
                for(int i=0;i<arrayList.size();i++){
                    if(arrayList.get(i).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                        MyMediaPlayer.getInstance().setPosition(i);
                        break;
                    }
                }
                MyMediaPlayer.getInstance().setListPlaySong(arrayList);
            }
        }

        MyMediaPlayer.getInstance().setCheckUpdateAlbum(true);

        Intent intent=new Intent();
        intent.putExtra("name album",myAlbumObject.getNameAlbum());
        MyMediaPlayer.getInstance().setCheckUpdateAlbum(true);
        setResult(Activity.RESULT_OK,intent);
        finish();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
    }

    private void ArrangeSong(ArrayList<MySongObject> arrayList) {
        Collections.sort(arrayList, new Comparator<MySongObject>() {
            @Override
            public int compare(MySongObject mySongObject, MySongObject t1) {
                return mySongObject.getNameSong().compareToIgnoreCase(t1.getNameSong());
            }
        });
    }

    public void Arrange(){
        Collections.sort(myListAlbum, new Comparator<MyAlbumObject>() {
            @Override
            public int compare(MyAlbumObject myAlbumObject, MyAlbumObject t1) {
                return myAlbumObject.getNameAlbum().compareToIgnoreCase(t1.getNameAlbum());
            }
        });
    }


    public void loadData(){
        myListAlbum=new ArrayList<>();
        myListAlbum= (ArrayList<MyAlbumObject>) MyAlbumDatabase.getInstance(this).myAlbumDAO().getMyAlbum();
        Arrange();
        selectAlbumAdapter.setData(myListAlbum);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
    }
}