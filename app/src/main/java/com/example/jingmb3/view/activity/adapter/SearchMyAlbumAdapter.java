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
import com.example.jingmb3.model.offline.MyAlbumObject;

import java.util.ArrayList;

public class SearchMyAlbumAdapter extends RecyclerView.Adapter<SearchMyAlbumAdapter.ViewHolder> implements Filterable {
    ArrayList<MyAlbumObject> listAlbum;
    ArrayList<MyAlbumObject> listAlbumSearch;

    public static SearchMyAlbumAdapter Instance;
    public static SearchMyAlbumAdapter getInstance(){
        if(Instance==null){
            return Instance=new SearchMyAlbumAdapter();
        }
        return Instance;
    }

    public void setListAlbum(ArrayList<MyAlbumObject> listAlbum) {
        this.listAlbum = listAlbum;
    }

    public void setData(ArrayList<MyAlbumObject> listAlbum){
        this.listAlbum=listAlbum;
        listAlbumSearch=listAlbum;
        notifyDataSetChanged();
    }

    public interface ClickItem{
        void clickItem(int postion);
    }

    public ClickItem clickItem;

    public void ClickItem(ClickItem clickItem){
        this.clickItem=clickItem;
    }

    public ArrayList<MyAlbumObject> getListAlbum() {
        return listAlbum;
    }

    @NonNull
    @Override
    public SearchMyAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_my_album,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchMyAlbumAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MyAlbumObject myAlbumObject=listAlbum.get(position);
        holder.nameAlbum.setSelected(true);
        holder.nameAlbum.setText(myAlbumObject.getNameAlbum());
        if(myAlbumObject.getId_song()==null)  holder.countSong.setText("0 Bài hát");
        else holder.countSong.setText(myAlbumObject.getId_song().size()+" Bài hát");
        if(myAlbumObject.getImageAlbum()!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(myAlbumObject.getImageAlbum(),0,
                    myAlbumObject.getImageAlbum().length);
            holder.imgAlbum.setImageBitmap(bitmap);
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
        if(listAlbum!=null) return listAlbum.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAlbum;
        private TextView nameAlbum,countSong;
        private LinearLayout item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum=itemView.findViewById(R.id.imgMyAlbumSearch);
            nameAlbum=itemView.findViewById(R.id.nameAlbumSearch);
            countSong=itemView.findViewById(R.id.CountSongInAlbumSearch);
            item=itemView.findViewById(R.id.layoutItem_searchMyAlbum);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String strSearch=charSequence.toString().trim();

            if (strSearch.isEmpty()){
                listAlbum=listAlbumSearch;
            }
            else {
                ArrayList<MyAlbumObject> arrayList=new ArrayList<>();
                for(MyAlbumObject myAlbumObject : listAlbumSearch){
                    if (myAlbumObject.getNameAlbum().toLowerCase().contains(strSearch.toLowerCase())){
                        arrayList.add(myAlbumObject);}
                }
                listAlbum=arrayList;
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=listAlbum;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listAlbum= (ArrayList<MyAlbumObject>) filterResults.values;
            notifyDataSetChanged();
        }
    };
}
