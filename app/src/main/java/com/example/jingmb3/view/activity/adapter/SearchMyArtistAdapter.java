package com.example.jingmb3.view.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jingmb3.R;
import com.example.jingmb3.model.offline.MyArtistObject;
import com.example.jingmb3.model.offline.MySongsDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchMyArtistAdapter extends RecyclerView.Adapter<SearchMyArtistAdapter.ViewHolder> implements Filterable {
    private ArrayList<MyArtistObject> ListArtists,listArtistSearch;
    private Context mContext;
    public static SearchMyArtistAdapter Instance;
    public static SearchMyArtistAdapter getInstance(){
        if(Instance==null){
            return Instance=new SearchMyArtistAdapter();
        }
        return Instance;
    }

    public void setData(ArrayList<MyArtistObject> listArtists,Context context){
        ListArtists=listArtists;
        listArtistSearch=listArtists;
        mContext=context;
        notifyDataSetChanged();
    }

    public void setListArtists(ArrayList<MyArtistObject> listArtists) {
        ListArtists = listArtists;
    }

    public ArrayList<MyArtistObject> getListArtists() {
        return ListArtists;
    }

    public interface ClickItem{
        void clickItem(int postion);
    }

    public ClickItem clickItem;

    public void ClickItem(ClickItem clickItem){
        this.clickItem=clickItem;
    }

    @NonNull
    @Override
    public SearchMyArtistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_my_artist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchMyArtistAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MyArtistObject myArtistObject=ListArtists.get(position);
        holder.nameArtist.setSelected(true);
        holder.nameArtist.setText(myArtistObject.getNameArtist());
        holder.countSong.setText(MySongsDatabase.getInstance(mContext).mySongsDAO()
                .getListSongByArtist(myArtistObject.getNameArtist()).size()+" Bài hát");
        if(myArtistObject.getImageArtist()!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(myArtistObject.getImageArtist(),0,
                    myArtistObject.getImageArtist().length);
            holder.imgArtist.setImageBitmap(bitmap);
        } else holder.imgArtist.setImageResource(R.drawable.icon_artist);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItem.clickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(ListArtists!=null) return ListArtists.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgArtist;
        private TextView nameArtist,countSong;
        private LinearLayout item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArtist=itemView.findViewById(R.id.imgMyArtistSearch);
            nameArtist=itemView.findViewById(R.id.nameArtistSearch);
            countSong=itemView.findViewById(R.id.CountSongInArtistSearch);
            item=itemView.findViewById(R.id.layoutItem_searchMyArtist);
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
                ListArtists=listArtistSearch;
            }
            else {
                ArrayList<MyArtistObject> arrayList=new ArrayList<>();
                for(MyArtistObject myArtistObject : listArtistSearch){
                    if (myArtistObject.getNameArtist().toLowerCase().contains(strSearch.toLowerCase())){
                        arrayList.add(myArtistObject);}
                }
                ListArtists=arrayList;
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=ListArtists;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ListArtists= (ArrayList<MyArtistObject>) filterResults.values;
            notifyDataSetChanged();
        }
    };
}
