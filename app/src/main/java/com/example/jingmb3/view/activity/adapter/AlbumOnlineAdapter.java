package com.example.jingmb3.view.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.example.jingmb3.R;
import com.example.jingmb3.model.offline.MyAlbumObject;
import com.example.jingmb3.model.online.GetSongs;
import com.example.jingmb3.model.online.Upload;
import com.example.jingmb3.view.offline.fragment.ClickItemAlbum;

import java.util.List;

public class AlbumOnlineAdapter extends RecyclerView.Adapter<AlbumOnlineAdapter.ViewHolder> {
    List<Upload> uploadList;
    Context context;

    public AlbumOnlineAdapter(List<Upload> uploadList, Context context) {
        this.uploadList = uploadList;
        this.context = context;
    }
    public void setGetAlbum(List<Upload> getAlbum) {
        this.uploadList = getAlbum;
        notifyDataSetChanged();
    }
    public interface clickAlbumOnline{
        void clickAlbumOnline(int position);
    }
    public AlbumOnlineAdapter.clickAlbumOnline clickAlbum;
    public void setClickAlbum(AlbumOnlineAdapter.clickAlbumOnline clickAlbumOnline){
        clickAlbum = clickAlbumOnline;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album_online,parent,false);
        return new AlbumOnlineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Upload upload= uploadList.get(position);
        holder.titleAlbum.setText(upload.getName());
        holder.cateAlbum.setText(upload.getSongsCategory());
        holder.titleAlbum.setSelected(true);
        holder.cateAlbum.setSelected(true);
        Glide.with(context).load(upload.getUrl()).into(holder.imgAlbum);
        holder.itemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAlbum.clickAlbumOnline(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(uploadList!=null) return uploadList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleAlbum;
        ImageView imgAlbum;
        TextView cateAlbum;
        LinearLayout itemAlbum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleAlbum = itemView.findViewById(R.id.nameAlbumOnline);
            cateAlbum = itemView.findViewById(R.id.songCategory);
            imgAlbum = itemView.findViewById(R.id.imgAlbumOnl);
            itemAlbum = itemView.findViewById(R.id.item_album_online);
        }
    }
}
