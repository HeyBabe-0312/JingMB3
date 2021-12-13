package com.example.jingmb3.view.offline.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.jingmb3.databinding.FragmentMyFavoriteSongsBinding;
import com.example.jingmb3.model.offline.FavoriteDatabase;
import com.example.jingmb3.model.offline.FavoriteObject;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.activity.LoadingDialog;
import com.example.jingmb3.view.offline.activity.EditMySong;
import com.example.jingmb3.view.offline.activity.PlayerSong;
import com.example.jingmb3.view.offline.activity.SelectAlbumToAddSong;
import com.example.jingmb3.view.activity.adapter.MyFavSongAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MyFavoriteSongs extends Fragment {

    private ArrayList<FavoriteObject> myFavListSong;
    private MyFavSongAdapter myFavSongAdapter;
    FragmentMyFavoriteSongsBinding binding;
    private int reqestAddSongtoAlbum=1;
    private int reqestEditSong=2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMyFavoriteSongsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myFavListSong=new ArrayList<>();
        myFavSongAdapter=new MyFavSongAdapter(myFavListSong, new IClickItemListener() {
            @Override
            public void OnClickItemSongs(int position) {
                if(!MediaOnline.getInstance().isCheckStop()){
                    MediaOnline.getInstance().stopPlaySong();
                }
                if(!MyMediaPlayer.getInstance().isCheckStopMedia())  MyMediaPlayer.getInstance().stopAudioFile();
                MyMediaPlayer.getInstance().setCheckFavSong(true);
                MyMediaPlayer.getInstance().setCheckSongAlbum(false);
                MyMediaPlayer.getInstance().setCheckSongArtist(false);
                Intent intent=new Intent(getActivity(), PlayerSong.class);
                startActivity(intent.putExtra("pos",position));
                getActivity().overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
            }
        });
        loadData();
        binding.rvMyFavSong.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMyFavSong.setAdapter(myFavSongAdapter);
        myFavSongAdapter.setClickMoreOption(new MyFavSongAdapter.ClickMoreOption() {
            @Override
            public void clickMoreOption(int position) {
                OpenDialogOption(position);
            }
        });
        LoadingDialog.getInstance().StopDialog();
    }

    private void OpenDialogOption(int position) {
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_option_fav_song);
        Window window=dialog.getWindow();
        if(window==null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes=window.getAttributes();
        windowAttributes.gravity= Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        dialog.show();
        ImageView imgSong;
        TextView nameSong,nameArtist;
        LinearLayout AddToAlbum, RemoveFromFavList, EditSong;
        imgSong=dialog.findViewById(R.id.imgFavSongOption);
        nameSong=dialog.findViewById(R.id.nameFavSongOption);
        nameArtist=dialog.findViewById(R.id.ArtistFavSongOption);
        AddToAlbum=dialog.findViewById(R.id.AddFavSongToAlbum);
        RemoveFromFavList=dialog.findViewById(R.id.RemoveFromListFavorite);
        EditSong=dialog.findViewById(R.id.EditFavSong);

        nameSong.setSelected(true);
        nameSong.setText(myFavListSong.get(position).getNameSong());
        nameArtist.setText(myFavListSong.get(position).getNameArtist());
        if(myFavListSong.get(position).getImageSong()!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(myFavListSong.get(position).getImageSong(),0,
                    myFavListSong.get(position).getImageSong().length);
            imgSong.setImageBitmap(bitmap);
        }

        AddToAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SelectAlbumToAddSong.class);
                intent.putExtra("idSong",myFavListSong.get(position).getId_song());
                startActivityForResult(intent,reqestAddSongtoAlbum);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
                dialog.dismiss();
            }
        });

        RemoveFromFavList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int IdSong=myFavListSong.get(position).getId_song();
                FavoriteDatabase.getInstance(getContext()).favoriteDAO().deleteSong(myFavListSong.get(position));
                Toast.makeText(getContext(),"Đã xóa khỏi danh sách yêu thích!",Toast.LENGTH_SHORT).show();
                loadData();
                if(IdSong==MyMediaPlayer.getInstance().getIdSong()){
                    MyMediaPlayer.getInstance().stopAudioFile();
                    MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                    ArrayList<MySongObject> arrayList=new ArrayList<>();
                    arrayList=MyMediaPlayer.getInstance().getListPlaySong();
                    for(MySongObject mySongObject:arrayList){
                        if(mySongObject.getId_song()==IdSong){
                            arrayList.remove(mySongObject);
                            break;
                        }
                    }
                    if(arrayList.size()==0) MyMediaPlayer.getInstance().setCheckFavSong(false);
                    MyMediaPlayer.getInstance().setListPlaySong(arrayList);
                    MyMediaPlayer.getInstance().setPosition(0);
                    myMusic.loadMiniPlayer(0);
                }
                else{
                    MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
                    ArrayList<MySongObject> arrayList=new ArrayList<>();
                    arrayList=MyMediaPlayer.getInstance().getListPlaySong();
                    for(MySongObject mySongObject:arrayList){
                        if(mySongObject.getId_song()==IdSong){
                            arrayList.remove(mySongObject);
                            break;
                        }
                    }
                    for(int i=0;i<arrayList.size();i++){
                        if(arrayList.get(i).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                            MyMediaPlayer.getInstance().setPosition(i);
                            break;
                        }
                    }
                    MyMediaPlayer.getInstance().setListPlaySong(arrayList);
                    myMusic.loadMiniPlayer(MyMediaPlayer.getInstance().getPosition());
                }
                dialog.dismiss();
            }
        });

        EditSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), EditMySong.class);
                intent.putExtra("idSong",myFavListSong.get(position).getId_song());
                startActivityForResult(intent,reqestEditSong);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(reqestAddSongtoAlbum==requestCode && resultCode==Activity.RESULT_OK){
            Toast.makeText(getContext(),"Đã thêm bài hát vào Album "+data.getStringExtra("name album"),Toast.LENGTH_SHORT).show();
        }
        if(reqestEditSong==requestCode && resultCode==Activity.RESULT_OK){
            Toast.makeText(getContext(),"Đã chỉnh sửa bài hát!",Toast.LENGTH_SHORT).show();
            loadData();
        }
    }

    public void loadData(){
        myFavListSong= (ArrayList<FavoriteObject>) FavoriteDatabase.getInstance(getContext()).favoriteDAO().getListFavSong();
        if(myFavListSong.isEmpty()) binding.countFavSongs.setText("0 Bài hát yêu thích");
        else binding.countFavSongs.setText(myFavListSong.size()+" Bài hát yêu thích");
        Arrange();
        myFavSongAdapter.setData(myFavListSong);
    }
    public void Arrange(){
        Collections.sort(myFavListSong, new Comparator<FavoriteObject>() {
            @Override
            public int compare(FavoriteObject favoriteObject, FavoriteObject t1) {
                return favoriteObject.getNameSong().compareToIgnoreCase(t1.getNameSong());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MyMediaPlayer.getInstance().isCheckUpdateFavorite()){
            loadData();
            MyMediaPlayer.getInstance().setCheckUpdateFavorite(false);
        }

    }
}