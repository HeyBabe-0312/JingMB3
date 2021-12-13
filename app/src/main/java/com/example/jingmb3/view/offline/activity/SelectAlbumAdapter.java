package com.example.jingmb3.view.offline.activity;

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
import com.example.jingmb3.model.offline.MyAlbumObject;
import com.example.jingmb3.model.offline.MySongObject;
import com.example.jingmb3.view.offline.fragment.ClickItemAlbum;

import java.util.ArrayList;

public class SelectAlbumAdapter extends RecyclerView.Adapter<SelectAlbumAdapter.ViewHolder> {
    private ArrayList<MyAlbumObject> ListAlbums;
    private ClickItemAlbum clickItemAlbum;
    @NonNull
    @Override
    public SelectAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_album,parent,false);
        return new ViewHolder(view);
    }

    public SelectAlbumAdapter(ArrayList<MyAlbumObject> listAlbums,ClickItemAlbum clickItemAlbum) {
        ListAlbums = listAlbums;
        this.clickItemAlbum=clickItemAlbum;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAlbumAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final MyAlbumObject myAlbumObject=ListAlbums.get(position);
        holder.nameAlbum.setText(myAlbumObject.getNameAlbum());
        holder.nameAlbum.setSelected(true);
        if(myAlbumObject.getImageAlbum()!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(myAlbumObject.getImageAlbum(),0,myAlbumObject.getImageAlbum().length);
            holder.imageAlbum.setImageBitmap(bitmap);}
        if(myAlbumObject.getId_song()==null)  holder.countSong.setText("0 Bài hát");
        else holder.countSong.setText(myAlbumObject.getId_song().size()+" Bài hát");
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemAlbum.ClickItemInAlbum(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(ListAlbums!= null) return ListAlbums.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageAlbum;
        private TextView nameAlbum,countSong;
        private LinearLayout layoutItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAlbum=itemView.findViewById(R.id.imgSelectAlbum);
            nameAlbum=itemView.findViewById(R.id.nameSelectAlbum);
            layoutItem=itemView.findViewById(R.id.layout_item_select_album);
            countSong=itemView.findViewById(R.id.CS_SelectAlbum);
        }
    }
    public void setData(ArrayList<MyAlbumObject> myAlbumObjectArrayList){
        this.ListAlbums=myAlbumObjectArrayList;
        notifyDataSetChanged();
    }
}

