package com.example.jingmb3.view.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jingmb3.R;
import com.example.jingmb3.model.offline.MyArtistObject;
import com.example.jingmb3.model.offline.MySongsDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyArtistAdapter extends RecyclerView.Adapter<MyArtistAdapter.ViewHolder>{
        private ArrayList<MyArtistObject> ListArtists;
        private Context context;
        public interface ClickItemArtist{
            void clickItemArtist(int IdArtist);
        }
        ClickItemArtist clickItemArtist;
        public void ClickItemArtist(ClickItemArtist clickItemArtist){
            this.clickItemArtist=clickItemArtist;
        }

    public MyArtistAdapter(Context context) {
        this.context= context;
    }

    @NonNull
        @Override
        public MyArtistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_my_artist,parent,false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull MyArtistAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            final MyArtistObject myArtistObject=ListArtists.get(position);
            holder.nameArtist.setText(myArtistObject.getNameArtist());
            holder.nameArtist.setSelected(true);
            holder.countSong.setText(MySongsDatabase.getInstance(context).mySongsDAO()
                    .getListSongByArtist(myArtistObject.getNameArtist()).size()+" Bài hát");
            if(myArtistObject.getImageArtist()!=null){
                Bitmap bitmap=BitmapFactory.decodeByteArray(myArtistObject.getImageArtist(),0,
                        myArtistObject.getImageArtist().length);
                holder.imageArtist.setImageBitmap(bitmap);
            }else holder.imageArtist.setImageResource(R.drawable.icon_artist);
            holder.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItemArtist.clickItemArtist(myArtistObject.getId_artist());
                }
            });
        }

        @Override
        public int getItemCount() {
            if(ListArtists!= null) return ListArtists.size();
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private CircleImageView imageArtist;
            private TextView nameArtist,countSong;
            private LinearLayout layoutItem;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageArtist=itemView.findViewById(R.id.imgMyArtist);
                nameArtist=itemView.findViewById(R.id.NameMyArtist);
                countSong=itemView.findViewById(R.id.CountSongInArtist);
                layoutItem=itemView.findViewById(R.id.layout_item_artist);
            }
        }
        public void setData(ArrayList<MyArtistObject> myArtistObjectArrayList){
            this.ListArtists=myArtistObjectArrayList;
            notifyDataSetChanged();
        }
}
