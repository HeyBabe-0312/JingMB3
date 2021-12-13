package com.example.jingmb3.view.activity.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jingmb3.R;
import com.example.jingmb3.model.offline.MySongObject;

import java.util.ArrayList;


public class SearchMySongAdapter extends RecyclerView.Adapter<SearchMySongAdapter.ViewHolder> implements Filterable {
    private ArrayList<MySongObject> listSong;
    private ArrayList<MySongObject> listSongSearch;
    public static SearchMySongAdapter Instance;
    public static SearchMySongAdapter getInstance(){
        if(Instance==null){
            return Instance=new SearchMySongAdapter();
        }
        return Instance;
    }
    public interface ClickItem{
        void clickItem(int postion);
    }

    public void setListSong(ArrayList<MySongObject> listSong) {
        this.listSong = listSong;
    }

    public void setData(ArrayList<MySongObject> listSong){
        listSongSearch= listSong;
        this.listSong=listSong;
        notifyDataSetChanged();
    }
    public ClickItem clickItem;
    @NonNull
    @Override
    public SearchMySongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_my_song,parent,false);
        return new ViewHolder(view);
    }

    public void ClickItem(ClickItem clickItem){
        this.clickItem=clickItem;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchMySongAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MySongObject mySongObject=listSong.get(position);
        holder.nameSong.setText(mySongObject.getNameSong());
        holder.nameSong.setSelected(true);
        holder.nameArtist.setText(mySongObject.getNameArtist());
        if(mySongObject.getImageSong()!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(mySongObject.getImageSong(), 0,
                    mySongObject.getImageSong().length);
            holder.imgSong.setImageBitmap(bitmap);
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItem.clickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listSong!=null) return listSong.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgSong;
        private TextView nameSong,nameArtist;
        private LinearLayout item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong=itemView.findViewById(R.id.imgMySongSearch);
            nameArtist=itemView.findViewById(R.id.nameMyArtistSearch);
            nameSong=itemView.findViewById(R.id.nameSongSearch);
            item=itemView.findViewById(R.id.layoutItem_searchMySong);
        }
    }


    public ArrayList<MySongObject> getListSong() {
        return listSong;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String strSearch=charSequence.toString();

            if (strSearch.isEmpty()){
                listSong=listSongSearch;
            }
            else {
                ArrayList<MySongObject> arrayList=new ArrayList<>();
                for(MySongObject mySongObject : listSongSearch){
                    if (mySongObject.getNameSong().toLowerCase().contains(strSearch.toLowerCase())){
                        arrayList.add(mySongObject);}
                }
                listSong=arrayList;
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=listSong;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listSong= (ArrayList<MySongObject>) filterResults.values;
            notifyDataSetChanged();
        }
    };
}
