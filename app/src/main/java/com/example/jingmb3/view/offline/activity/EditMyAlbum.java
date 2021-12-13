package com.example.jingmb3.view.offline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivityEditMyAlbumBinding;
import com.example.jingmb3.model.offline.MyAlbumDatabase;
import com.example.jingmb3.model.offline.MyAlbumObject;
import com.example.jingmb3.model.offline.MyMediaPlayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditMyAlbum extends AppCompatActivity {

    private MyAlbumObject myAlbumObject;
    ActivityEditMyAlbumBinding binding;
    private int REQUEST_CAMERA=22;
    private int REQUEST_GALLERY=102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditMyAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int IdAlbum=getIntent().getIntExtra("IdAlbum",0);
        myAlbumObject= MyAlbumDatabase.getInstance(this).myAlbumDAO().getAlbumById(IdAlbum);
        binding.inNameAlbum.setText(myAlbumObject.getNameAlbum());
        if(myAlbumObject.getImageAlbum()!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(myAlbumObject.getImageAlbum(),0,
                    myAlbumObject.getImageAlbum().length);
            binding.imgEditAlbum.setImageBitmap(bitmap);
        }

        binding.cancelEditAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
            }
        });
        binding.cameraAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camera();
            }
        });
        binding.galleryImgAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        binding.DoneEditAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAlbum();
            }
        });
    }

    private void SaveAlbum() {
        String nameAlbum=binding.inNameAlbum.getText().toString().trim();
        if(nameAlbum.isEmpty()){
            Toast.makeText(this,"Hãy điền đủ thông tin!",Toast.LENGTH_SHORT).show();
            return;
        }
        myAlbumObject.setNameAlbum(nameAlbum);
        myAlbumObject.setImageAlbum(ImageView_to_Byte());
        MyAlbumDatabase.getInstance(this).myAlbumDAO().editAlbum(myAlbumObject);
        MyMediaPlayer.getInstance().setCheckUpdateAlbum(true);
        setResult(Activity.RESULT_OK);
        finish();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
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
        binding.imgEditAlbum.setDrawingCacheEnabled(true);
        binding.imgEditAlbum.buildDrawingCache();
        Bitmap bmp=Bitmap.createBitmap(binding.imgEditAlbum.getDrawingCache());
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[]  byteArray=stream.toByteArray();
        return byteArray;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_CAMERA && resultCode==AddMySong.RESULT_OK && data!=null){
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            binding.imgEditAlbum.setImageBitmap(bitmap);
        }
        if (requestCode==REQUEST_GALLERY && resultCode==AddMySong.RESULT_OK && data!=null){
            Uri uri =data.getData();
            Bitmap bitmapImg= null;
            try {
                bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.imgEditAlbum.setImageBitmap(bitmapImg);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
    }
}