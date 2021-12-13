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
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.model.online.GetSongs;
import com.example.jingmb3.model.online.MediaOnline;

import java.util.ArrayList;
import java.util.List;

public class SongOfAlbumOnlAdapter extends RecyclerView.Adapter<SongOfAlbumOnlAdapter.ViewHolder> {
    List<GetSongs> getSongsList;
    Context context;

    public SongOfAlbumOnlAdapter(Context context) {
        this.context = context;
    }

    public interface ClickItemToPlay{
        void clickToPlay(int position);
    }
    public void setData(List<GetSongs> arrayList){
        this.getSongsList=arrayList;
        notifyDataSetChanged();
    }
    ClickItemToPlay clickItemToPlay;
    @NonNull
    @Override
    public SongOfAlbumOnlAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_of_album_onl,parent,false);
        return new SongOfAlbumOnlAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongOfAlbumOnlAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final GetSongs mySong=getSongsList.get(position);
        holder.nameSong.setText(mySong.getSongTitle());
        holder.nameSong.setSelected(true);
        holder.nameArtist.setText(mySong.getArtist());
        holder.nameArtist.setSelected(true);
        if(mySong.getAlbum_art()!=null&& !mySong.getAlbum_art().equals("")){
            Glide.with(context)
                    .asBitmap()
                    .load(getSongsList.get(position).getAlbum_art())
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
    public void setGetSongs(List<GetSongs> getSongs) {
        this.getSongsList = getSongs;
        notifyDataSetChanged();
    }
    public void ClickToPlay(SongOfAlbumOnlAdapter.ClickItemToPlay clickItemToPlay){
        this.clickItemToPlay=clickItemToPlay;
    }
    @Override
    public int getItemCount() {
        if(getSongsList!=null) return getSongsList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imgSong;
            private TextView nameSong;
            private TextView nameArtist;
            private LinearLayout layout_item;
        public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imgSong=itemView.findViewById(R.id.imgSongAlbumOnl);
                nameSong=itemView.findViewById(R.id.nameSongAlbumOnl);
                nameArtist=itemView.findViewById(R.id.nameArtistAlbumOnl);
                layout_item=itemView.findViewById(R.id.item_song_album_onl);
        }
    }
}
