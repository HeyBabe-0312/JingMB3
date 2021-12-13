package com.example.jingmb3.model.offline;

import androidx.core.view.WindowInsetsCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.graph.ElementOrder;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.net.Proxy;
import java.security.KeyRep;
import java.util.ArrayList;

@Entity(tableName = "MyAlbum")
public class MyAlbumObject {
    @PrimaryKey(autoGenerate = true)
    private int id_album;
    @ColumnInfo(name = "Tên Album")
    private String nameAlbum;
    @ColumnInfo(name = "Ảnh Album")
    private byte[] imageAlbum;
    @ColumnInfo(name = "Tên nghệ sĩ")
    private String nameArstist;
    @ColumnInfo(name = "Danh sách bài hát")
    private ArrayList<String> id_song=null;

    public MyAlbumObject(){}
    public MyAlbumObject(String nameAlbum, byte[] imageAlbum, String nameArstist) {
        this.nameAlbum = nameAlbum;
        this.imageAlbum = imageAlbum;
        this.nameArstist = nameArstist;
    }

    public int getId_album() {
        return id_album;
    }

    public void setId_album(int id_album) {
        this.id_album = id_album;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public byte[] getImageAlbum() {
        return imageAlbum;
    }

    public void setImageAlbum(byte[] imageAlbum) {
        this.imageAlbum = imageAlbum;
    }

    public String getNameArstist() {
        return nameArstist;
    }

    public void setNameArstist(String nameArstist) {
        this.nameArstist = nameArstist;
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
