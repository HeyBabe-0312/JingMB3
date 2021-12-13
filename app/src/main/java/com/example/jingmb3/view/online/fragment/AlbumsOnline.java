package com.example.jingmb3.view.online.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.FragmentAlbumsOnlineBinding;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.model.online.Upload;
import com.example.jingmb3.view.activity.LoadingDialog;
import com.example.jingmb3.view.activity.adapter.AlbumOnlineAdapter;
import com.example.jingmb3.view.online.activity.SongOfAlbumOnl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlbumsOnline extends Fragment {
    List<Upload> uploadList;
    DatabaseReference databaseReference;
    AlbumOnlineAdapter albumOnlineAdapter;
    FragmentAlbumsOnlineBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlbumsOnlineBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadList = new ArrayList<>();
        albumOnlineAdapter = new AlbumOnlineAdapter(uploadList,getContext());
        albumOnlineAdapter.setGetAlbum(uploadList);
        binding.rcView.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.rcView.setAdapter(albumOnlineAdapter);
        databaseReference = FirebaseDatabase.getInstance("https://musicupload-7dde0-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("uploads");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadList.clear();
                for(DataSnapshot dss:snapshot.getChildren()){
                    Upload album = dss.getValue(Upload.class);
                    uploadList.add(album);
                }
                binding.countAlbum.setText(uploadList.size()+" Album");
                Arrange(uploadList);
                MediaOnline.getInstance().setAlbumList(uploadList);
                albumOnlineAdapter.setGetAlbum(MediaOnline.getInstance().getAlbumList());
                LoadingDialog.getInstance().StopDialog();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                LoadingDialog.getInstance().StopDialog();
            }
        });
        albumOnlineAdapter.setClickAlbum(new AlbumOnlineAdapter.clickAlbumOnline(){
            @Override
            public void clickAlbumOnline(int position) {
                Intent intent = new Intent(getContext(), SongOfAlbumOnl.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("album",uploadList.get(position).getName());
                intent.putExtra("url",uploadList.get(position).getUrl());
                startActivityForResult(intent,20);
                getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
            }
        });
    } public void Arrange(List<Upload> myListAlbum){
        Collections.sort(myListAlbum, new Comparator<Upload>() {
            @Override
            public int compare(Upload myAlbumObject, Upload t1) {
                return myAlbumObject.getName().compareToIgnoreCase(t1.getName());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==20 && resultCode== Activity.RESULT_OK){
            MusicOnline musicOnline = (MusicOnline) getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_content);
            musicOnline.loadMediaPlay(MediaOnline.getInstance().getPosition());
            if(MediaOnline.getInstance().getMediaPlayer()!=null){
                musicOnline.completeSong();
            }
        }
    }
}