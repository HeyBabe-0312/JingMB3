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

import com.example.jingmb3.R;
import com.example.jingmb3.model.offline.FavoriteDatabase;
import com.example.jingmb3.model.offline.MySongObject;

import java.util.ArrayList;

public class SongOfArtistAdapter extends RecyclerView.Adapter<SongOfArtistAdapter.ViewHolder>{
    private ArrayList<MySongObject> ListSong;
    private Context mContext;
    public interface ClickItemToPlay{
        void clickToPlay(int position);
    }

    public interface ClickFavBtn{
        void clickFavBtn(int ID);
    }

    public SongOfArtistAdapter(Context context) {
        mContext=context;
    }

    public void setData(ArrayList<MySongObject> arrayList){
        ListSong=arrayList;
        notifyDataSetChanged();
    }
    public ClickFavBtn clickFavBtn;
    public ClickItemToPlay clickItemToPlay;
    @NonNull
    @Override
    public SongOfArtistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_of_artist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongOfArtistAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final MySongObject mySongObject=ListSong.get(position);
        holder.nameSong.setText(mySongObject.getNameSong());
        holder.nameSong.setSelected(true);
        holder.nameArtist.setText(mySongObject.getNameArtist());
        if(mySongObject.getImageSong()!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(mySongObject.getImageSong(),
                    0,mySongObject.getImageSong().length);
            holder.imgSong.setImageBitmap(bitmap);
        }
        if(FavoriteDatabase.getInstance(mContext).favoriteDAO().getListIdSong().contains(mySongObject.getId_song())){
            holder.favBtn.setImageResource(R.drawable.ic_favorite);
        }
        else holder.favBtn.setImageResource(R.drawable.ic_favorite_border);
        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FavoriteDatabase.getInstance(mContext).favoriteDAO().getListIdSong().contains(mySongObject.getId_song())){
                    holder.favBtn.setImageResource(R.drawable.ic_favorite_border);
                }
                else holder.favBtn.setImageResource(R.drawable.ic_favorite);
                clickFavBtn.clickFavBtn(mySongObject.getId_song());
            }
        });
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemToPlay.clickToPlay(position);
            }
        });
    }
    public void ClickToPlay(ClickItemToPlay clickItemToPlay){
        this.clickItemToPlay=clickItemToPlay;
    }

    public void ClickFavBtn(ClickFavBtn clickFavBtn){
        this.clickFavBtn=clickFavBtn;
    }
    @Override
    public int getItemCount() {
        if(ListSong!=null) return ListSong.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgSong,favBtn;
        private TextView nameSong;
        private TextView nameArtist;
        private LinearLayout layout_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong=itemView.findViewById(R.id.imgSongArtist);
            nameSong=itemView.findViewById(R.id.nameSongArtist);
            nameArtist=itemView.findViewById(R.id.MyArtist);
            favBtn=itemView.findViewById(R.id.FavBtn);
            layout_item=itemView.findViewById(R.id.song_layout_item);
        }
    }
}
