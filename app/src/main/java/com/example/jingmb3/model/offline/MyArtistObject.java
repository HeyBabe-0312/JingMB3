package com.example.jingmb3.model.offline;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
@Entity(tableName = "MyArtist")
public class MyArtistObject {
    @PrimaryKey(autoGenerate = true)
    private int id_artist;
    @ColumnInfo(name = "Tên nghệ sĩ")
    private String nameArtist;
    @ColumnInfo(name = "Ảnh nghệ sĩ")
    private byte[] imageArtist;
    @ColumnInfo(name = "Danh sách bài hát")
    private ArrayList<String> id_song=null;

    public MyArtistObject(){}

    public int getId_artist() {
        return id_artist;
    }

    public void setId_artist(int id_artist) {
        this.id_artist = id_artist;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public byte[] getImageArtist() {
        return imageArtist;
    }

    public void setImageArtist(byte[] imageArtist) {
        this.imageArtist = imageArtist;
    }

    public ArrayList<String> getId_song() {
        return id_song;
    }

    public void setId_song(ArrayList<String> id_song) {
        this.id_song = id_song;
    }

    public static class IdSongConverters{
        @TypeConverter
        public static ArrayList<String> fromString(String value){
            Type listType=new TypeToken<ArrayList<String>>(){}.getType();
            return  new Gson().fromJson(value,listType);
        }

        @TypeConverter
        public static String fromArrayList(ArrayList<String> list){
            Gson gson=new Gson();
            String json=gson.toJson(list);
            return json;
        }
    }
}

