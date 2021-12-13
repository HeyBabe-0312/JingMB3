package com.example.jingmb3.view.offline.fragment;

import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.example.jingmb3.databinding.FragmentMyAlbumBinding;
import com.example.jingmb3.model.offline.ListSearch;
import com.example.jingmb3.model.offline.MyAlbumDatabase;
import com.example.jingmb3.model.offline.MyAlbumObject;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.offline.MySongsDatabase;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.offline.activity.AddMyAlbum;
import com.example.jingmb3.view.offline.activity.EditMyAlbum;
import com.example.jingmb3.view.offline.activity.SongsOfAlbum;
import com.example.jingmb3.view.activity.adapter.MyAlbumAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MyAlbum extends Fragment {

    private ArrayList<MyAlbumObject> myListAlbum;
    private MyAlbumAdapter myAlbumAdapter;
    FragmentMyAlbumBinding binding;
    private int REQUEST_ADD_ALBUM=23;
    private int requestEdit=50;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMyAlbumBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myListAlbum=new ArrayList<MyAlbumObject>();
        myAlbumAdapter=new MyAlbumAdapter(myListAlbum, new ClickItemAlbum() {
            @Override
            public void ClickItemInAlbum(int position) {
                GotoAlbum(position);
            }
        });
        myAlbumAdapter.setData(myListAlbum);
        binding.rvMyAlbum.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.rvMyAlbum.setAdapter(myAlbumAdapter);
        loadData();
        binding.addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), AddMyAlbum.class);
                startActivityForResult(intent,REQUEST_ADD_ALBUM);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
            }
        });

        myAlbumAdapter.setClickOptionAlbum(new MyAlbumAdapter.ClickOptionAlbum() {
            @Override
            public void clickOptionAlbum(int position) {
                OpenDialogAlbum(position);
            }
        });
    }

    private void OpenDialogAlbum(int position) {
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_option_album);
        Window window=dialog.getWindow();
        if(window==null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes=window.getAttributes();
        windowAttributes.gravity= Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        TextView nameAlbum, countSong;
        ImageView imgAlbum;
        nameAlbum=dialog.findViewById(R.id.nameAlbumOption);
        countSong=dialog.findViewById(R.id.countSongOption);
        imgAlbum=dialog.findViewById(R.id.imgAlbumOption);
        nameAlbum.setText(myListAlbum.get(position).getNameAlbum());
        if(myListAlbum.get(position).getId_song()==null) countSong.setText("0 Bài hát");
        else countSong.setText(myListAlbum.get(position).getId_song().size()+" Bài hát");
        if(myListAlbum.get(position).getImageAlbum()!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(myListAlbum.get(position).getImageAlbum(),0,
                    myListAlbum.get(position).getImageAlbum().length);
            imgAlbum.setImageBitmap(bitmap);
        }

        LinearLayout play,edit,remove;
        play=dialog.findViewById(R.id.PlayAlbum);
        edit=dialog.findViewById(R.id.EditAlbum);
        remove=dialog.findViewById(R.id.DeleteAlbum);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAlbum(position);
                dialog.dismiss();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveAlbum(position);
                dialog.dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), EditMyAlbum.class);
                intent.putExtra("IdAlbum",myListAlbum.get(position).getId_album());
                startActivityForResult(intent,requestEdit);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void playAlbum(int position){
        if(myListAlbum.get(position).getId_song()==null){
            Toast.makeText(getContext(),"Không có bài hát trong Album!",Toast.LENGTH_SHORT).show();
        }
        else if(myListAlbum.get(position).getId_song().size()==0){
            Toast.makeText(getContext(),"Không có bài hát trong Album!",Toast.LENGTH_SHORT).show();
        }
        else{
            if(!MediaOnline.getInstance().isCheckStop()){
                MediaOnline.getInstance().stopPlaySong();
            }
            ArrayList<String> listIdAlbum=new ArrayList<>();
            listIdAlbum=myListAlbum.get(position).getId_song();
            ArrayList<MySongObject> myListSong=new ArrayList<>();
            for(String id:listIdAlbum) {
                myListSong.add(MySongsDatabase.getInstance(getContext()).mySongsDAO().getMySongByID(Integer.valueOf(id)));
            }
            ArrangeSong(myListSong);
            MyMediaPlayer.getInstance().setListPlaySong(myListSong);
            MyMediaPlayer.getInstance().setPlayAlbum(true);
            if(MyMediaPlayer.getInstance().getMediaPlayer()==null){
                MyMediaPlayer.getInstance().setCheckSongAlbum(true);
                MyMediaPlayer.getInstance().setIdAlbum(myListAlbum.get(position).getId_album());
                MyMediaPlayer.getInstance().playAudioFile(getContext(),myListSong.get(0).getLinkSong(), 0);
                MyMediaPlayer.getInstance().Start();
            }
            else{
                if(!MyMediaPlayer.getInstance().isCheckStopMedia())
                    MyMediaPlayer.getInstance().stopAudioFile();
                MyMediaPlayer.getInstance().setStopMedia();
                MyMediaPlayer.getInstance().setCheckSongAlbum(true);
                MyMediaPlayer.getInstance().setCheckSongArtist(false);
                MyMediaPlayer.getInstance().setCheckFavSong(false);
                MyMediaPlayer.getInstance().setIdAlbum(myListAlbum.get(position).getId_album());
                MyMediaPlayer.getInstance().playAudioFile(getContext(),myListSong.get(0).getLinkSong(), 0);
                MyMediaPlayer.getInstance().Start();
            }
            MyMusic myMusic= (MyMusic) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
            myMusic.loadMiniPlayer(MyMediaPlayer.getInstance().getPosition());
            myMusic.mediaComplete();
        }
    }
    private void RemoveAlbum(int postion){
        int countSong=0;
        if(myListAlbum.get(postion).getId_song()!=null)
            countSong=myListAlbum.get(postion).getId_song().size();
        new AlertDialog.Builder(getContext()).setTitle("Xóa Album này?")
                .setMessage("Bạn có chắc chắn muốn xóa Album "+myListAlbum.get(postion).getNameAlbum()+" ("
                +countSong+" Bài hát)?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(MyMediaPlayer.getInstance().isCheckSongAlbum() &&
                        MyMediaPlayer.getInstance().getIdAlbum()==myListAlbum.get(postion).getId_album()){

                        }
                        MyAlbumDatabase.getInstance(getContext()).myAlbumDAO().deleteAlbum(myListAlbum.get(postion));
                        loadData();
                        Toast.makeText(getContext(),"Đã xóa Album!",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Hủy",null).show();
    }

    public void GotoAlbum(int position){
        Intent intent=new Intent(getActivity(), SongsOfAlbum.class);
        intent.putExtra("IdAlbum",myListAlbum.get(position).getId_album());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
    }

    private void ArrangeSong(ArrayList<MySongObject> myListSong){
        Collections.sort(myListSong, new Comparator<MySongObject>() {
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
        myListAlbum= (ArrayList<MyAlbumObject>) MyAlbumDatabase.getInstance(getContext()).myAlbumDAO().getMyAlbum();
        Arrange();
        ListSearch.getInstance().setListAlbum(myListAlbum);
        binding.countAlbum.setText(myListAlbum.size()+" Album");
        myAlbumAdapter.setData(myListAlbum);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_ADD_ALBUM==requestCode && resultCode== Activity.RESULT_OK){
            loadData();
            Toast.makeText(getContext(),"Đã thêm Album!",Toast.LENGTH_SHORT).show();
        }
        if(requestEdit==requestCode && resultCode== Activity.RESULT_OK){
            loadData();
            Toast.makeText(getContext(),"Đã sửa Album!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MyMediaPlayer.getInstance().isCheckUpdateAlbum()){
            MyMediaPlayer.getInstance().setCheckUpdateAlbum(false);
            loadData();
        }
    }
}