package com.example.jingmb3.view.activity.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jingmb3.model.offline.FavoriteDatabase;
import com.example.jingmb3.model.offline.FavoriteObject;
import com.example.jingmb3.view.offline.fragment.MyAlbum;
import com.example.jingmb3.view.offline.fragment.MyArtists;
import com.example.jingmb3.view.offline.fragment.MyFavoriteSongs;
import com.example.jingmb3.view.offline.fragment.MyMusic;
import com.example.jingmb3.view.offline.fragment.MySongs;

import java.util.ArrayList;

public class MyMusicViewPagerAdapter extends FragmentStateAdapter {


    public MyMusicViewPagerAdapter(@NonNull MyMusic fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MySongs();
            case 1:
                return new MyAlbum();
            case 2:
                return new MyArtists();
            case 3:
                return new MyFavoriteSongs();
            default:
                return new MySongs();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
