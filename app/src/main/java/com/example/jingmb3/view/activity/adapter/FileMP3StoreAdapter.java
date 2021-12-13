package com.example.jingmb3.view.activity.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jingmb3.R;
import com.example.jingmb3.view.offline.fragment.ClickItemFileMP3;

import java.util.ArrayList;

public class FileMP3StoreAdapter extends RecyclerView.Adapter<FileMP3StoreAdapter.ViewHolder>{
    ArrayList<String> ListNameFile;
    private ClickItemFileMP3 clickItemFileMP3;

    public FileMP3StoreAdapter(ArrayList<String> listNameFile,ClickItemFileMP3 clickItemFileMP3) {
        ListNameFile = listNameFile;
        this.clickItemFileMP3=clickItemFileMP3;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_file_mp3,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final String nameFile=ListNameFile.get(position);
        holder.nameFile.setText(nameFile);
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemFileMP3.OnClickItemFile(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(ListNameFile!= null) return ListNameFile.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameFile;
        private LinearLayout layout_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameFile=itemView.findViewById(R.id.nameFile);
            layout_item=itemView.findViewById(R.id.layout_item_file);
        }
    }
    public void setData(ArrayList<String> myFileSongArrayList){
        this.ListNameFile=myFileSongArrayList;
        notifyDataSetChanged();
    }
}
