package com.example.jingmb3.view.online.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.jingmb3.R;
import com.example.jingmb3.databinding.ActivitySearchOnlineBinding;
import com.example.jingmb3.view.online.fragment.SearchAlbumOnl;
import com.example.jingmb3.view.online.fragment.SearchArtistOnl;
import com.example.jingmb3.view.online.fragment.SearchSongOnl;
import com.google.android.material.navigation.NavigationBarView;

public class SearchOnline extends AppCompatActivity {
    ActivitySearchOnlineBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchOnlineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int id=binding.search.getContext()
                .getResources().getIdentifier("android:id/search_src_text",null,null);
        TextView searchEditText=binding.search.findViewById(id);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.gray));

        binding.cancleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
            }
        });
        SearchOnlAdapter adapter = new SearchOnlAdapter(this);
        binding.viewpagerSearch.setAdapter(adapter);
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
            public boolean onQueryTextSubmit(String s) {
                SearchSongOnl.getInstance().setSearchQuery(s);
                SearchAlbumOnl.getInstance().setSearchQuery(s);
                SearchArtistOnl.getInstance().setSearchQuery(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                SearchSongOnl.getInstance().setSearchQuery(s);
                SearchAlbumOnl.getInstance().setSearchQuery(s);
                SearchArtistOnl.getInstance().setSearchQuery(s);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_right_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.search.clearFocus();
    }
}