package com.example.jingmb3.view.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jingmb3.R;
import com.example.jingmb3.model.offline.FavoriteDatabase;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.online.GetSongs;
import com.example.jingmb3.model.online.MediaOnline;

import java.util.ArrayList;
import java.util.List;

public class SongOfArtistOnlAdapter extends RecyclerView.Adapter<SongOfArtistOnlAdapter.ViewHolder> {
    List<GetSongs> getSongList;
    Context context;
    public interface ClickItemToPlay{
        void clickToPlay(int position);
    }

    public SongOfArtistOnlAdapter(Context context) {
        this.context=context;
    }

    public void setData(List<GetSongs> arrayList){
        getSongList=arrayList;
        notifyDataSetChanged();
    }
    public ClickItemToPlay clickItemToPlay;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_of_artist_onl,parent,false);
        return new SongOfArtistOnlAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final GetSongs mySongObject=getSongList.get(position);
        holder.nameSong.setText(mySongObject.getSongTitle());
        holder.nameSong.setSelected(true);
        holder.nameArtist.setText(mySongObject.getArtist());
        holder.nameArtist.setSelected(true);
        if(mySongObject.getAlbum_art()!=null&& !mySongObject.getAlbum_art().equals("")){
            Glide.with(context)
                    .asBitmap()
                    .load(getSongList.get(position).getAlbum_art())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            holder.imgSong.setImageBitmap(resource);
                            MediaOnline.getInstance().setUrlImage(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        else{
            holder.imgSong.setImageResource(R.drawable.icon_music);
        }
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemToPlay.clickToPlay(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(getSongList!=null) return getSongList.size();
        return 0;
    }
    public void ClickToPlay(SongOfArtistOnlAdapter.ClickItemToPlay clickItemToPlay){
        this.clickItemToPlay=clickItemToPlay;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgSong;
        private TextView nameSong;
        private TextView nameArtist;
        private LinearLayout layout_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong=itemView.findViewById(R.id.imgSongArtistOnl);
            nameSong=itemView.findViewById(R.id.nameSongArtistOnl);
            nameArtist=itemView.findViewById(R.id.nameArtistSongOnl);
            layout_item=itemView.findViewById(R.id.item_artist_song_onl);
        }
    }
}
