package com.example.jingmb3.view.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.example.jingmb3.model.online.ArtistOnline;
import com.example.jingmb3.model.online.GetSongs;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.model.online.Upload;
import com.example.jingmb3.view.online.activity.SongOfAlbumOnl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArtistOnlineAdapter extends RecyclerView.Adapter<ArtistOnlineAdapter.ViewHolder> {
    List<ArtistOnline> artistList;
    Context context;
    DatabaseReference databaseReference;
    public ArtistOnlineAdapter(List<ArtistOnline> artistList, Context context) {
        this.artistList = artistList;
        this.context = context;
    }
    public void setGetArtist(List<ArtistOnline> getArtist) {
        this.artistList = getArtist;
        notifyDataSetChanged();
    }
    public interface clickArtistOnline{
        void clickArtistOnline(int position);
    }
    public ArtistOnlineAdapter.clickArtistOnline clickArtist;
    public void setClickArtist(ArtistOnlineAdapter.clickArtistOnline clickartistOnline){
        clickArtist = clickartistOnline;
    }
    @NonNull
    @Override
    public ArtistOnlineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artist_online,parent,false);
        return new ArtistOnlineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistOnlineAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        List<GetSongs> getSongsList = new ArrayList<>();
        final ArtistOnline artistOnline = artistList.get(position);
        holder.nameArtist.setText(artistOnline.getArtist());
        holder.nameArtist.setSelected(true);
        if(artistOnline.getUrl()!=null&&!artistOnline.getUrl().equals("")) Glide.with(context).load(artistOnline.getUrl()).into(holder.imgArtist);
        else holder.imgArtist.setImageResource(R.drawable.icon_artist);
        databaseReference = FirebaseDatabase.getInstance("https://musicupload-7dde0-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("songs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getSongsList.clear();
                for(DataSnapshot dss:snapshot.getChildren()){
                    GetSongs getSongs = dss.getValue(GetSongs.class);
                    getSongs.setmKey(dss.getKey());
                    if(artistOnline.getArtist().equals(getSongs.getArtist())){
                        getSongsList.add(getSongs);
                    }
                }
                holder.countSongs.setText(getSongsList.size()+" Bài hát");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickArtist.clickArtistOnline(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(artistList!=null) return artistList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameArtist;
        ImageView imgArtist;
        TextView countSongs;
        LinearLayout itemArtist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countSongs = itemView.findViewById(R.id.CountSongInArtistOnl);
            nameArtist = itemView.findViewById(R.id.nameArtistOnl);
            imgArtist = itemView.findViewById(R.id.imgArtistOnline);
            itemArtist = itemView.findViewById(R.id.item_artist_online);
        }
    }
}
