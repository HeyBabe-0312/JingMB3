package com.example.jingmb3.view.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.example.jingmb3.R;
import com.example.jingmb3.model.online.ArtistOnline;
import com.example.jingmb3.model.online.GetSongs;
import com.example.jingmb3.model.online.Upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchArtistOnlAdapter extends RecyclerView.Adapter<SearchArtistOnlAdapter.ViewHolder> implements Filterable {
    List<ArtistOnline> artistList;
    List<ArtistOnline> artistFilterList;
    Context context;
    DatabaseReference databaseReference;
    public static SearchArtistOnlAdapter intance;
    public static SearchArtistOnlAdapter getInstance(){
        if(intance==null) return intance = new SearchArtistOnlAdapter();
        return intance;
    }
    public void setGetArtist(List<ArtistOnline> getArtist,Context context) {
        this.artistFilterList = getArtist;
        this.context = context;
        this.artistList = getArtist;
        notifyDataSetChanged();
    }
    public interface clickArtistOnline{
        void clickArtistOnline(int position);
    }
    public clickArtistOnline clickArtist;
    public void setClickArtist(clickArtistOnline clickartistOnline){
        clickArtist = clickartistOnline;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_my_artist,parent,false);
        return new ViewHolder(view);
    }
    public List<ArtistOnline> getArtistList(){
        return artistList;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        List<GetSongs> getSongsList = new ArrayList<>();
        ArtistOnline artistOnline = artistList.get(position);
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
            countSongs = itemView.findViewById(R.id.CountSongInArtistSearch);
            nameArtist = itemView.findViewById(R.id.nameArtistSearch);
            imgArtist = itemView.findViewById(R.id.imgMyArtistSearch);
            itemArtist = itemView.findViewById(R.id.layoutItem_searchMyArtist);
        }
    }
    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String search = charSequence.toString();
            if (search.isEmpty()){
                artistList = artistFilterList;
            }
            else{
                List<ArtistOnline> arrayList = new ArrayList<>();
                for(ArtistOnline i:artistFilterList){
                    if(i.getArtist().toLowerCase().contains(search.toLowerCase())){
                        arrayList.add(i);
                    }
                }
                artistList = arrayList;
            }
            FilterResults results = new FilterResults();
            results.values = artistList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            artistList = (List<ArtistOnline>) filterResults.values;
            notifyDataSetChanged();
        }
    };
}
