package com.example.jingmb3.view.offline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivityAddMySongBinding;
import com.example.jingmb3.model.offline.MyArtistDatabase;
import com.example.jingmb3.model.offline.MyArtistObject;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.offline.MySongsDatabase;
import com.example.jingmb3.view.activity.LoadingDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AddMySong extends AppCompatActivity {

    ActivityAddMySongBinding binding;
    private int REQUEST_UPFILE=1;
    private int REQUEST_CAMERA=2;
    private int REQUEST_GALLERY=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddMySongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.cancelAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
            }
        });

        binding.uploadUri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingDialog.getInstance().StartDialog(AddMySong.this);
                Intent intent = new Intent(AddMySong.this,MyMusicStore.class);
                startActivityForResult(intent,REQUEST_UPFILE);
            }
        });

        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera();
            }
        });

        binding.galleryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        binding.DoneAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveSong();
            }
        });

    }

    private void SaveSong() {
        String name=binding.inNameSong.getText().toString().trim();
        String artist=binding.inArtist.getText().toString().trim();
        String uri=binding.uriFileSong.getText().toString().trim();
        if (uri.isEmpty()|| name.equals("") || artist.isEmpty()){
            Toast.makeText(AddMySong.this,"Hãy nhập đủ thông tin!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!MyArtistDatabase.getInstance(this).myArtistDAO().getListNameArtist().contains(artist)){
            MyArtistObject myArtistObject=new MyArtistObject();
            myArtistObject.setNameArtist(artist);
            MyArtistDatabase.getInstance(this).myArtistDAO().insertArtist(myArtistObject);
        }

        MySongObject mySongObject=new MySongObject(name,artist,ImageView_to_Byte(),uri);
        MySongsDatabase.getInstance(this).mySongsDAO().insertSong(mySongObject);
        MyMediaPlayer.getInstance().setCheckUpdateArtist(true);

        if(MyArtistDatabase.getInstance(this).myArtistDAO().getListNameArtist().contains(artist)&&
        MyMediaPlayer.getInstance().isCheckSongArtist()){
            if(MyArtistDatabase.getInstance(this).myArtistDAO().getArtistById(MyMediaPlayer.getInstance().getIdArtist()).getNameArtist()
            .equals(artist)){
                ArrayList<MySongObject> listSong= MyMediaPlayer.getInstance().getListPlaySong();
                listSong.add(mySongObject);
                Arrange(listSong);
                MyMediaPlayer.getInstance().setListPlaySong(listSong);
                for(int i=0;i<listSong.size();i++){
                    if(listSong.get(i).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                        MyMediaPlayer.getInstance().setPosition(i);
                        break;
                    }
                }
            }
        }
        else if(!MyMediaPlayer.getInstance().isCheckSongAlbum() && !MyMediaPlayer.getInstance().isCheckFavSong()){
            ArrayList<MySongObject> listSong= (ArrayList<MySongObject>) MySongsDatabase.getInstance(this).mySongsDAO().getListSong();
            Arrange(listSong);
            MyMediaPlayer.getInstance().setListPlaySong(listSong);
            for(int i=0;i<listSong.size();i++){
                if(listSong.get(i).getId_song()==MyMediaPlayer.getInstance().getIdSong()){
                    MyMediaPlayer.getInstance().setPosition(i);
                    break;
                }
            }
        }
        MyMediaPlayer.getInstance().setCheckUpdateSong(true);
        setResult(Activity.RESULT_OK);
        finish();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
    }

    private void Arrange(ArrayList<MySongObject> listSong) {
        Collections.sort(listSong, new Comparator<MySongObject>() {
            @Override
            public int compare(MySongObject mySongObject, MySongObject t1) {
                return mySongObject.getNameSong().compareToIgnoreCase(t1.getNameSong());
            }
        });
    }

    private void Camera(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    private void SelectImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_GALLERY);
    }

    public byte[] ImageView_to_Byte(){
        binding.imgSong.setDrawingCacheEnabled(true);
        binding.imgSong.buildDrawingCache();
        Bitmap bmp=Bitmap.createBitmap(binding.imgSong.getDrawingCache());
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[]  byteArray=stream.toByteArray();
        return byteArray;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPFILE && resultCode == Activity.RESULT_OK){

               String uri=data.getStringExtra("FileSongUri");
                binding.uriFileSong.setText(uri);
        }

        if (requestCode==REQUEST_CAMERA && resultCode==AddMySong.RESULT_OK && data!=null){
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            binding.imgSong.setImageBitmap(bitmap);
        }
        if (requestCode==REQUEST_GALLERY && resultCode==AddMySong.RESULT_OK && data!=null){
            Uri uri =data.getData();
            Bitmap bitmapImg= null;
            try {
                bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.imgSong.setImageBitmap(bitmapImg);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
    }
}