package com.example.jingmb3.view.offline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivitySearchBinding;
import com.example.jingmb3.model.offline.ListSearch;
import com.example.jingmb3.model.offline.MyMediaPlayer;
import com.example.jingmb3.view.activity.adapter.SearchMyAlbumAdapter;
import com.example.jingmb3.view.activity.adapter.SearchMyArtistAdapter;
import com.example.jingmb3.view.activity.adapter.SearchMySongAdapter;
import com.example.jingmb3.view.offline.fragment.SearchMyAlbum;
import com.example.jingmb3.view.offline.fragment.SearchMyArtist;
import com.example.jingmb3.view.offline.fragment.SearchMySong;
import com.google.android.material.navigation.NavigationBarView;

public class Search extends AppCompatActivity {

    public static ActivitySearchBinding binding;

    public static ActivitySearchBinding getBinding() {
        return binding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int id=binding.search.getContext()
                 .getResources().getIdentifier("android:id/search_src_text",null,null);
        TextView searchEditText=binding.search.findViewById(id);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.gray));
        ListSearch.getInstance().setCheckSearch(true);
        binding.cancleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListSearch.getInstance().setCheckSearch(false);
                binding.search.clearFocus();
                finish();
                overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
            }
        });

        SearchViewPagerAdapter searchViewPagerAdapter=new SearchViewPagerAdapter(this);
        binding.viewpagerSearch.setAdapter(searchViewPagerAdapter);
        binding.viewpagerSearch.setOffscreenPageLimit(2);
        binding.bottomNaviSearch.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.bottom_song){
                    binding.viewpagerSearch.setCurrentItem(0);
                }
                else if(id==R.id.bottom_album){
                    binding.viewpagerSearch.setCurrentItem(1);
                }
                else if(id==R.id.bottom_artist){
                    binding.viewpagerSearch.setCurrentItem(2);
                }
                return false;
            }
        });

        binding.viewpagerSearch.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        binding.bottomNaviSearch.getMenu().findItem(R.id.bottom_song).setChecked(true);
                        break;
                    case 1:
                        binding.bottomNaviSearch.getMenu().findItem(R.id.bottom_album).setChecked(true);
                        break;
                    case 2:
                        binding.bottomNaviSearch.getMenu().findItem(R.id.bottom_artist).setChecked(true);
                        break;
                }
            }
        });

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchMySong.getInstance().setSearchQuery(query);
                SearchMyAlbum.getInstance().setSearchQuery(query);
                SearchMyArtist.getInstance().setSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchMySong.getInstance().setSearchQuery(newText);
                SearchMyAlbum.getInstance().setSearchQuery(newText);
                SearchMyArtist.getInstance().setSearchQuery(newText);
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.search.clearFocus();
    }
}