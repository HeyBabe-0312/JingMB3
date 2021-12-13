package com.example.jingmb3.view.offline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivityAddMyAlbumBinding;
import com.example.jingmb3.model.offline.MyAlbumDatabase;
import com.example.jingmb3.model.offline.MyAlbumObject;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.offline.MySongsDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddMyAlbum extends AppCompatActivity {

    ActivityAddMyAlbumBinding binding;
    private int REQUEST_GALLERY=20;
    private int REQUEST_CAMERA=50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddMyAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.DoneAddAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAlbum();
            }
        });

        binding.cameraAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera();
            }
        });
        binding.galleryImgAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
        binding.cancelAddAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
            }
        });

    }

    private boolean CheckAlbum(String name){
        ArrayList<String>  NameAlbumList=new ArrayList<String >();
        ArrayList<MyAlbumObject> listAlbum=new ArrayList<MyAlbumObject>();
        listAlbum= (ArrayList<MyAlbumObject>) MyAlbumDatabase.getInstance(this).myAlbumDAO().getMyAlbum();
        for(int i=0;i<listAlbum.size();i++){
            NameAlbumList.add(listAlbum.get(i).getNameAlbum());
        }
        if(NameAlbumList.contains(name)) return true;
        else return false;
    }
    private void SaveAlbum() {
        String name=binding.inNameAlbum.getText().toString().trim();
        if (name.isEmpty()){
            Toast.makeText(AddMyAlbum.this,"Hãy nhập đủ thông tin!",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(CheckAlbum(name)){
            Toast.makeText(AddMyAlbum.this,"Đã tồn tại Album!",Toast.LENGTH_SHORT).show();
            return;
        }
        MyAlbumObject myAlbumObject=new MyAlbumObject();
        myAlbumObject.setNameAlbum(name);
        myAlbumObject.setImageAlbum(ImageView_to_Byte());
        MyAlbumDatabase.getInstance(this).myAlbumDAO().insertAlbum(myAlbumObject);
        MyMediaPlayer.getInstance().setCheckUpdateAlbum(true);
        setResult(Activity.RESULT_OK);
        finish();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
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
        binding.imgAlbum.setDrawingCacheEnabled(true);
        binding.imgAlbum.buildDrawingCache();
        Bitmap bmp=Bitmap.createBitmap(binding.imgAlbum.getDrawingCache());
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[]  byteArray=stream.toByteArray();
        return byteArray;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CAMERA && resultCode==AddMySong.RESULT_OK && data!=null){
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            binding.imgAlbum.setImageBitmap(bitmap);
        }
        if (requestCode==REQUEST_GALLERY && resultCode==AddMySong.RESULT_OK && data!=null){
            Uri uri =data.getData();
            Bitmap bitmapImg= null;
            try {
                bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.imgAlbum.setImageBitmap(bitmapImg);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
    }
}