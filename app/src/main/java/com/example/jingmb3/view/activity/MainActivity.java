package com.example.jingmb3.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivityMainBinding;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.view.offline.activity.Search;
import com.example.jingmb3.view.online.activity.SearchOnline;
import com.example.jingmb3.view.online.fragment.MusicOnline;
import com.example.jingmb3.view.offline.fragment.MyMusic;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final int MUSIC_ONLINE=1;
    public static final int MY_MUSIC=2;
    private int mCurrentFragment=MUSIC_ONLINE;
    private long backPressedTime;
    private Toast toast;
    private ActivityMainBinding binding;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.navigationView.setItemIconTintList(null);
        binding.navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new MusicOnline());
        binding.navigationView.getMenu().findItem(R.id.MusicOnlineMenu).setChecked(true);
        binding.menuToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerlayout.openDrawer(GravityCompat.START);
            }
        });

        binding.searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentFragment==MUSIC_ONLINE){
                    Intent intent=new Intent(MainActivity.this, SearchOnline.class);
                    startActivityForResult(intent,15);
                    overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
                }
                else{
                    Intent intent=new Intent(MainActivity.this, Search.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_left_in,R.anim.slide_up_out);
                }
            }
        });
        LoadingDialog.getInstance().StartDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==15 && resultCode== Activity.RESULT_OK){
            MusicOnline musicOnline = (MusicOnline) getSupportFragmentManager().findFragmentById(R.id.frame_content);
            musicOnline.loadMediaPlay(MediaOnline.getInstance().getPosition());
            if(MediaOnline.getInstance().getMediaPlayer()!=null){
                musicOnline.completeSong();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.MusicOnlineMenu)
        {
            if(mCurrentFragment!=MUSIC_ONLINE){
                LoadingDialog.getInstance().StartDialog(this);
                replaceFragment(new MusicOnline());
                mCurrentFragment=MUSIC_ONLINE;
            }

        }  else if (id==R.id.MyMusicMenu)
        {
            if(mCurrentFragment!=MY_MUSIC){
                LoadingDialog.getInstance().StartDialog(this);
                replaceFragment(new MyMusic());
                mCurrentFragment=MY_MUSIC;
            }
        }
        binding.drawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment){
        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content,fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        if(backPressedTime+2000>System.currentTimeMillis()){
            toast.cancel();
            super.onBackPressed();
            return;
        }
        else{
            toast=Toast.makeText(MainActivity.this,"Nhấn nút quay lại lần nữa để thoát",Toast.LENGTH_SHORT);
            toast.show();
        }
        backPressedTime=System.currentTimeMillis();
    }

}