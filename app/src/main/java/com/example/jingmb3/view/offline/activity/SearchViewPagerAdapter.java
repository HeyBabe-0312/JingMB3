package com.example.jingmb3.view.offline.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jingmb3.view.offline.fragment.SearchMyAlbum;
import com.example.jingmb3.view.offline.fragment.SearchMyArtist;
import com.example.jingmb3.view.offline.fragment.SearchMySong;

public class SearchViewPagerAdapter extends FragmentStateAdapter {
    public SearchViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SearchMySong();
            case 1:
                return new SearchMyAlbum();
            case 2:
                return new SearchMyArtist();
            default:
                return new SearchMySong();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
