package com.example.jingmb3.view.online.activity;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jingmb3.view.online.fragment.SearchAlbumOnl;
import com.example.jingmb3.view.online.fragment.SearchArtistOnl;
import com.example.jingmb3.view.online.fragment.SearchSongOnl;

public class SearchOnlAdapter extends FragmentStateAdapter {
    public SearchOnlAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SearchSongOnl();
            case 1:
                return new SearchAlbumOnl();
            case 2:
                return new SearchArtistOnl();
            default:
                return new SearchSongOnl();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
