package com.example.jingmb3.view.activity.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jingmb3.R;
import com.example.jingmb3.model.offline.MySongObject;

import java.util.ArrayList;

public class SongsOfAlbumAdapter extends RecyclerView.Adapter<SongsOfAlbumAdapter.ViewHolder> {
    private ArrayList<MySongObject> ListSong;
    public interface ClickItemToPlay{
        void clickToPlay(int position);
    }
    public interface ClickOption{
        void clickOption(int postion);
    }
    public void setData(ArrayList<MySongObject> arrayList){
        ListSong=arrayList;
        notifyDataSetChanged();
    }
    ClickOption clickOption;
    ClickItemToPlay clickItemToPlay;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_of_album,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final MySongObject mySongObject=ListSong.get(position);
        holder.nameSong.setText(mySongObject.getNameSong());
        holder.nameSong.setSelected(true);
        holder.nameArtist.setText(mySongObject.getNameArtist());
        if(mySongObject.getImageSong()!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(mySongObject.getImageSong(),
                    0,mySongObject.getImageSong().length);
            holder.imgSong.setImageBitmap(bitmap);
        }
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemToPlay.clickToPlay(position);
            }
        });
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOption.clickOption(position);
            }
        });
    }
    public void ClickToPlay(ClickItemToPlay clickItemToPlay){
        this.clickItemToPlay=clickItemToPlay;
    }

    public void ClickOption(ClickOption clickOption){
        this.clickOption=clickOption;
    }
    @Override
    public int getItemCount() {
        if(ListSong!=null) return ListSong.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgSong,option;
        private TextView nameSong;
        private TextView nameArtist;
        private LinearLayout layout_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong=itemView.findViewById(R.id.imgSongAlbum);
            nameSong=itemView.findViewById(R.id.nameSongAlbum);
            nameArtist=itemView.findViewById(R.id.nameArtistAlbum);
            layout_item=itemView.findViewById(R.id.item_song_layout);
            option=itemView.findViewById(R.id.optionSong_of_Album);
        }
    }
}
