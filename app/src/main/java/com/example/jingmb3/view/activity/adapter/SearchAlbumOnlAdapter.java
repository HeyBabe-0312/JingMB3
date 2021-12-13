package com.example.jingmb3.view.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jingmb3.R;
import com.example.jingmb3.model.online.GetSongs;
import com.example.jingmb3.model.online.MediaOnline;
import com.example.jingmb3.model.online.Upload;

import java.util.ArrayList;
import java.util.List;

public class SearchAlbumOnlAdapter extends RecyclerView.Adapter<SearchAlbumOnlAdapter.ViewHolder> implements Filterable {
    List<Upload> uploadList;
    List<Upload> uploadFilterList;
    Context context;
    public static SearchAlbumOnlAdapter intance;
    public static SearchAlbumOnlAdapter getInstance(){
        if(intance==null) return intance = new SearchAlbumOnlAdapter();
        return intance;
    }
    public interface clickAlbumOnline{
        void clickAlbumOnline(int position);
    }
    public clickAlbumOnline clickAlbum;
    public void setClickAlbum(clickAlbumOnline clickAlbumOnline){
        clickAlbum = clickAlbumOnline;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_my_album,parent,false);
        return new ViewHolder(view);
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
                uploadList = uploadFilterList;
            }
            else{
                List<Upload> arrayList = new ArrayList<>();
                for(Upload i:uploadFilterList){
                    if(i.getName().toLowerCase().contains(search.toLowerCase())){
                        arrayList.add(i);
                    }
                }
                uploadList = arrayList;
            }
            FilterResults results = new FilterResults();
            results.values = uploadList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            uploadList = (List<Upload>) filterResults.values;
            notifyDataSetChanged();
        }
    };
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Upload upload= uploadList.get(position);
        holder.titleAlbum.setText(upload.getName());
        holder.cateAlbum.setText(upload.getSongsCategory());
        holder.titleAlbum.setSelected(true);
        holder.cateAlbum.setSelected(true);
        Glide.with(context).load(upload.getUrl()).into(holder.imgAlbum);
        holder.itemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAlbum.clickAlbumOnline(position);
            }
        });
    }
    public void setGetAlbum(List<Upload> getAlbum, Context context) {
        this.uploadFilterList = getAlbum;
        this.uploadList = getAlbum;
        this.context = context;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(uploadList!=null) {
            return uploadList.size();
        }
        return 0;
    }

    public List<Upload> getListAlbum(){
        return uploadList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleAlbum;
        ImageView imgAlbum;
        TextView cateAlbum;
        LinearLayout itemAlbum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleAlbum = itemView.findViewById(R.id.nameAlbumSearch);
            cateAlbum = itemView.findViewById(R.id.CountSongInAlbumSearch);
            imgAlbum = itemView.findViewById(R.id.imgMyAlbumSearch);
            itemAlbum = itemView.findViewById(R.id.layoutItem_searchMyAlbum);
        }
        }
    }

