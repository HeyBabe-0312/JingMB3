package com.example.jingmb3.view.offline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.jingmb3.databinding.ActivityMyMusicStoreBinding;
import com.example.jingmb3.view.activity.LoadingDialog;
import com.example.jingmb3.view.offline.fragment.ClickItemFileMP3;
import com.example.jingmb3.view.activity.adapter.FileMP3StoreAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MyMusicStore extends AppCompatActivity {

    private ActivityMyMusicStoreBinding binding;
    private String[] items;
    private ArrayList<File> mySongs;
    private ArrayList<String> myListSong;
    private FileMP3StoreAdapter fileMP3StoreAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyMusicStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myListSong=new ArrayList<String>();
        fileMP3StoreAdapter=new FileMP3StoreAdapter(myListSong, new ClickItemFileMP3() {
            @Override
            public void OnClickItemFile(int position) {
                String uri=mySongs.get(position).toString();
                Intent intent=new Intent();
                intent.putExtra("FileSongUri",uri);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        fileMP3StoreAdapter.setData(myListSong);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvListsong.getContext(),
                new LinearLayoutManager(this).getOrientation());
        binding.rvListsong.addItemDecoration(dividerItemDecoration);
        binding.rvListsong.setLayoutManager(new LinearLayoutManager(this));
        binding.rvListsong.setAdapter(fileMP3StoreAdapter);
        runtimePermission();

    }

    public void runtimePermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySong();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findsong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        if(!file.isDirectory()){
            Toast.makeText(this,"Directory is not exist",Toast.LENGTH_SHORT).show();
        }
        else{
            File[] files = file.listFiles();
            if (!(files ==null)){
                for (File singleFile : files) {
                    if (singleFile.isDirectory() && !singleFile.isHidden()) {
                        arrayList.addAll(findsong(singleFile));
                    } else {
                        if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                            arrayList.add(singleFile);
                        }
                    }
                }}
        }
        return arrayList;
    }

    public  void displaySong(){
        mySongs=findsong(Environment.getExternalStorageDirectory());
        if(mySongs==null) return;
        items=new String[mySongs.size()];
        for(int i=0;i<mySongs.size();i++){
            items[i]=mySongs.get(i).getName();
            myListSong.add(items[i]);
        }
        fileMP3StoreAdapter.setData(myListSong);
        LoadingDialog.getInstance().StopDialog();
    }
}