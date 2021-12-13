package com.example.jingmb3.view.offline.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.FragmentMySongsBinding;
import com.example.jingmb3.model.offline.FavoriteDatabase;
import com.example.jingmb3.model.offline.FavoriteObject;
import com.example.jingmb3.model.offline.ListSearch;
import com.example.jingmb3.model.offline.MyAlbumDatabase;
import com.example.jingmb3.model.offline.MyAlbumObject;
import com.example.jingmb3.model.offline.MyArtistDatabase;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.offline.MySongsDatabase;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.offline.activity.AddMySong;
import com.example.jingmb3.view.offline.activity.EditMySong;
import com.example.jingmb3.view.offline.activity.PlayerSong;
import com.example.jingmb3.view.offline.activity.SelectAlbumToAddSong;
import com.example.jingmb3.view.activity.adapter.MySongAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MySongs extends Fragment  {


    private static final int REQUEST_ADD_SONG = 80;
    FragmentMySongsBinding binding;
    private ArrayList<MySongObject> myListSong;
    private ArrayList<MySongObject> FavList;
    private ArrayList<MySongObject> artistList;
    private ArrayList<MySongObject> albumList;
    private MySongAdapter mySongAdapter;
    private int reqestAddSongtoAlbum=100;
    private int reqestEditSong=99;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMySongsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddMySong.class);
                startActivityForResult(intent,REQUEST_ADD_SONG);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
            }
        });
        myListSong=new ArrayList<MySongObject>();
        FavList=new ArrayList<>();
        mySongAdapter=new MySongAdapter(getContext());
        mySongAdapter.IClickItemListener(new IClickItemListener() {
            @Override
            public void OnClickItemSongs(int position) {
                OnClickToPlay(position);
            }
        });
        mySongAdapter.setClickMoreOption(new MySongAdapter.ClickMoreOption() {
            @Override
            public void clickMoreOption(int position) {
                openDialogOption(position);
            }
        });
        mySongAdapter.setData(myListSong);
        binding.rvMysong.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMysong.setAdapter(mySongAdapter);
        loadData();
    }


    private void OnClickToPlay(int position) {
        if(!MediaOnline.getInstance().isCheckStop()){
            MediaOnline.getInstance().stopPlaySong();
        }
        if(!MyMediaPlayer.getInstance().isCheckStopMedia()) MyMediaPlayer.getInstance().stopAudioFile();
        MyMediaPlayer.getInstance().setCheckSongAlbum(false);
        MyMediaPlayer.getInstance().setCheckSongArtist(false);
        MyMediaPlayer.getInstance().setCheckFavSong(false);
        Intent intent=new Intent(getActivity(), PlayerSong.class);
        startActivity(intent.putExtra("pos",position));
        getActivity().overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_ADD_SONG==requestCode && resultCode==Activity.RESULT_OK){
            loadData();
            Toast.makeText(getContext(),"Đã thêm bài hát mới!",Toast.LENGTH_SHORT).show();
        }
        if(reqestAddSongtoAlbum==requestCode && resultCode==Activity.RESULT_OK){
            Toast.makeText(getContext(),"Đã thêm bài hát vào Album "+data.getStringExtra("name album"),Toast.LENGTH_SHORT).show();
        }
        if(reqestEditSong==requestCode && resultCode==Activity.RESULT_OK){
            Toast.makeText(getContext(),"Đã chỉnh sửa bài hát!",Toast.LENGTH_SHORT).show();
            loadData();
        }

    }

    public void Arrange(ArrayList<MySongObject> myListSong){
        Collections.sort(myListSong, new Comparator<MySongObject>() {
            @Override
            public int compare(MySongObject mySongObject, MySongObject t1) {
                return mySongObject.getNameSong().compareToIgnoreCase(t1.getNameSong());
            }
        });
    }


    public void loadData(){
        myListSong= (ArrayList<MySongObject>) MySongsDatabase.getInstance(getContext()).mySongsDAO().getListSong();
        Arrange(myListSong);
        ListSearch.getInstance().setListSong(myListSong);
        binding.countSongs.setText(myListSong.size()+" Bài hát");
        mySongAdapter.setData(myListSong);
    }

    public ArrayList<MySongObject> getMyListSong() {
        return myListSong;
    }

    public void setMyListSong(ArrayList<MySongObject> myListSong) {
        this.myListSong = myListSong;
    }

    public MySongAdapter getMySongAdapter() {
        return mySongAdapter;
    }

    public void setMySongAdapter(MySongAdapter mySongAdapter) {
        this.mySongAdapter = mySongAdapter;
    }



    public void openDialogOption(final int position){
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_more_option);
        Window window=dialog.getWindow();
        if(window==null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes=window.getAttributes();
        windowAttributes.gravity= Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        LinearLayout addToAlbum,addListFavorite,Edit,Remove;
        ImageView imgSong,favImg;
        TextView nameSong,nameArtist, favText;
        favImg=dialog.findViewById(R.id.favImg);
        nameSong=dialog.findViewById(R.id.nameSongOption);
        nameArtist=dialog.findViewById(R.id.artistOption);
        imgSong=dialog.findViewById(R.id.imgSongOption);
        addToAlbum=dialog.findViewById(R.id.AddSongToAlbum);
        addListFavorite=dialog.findViewById(R.id.AddListFavorite);
        favText=dialog.findViewById(R.id.favText);
        Edit=dialog.findViewById(R.id.EditSong);
        Remove=dialog.findViewById(R.id.DeleteSong);

        ArrayList<Integer> IdFavSong=new ArrayList<>();
        IdFavSong= (ArrayList<Integer>) FavoriteDatabase.getInstance(getContext()).favoriteDAO().getListIdSong();
        if (!IdFavSong.isEmpty()){
            if(IdFavSong.contains(myListSong.get(position).getId_song())){
                favText.setText("Xóa khỏi danh sách yêu thích");
                favImg.setImageResource(R.drawable.ic_heart_broken);
            }
            else {
                favText.setText("Thêm vào danh sách yêu thích");
                favImg.setImageResource(R.drawable.ic_favorite);
            }
        }
        Bitmap bitmap=BitmapFactory.decodeByteArray(myListSong.get(position).getImageSong(),0,
                myListSong.get(position).getImageSong().length);
        imgSong.setImageBitmap(bitmap);
        nameArtist.setText(myListSong.get(position).getNameArtist());
        nameSong.setText(myListSong.get(position).getNameSong());
        addListFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favText.getText().equals("Thêm vào danh sách yêu thích")){
                    String nameSong=myListSong.get(position).getNameSong();
                    String nameArtist=myListSong.get(position).getNameArtist();
                    byte[] imgSong=myListSong.get(position).getImageSong();
                    String uriSong=myListSong.get(position).getLinkSong();
                    int IdSong=myListSong.get(position).getId_song();
                    FavoriteObject favoriteObject;
                    favoriteObject=new FavoriteObject(nameSong,nameArtist,imgSong,uriSong,IdSong);
                    FavoriteDatabase.getInstance(getContext()).favoriteDAO().insertSong(favoriteObject);
                    Toast.makeText(getContext(),"Đã thêm vào danh sách yêu thích!",Toast.LENGTH_SHORT).show();
                    MyMediaPlayer.getInstance().setCheckUpdateFavorite(true);
                    if(MyMediaPlayer.getInstance().isCheckFavSong()){
                        MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                        FavList=MyMediaPlayer.getInstance().getListPlaySong();
                        FavList.add(myListSong.get(position));
                        Arrange(FavList);
                        for(int i=0;i<FavList.size();i++){
                            if(FavList.get(i).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                                MyMediaPlayer.getInstance().setPosition(i);
                                break;
                            }
                        }
                        MyMediaPlayer.getInstance().setListPlaySong(FavList);
                        myMusic.loadMiniPlayer(MyMediaPlayer.getInstance().getPosition());
                    }
                    dialog.dismiss();
                }
                else{
                    FavoriteObject favoriteObject=FavoriteDatabase.getInstance(getContext()).favoriteDAO().getMyFavSongByID
                            (myListSong.get(position).getId_song());
                    FavoriteDatabase.getInstance(getContext()).favoriteDAO().deleteSong(favoriteObject);
                    Toast.makeText(getContext(),"Đã xóa khỏi danh sách yêu thích!",Toast.LENGTH_SHORT).show();
                    MyMediaPlayer.getInstance().setCheckUpdateFavorite(true);
                    if(MyMediaPlayer.getInstance().isCheckFavSong()){
                        if(MyMediaPlayer.getInstance().getIdSong()==myListSong.get(position).getId_song()){
                            MyMediaPlayer.getInstance().stopAudioFile();
                            MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                            FavList=MyMediaPlayer.getInstance().getListPlaySong();
                            for(int i=0;i<FavList.size();i++){
                                if(FavList.get(i).getId_song()==myListSong.get(position).getId_song()){
                                    FavList.remove( FavList.get(i));
                                    break;
                                }
                            }
                            if(FavList.size()==0) MyMediaPlayer.getInstance().setCheckFavSong(false);
                            MyMediaPlayer.getInstance().setPosition(0);
                            MyMediaPlayer.getInstance().setListPlaySong(FavList);
                            myMusic.loadMiniPlayer(MyMediaPlayer.getInstance().getPosition());
                        }
                        else{
                            MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                            FavList=MyMediaPlayer.getInstance().getListPlaySong();
                            for(int i=0;i<FavList.size();i++){
                                if(FavList.get(i).getId_song()==myListSong.get(position).getId_song()){
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
                            myMusic.loadMiniPlayer(MyMediaPlayer.getInstance().getPosition());
                        }
                    }
                    dialog.dismiss();
                }
            }
        });
        addToAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SelectAlbumToAddSong.class);
                intent.putExtra("idSong",myListSong.get(position).getId_song());
                startActivityForResult(intent,reqestAddSongtoAlbum);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
                dialog.dismiss();
            }
        });
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), EditMySong.class);
                intent.putExtra("idSong",myListSong.get(position).getId_song());
                startActivityForResult(intent,reqestEditSong);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
                dialog.dismiss();
            }
        });
        Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveSong(position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void RemoveSong(final int postion){
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa bài hát này?")
                .setMessage("Bạn có chắc chắn muốn xóa bài hát "+myListSong.get(postion).getNameSong()
                +" ra khỏi ứng dụng?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyMediaPlayer.getInstance().setCheckUpdateArtist(true);
                        MyMediaPlayer.getInstance().setCheckUpdateAlbum(true);
                        MyMediaPlayer.getInstance().setCheckUpdateFavorite(true);
                        int IdSong=myListSong.get(postion).getId_song();
                        String nameArtist=myListSong.get(postion).getNameArtist();
                        MyAlbumObject myAlbum=MyAlbumDatabase.getInstance(getContext()).myAlbumDAO().getAlbumById(MyMediaPlayer.getInstance().getIdAlbum());
                        ArrayList<Integer> ListIdFavSong= (ArrayList<Integer>) FavoriteDatabase.getInstance(getContext()).favoriteDAO().getListIdSong();
                        ArrayList<MyAlbumObject> myAlbumList=new ArrayList<>();
                        myAlbumList= (ArrayList<MyAlbumObject>) MyAlbumDatabase.getInstance(getContext()).myAlbumDAO().getMyAlbum();
                        for(MyAlbumObject myAlbumObject:myAlbumList){
                            if(myAlbumObject.getId_song()!=null) {
                                if (myAlbumObject.getId_song().contains(String.valueOf(IdSong))) {
                                    ArrayList<String> idSongList = myAlbumObject.getId_song();
                                    idSongList.remove(String.valueOf(IdSong));
                                    myAlbumObject.setId_song(idSongList);
                                    MyAlbumDatabase.getInstance(getContext()).myAlbumDAO().editAlbum(myAlbumObject);
                                }
                            }
                        }
                        MySongsDatabase.getInstance(getContext()).mySongsDAO().deleteSong(myListSong.get(postion));
                        Toast.makeText(getContext(),"Đã xóa bài hát",Toast.LENGTH_SHORT).show();

                        List<Integer> IdFavSong=FavoriteDatabase.getInstance(getContext()).favoriteDAO().getListIdSong();
                        if(IdFavSong.contains(IdSong)){
                            FavoriteDatabase.getInstance(getContext()).favoriteDAO().deleteSong(FavoriteDatabase
                                    .getInstance(getContext()).favoriteDAO().getMyFavSongByID(IdSong));
                        }

                        List<String> listArtist=MySongsDatabase.getInstance(getContext()).mySongsDAO().getListArtist();
                        if(!listArtist.contains(nameArtist)){
                            MyArtistDatabase.getInstance(getContext()).myArtistDAO().deleteArtist(MyArtistDatabase.
                                    getInstance(getContext()).myArtistDAO().getArtistByName(nameArtist));
                        }

                        loadData();
                        if(myListSong.isEmpty()){
                            MyMediaPlayer.getInstance().stopAudioFile();
                            MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                            myMusic.HideMiniPlayer();
                            return;
                        }
                        if(MyMediaPlayer.getInstance().getIdSong()==IdSong){
                            MyMediaPlayer.getInstance().stopAudioFile();
                            MyMediaPlayer.getInstance().setCheckSongAlbum(false);
                            MyMediaPlayer.getInstance().setCheckSongArtist(false);
                            MyMediaPlayer.getInstance().setCheckFavSong(false);
                            MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                            MyMediaPlayer.getInstance().setListPlaySong(myListSong);
                            MyMediaPlayer.getInstance().setPosition(0);
                            myMusic.loadMiniPlayer(0);
                        }
                        else {
                            if(!MyMediaPlayer.getInstance().isCheckSongAlbum()&&!MyMediaPlayer.getInstance().isCheckSongArtist()
                            &&!MyMediaPlayer.getInstance().isCheckFavSong()){
                                MyMediaPlayer.getInstance().setListPlaySong(myListSong);
                                for(int j=0;j<myListSong.size();j++){
                                    if(myListSong.get(j).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                                        MyMediaPlayer.getInstance().setPosition(j);
                                        break;
                                    }
                                }
                                MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                                myMusic.loadMiniPlayer(MyMediaPlayer.getInstance().getPosition());
                            }
                            else if(MyMediaPlayer.getInstance().isCheckSongArtist()){
                                if(MyArtistDatabase.getInstance(getContext()).myArtistDAO().getArtistById(MyMediaPlayer
                                .getInstance().getIdArtist()).getNameArtist().equals(nameArtist)){
                                    artistList=MyMediaPlayer.getInstance().getListPlaySong();
                                    Arrange(artistList);
                                    for(MySongObject mySongObject:artistList){
                                        if(mySongObject.getId_song()==IdSong){
                                            artistList.remove(mySongObject);
                                            break;
                                        }
                                    }
                                    for(int k=0;k<artistList.size();k++){
                                        if(artistList.get(k).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                                            MyMediaPlayer.getInstance().setPosition(k);
                                            break;
                                        }
                                    }
                                    MyMediaPlayer.getInstance().setListPlaySong(artistList);
                                    MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                                    myMusic.loadMiniPlayer(MyMediaPlayer.getInstance().getPosition());
                                }
                            }
                            else if(MyMediaPlayer.getInstance().isCheckSongAlbum()){
                                if(myAlbum.getId_song()!=null){
                                    if(myAlbum.getId_song().contains(String.valueOf(IdSong))){
                                        albumList=MyMediaPlayer.getInstance().getListPlaySong();
                                        Arrange(albumList);
                                        for(MySongObject mySongObject:albumList){
                                            if(mySongObject.getId_song()==IdSong){
                                                albumList.remove(mySongObject);
                                                break;
                                            }
                                        }
                                        for(int k=0;k<albumList.size();k++){
                                            if(albumList.get(k).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                                                MyMediaPlayer.getInstance().setPosition(k);
                                                break;
                                            }
                                        }
                                        MyMediaPlayer.getInstance().setListPlaySong(albumList);
                                        MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                                        myMusic.loadMiniPlayer(MyMediaPlayer.getInstance().getPosition());
                                    }
                                }
                            }
                            else if(MyMediaPlayer.getInstance().isCheckFavSong()){
                                if(ListIdFavSong!=null){
                                    if(ListIdFavSong.contains(IdSong)){
                                        FavList=MyMediaPlayer.getInstance().getListPlaySong();
                                        Arrange(FavList);
                                        for(MySongObject mySongObject:FavList){
                                            if(mySongObject.getId_song()==IdSong){
                                                FavList.remove(mySongObject);
                                                break;
                                            }
                                        }
                                        for(int k=0;k<FavList.size();k++){
                                            if(FavList.get(k).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                                                MyMediaPlayer.getInstance().setPosition(k);
                                                break;
                                            }
                                        }
                                        MyMediaPlayer.getInstance().setListPlaySong(FavList);
                                        MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                                        myMusic.loadMiniPlayer(MyMediaPlayer.getInstance().getPosition());
                                    }
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("Hủy",null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MyMediaPlayer.getInstance().isCheckUpdateSong()){
            loadData();
            MyMediaPlayer.getInstance().setCheckUpdateSong(false);
        }

    }
}