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
import com.example.jingmb3.model.offline.MyAlbumObject;
import com.example.jingmb3.view.offline.fragment.ClickItemAlbum;

import java.util.ArrayList;

public class MyAlbumAdapter extends RecyclerView.Adapter<MyAlbumAdapter.ViewHolder> {
    private ArrayList<MyAlbumObject> ListAlbums;
    private ClickItemAlbum clickItemAlbum;
    public interface ClickOptionAlbum{
        void clickOptionAlbum(int position);
    }
    private ClickOptionAlbum clickOptionAlbum;
    @NonNull
    @Override
    public MyAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_album,parent,false);
        return new ViewHolder(view);
    }

    public MyAlbumAdapter(ArrayList<MyAlbumObject> listAlbums,ClickItemAlbum clickItemAlbum) {
        ListAlbums = listAlbums;
        this.clickItemAlbum=clickItemAlbum;
    }
    public void setClickOptionAlbum(ClickOptionAlbum clickOptionAlbum){
        this.clickOptionAlbum=clickOptionAlbum;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAlbumAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        holder.optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOptionAlbum.clickOptionAlbum(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(ListAlbums!= null) return ListAlbums.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageAlbum, optionBtn;
        private TextView nameAlbum,countSong;
        private LinearLayout layoutItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAlbum=itemView.findViewById(R.id.imgMyAlbum);
            nameAlbum=itemView.findViewById(R.id.nameMyAlbum);
            layoutItem=itemView.findViewById(R.id.layout_item_album);
            countSong=itemView.findViewById(R.id.CountSongInAlbum);
            optionBtn=itemView.findViewById(R.id.optionAlbum);
        }
    }
    public void setData(ArrayList<MyAlbumObject> myAlbumObjectArrayList){
        this.ListAlbums=myAlbumObjectArrayList;
        notifyDataSetChanged();
    }
}
