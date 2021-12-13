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
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.view.offline.fragment.IClickItemListener;


import java.util.ArrayList;


public class MySongAdapter extends RecyclerView.Adapter<MySongAdapter.ViewHolder>{
    private ArrayList<MySongObject> ListSongs;
    private IClickItemListener iClickItemListener;
    private Context mContext;
    public interface ClickMoreOption{
        void clickMoreOption(int position);
    }
    ClickMoreOption clickMoreOption;
    public void setClickMoreOption(ClickMoreOption clickMoreOption){
        this.clickMoreOption=clickMoreOption;
    }

    public MySongAdapter(Context context) {
        mContext=context;
    }

    public void IClickItemListener(IClickItemListener iClickItemListener){
        this.iClickItemListener=iClickItemListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_song,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final MySongObject mySongObject=ListSongs.get(position);
        holder.nameSong.setText(mySongObject.getNameSong());
        holder.nameArtist.setText(mySongObject.getNameArtist());
        holder.nameSong.setSelected(true);
        if(mySongObject.getImageSong()!=null){
        Bitmap bitmap= BitmapFactory.decodeByteArray(mySongObject.getImageSong(),0,mySongObject.getImageSong().length);
        holder.imageSong.setImageBitmap(bitmap);}
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.OnClickItemSongs(position);
            }
        });
        holder.moreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMoreOption.clickMoreOption(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(ListSongs!=null) return ListSongs.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameSong,nameArtist;
        private ImageView imageSong,moreOption;
        private LinearLayout layoutItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameSong=itemView.findViewById(R.id.nameMySong);
            nameArtist=itemView.findViewById(R.id.nameMyArtist);
            imageSong=itemView.findViewById(R.id.imageMySong);
            layoutItem=itemView.findViewById(R.id.item_song);
            moreOption=itemView.findViewById(R.id.more_option);
        }
    }
    public void setData(ArrayList<MySongObject> mySongObjectArrayList){
        this.ListSongs=mySongObjectArrayList;
        notifyDataSetChanged();
    }
}
