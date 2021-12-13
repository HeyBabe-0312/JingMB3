package com.example.jingmb3.model.offline;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

@Entity(tableName = "MySongs")
public class MySongObject implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id_song;
    @ColumnInfo(name = "Tên bài hát")
    private String nameSong="";
    @ColumnInfo(name = "Nghệ sĩ")
    private String nameArtist;
    @ColumnInfo(name = "Ảnh bài hát")
    private byte[] imageSong=null;
    @ColumnInfo(name = "Nhạc")
    private String linkSong="";
    @ColumnInfo(name = "Danh sách Album")
    private ArrayList<String> id_album=null;

    public MySongObject(){

    }
    public MySongObject( String nameSong, String nameArtist, byte[] imageSong, String linkSong) {
        this.nameSong = nameSong;
        this.nameArtist = nameArtist;
        this.imageSong = imageSong;
        this.linkSong = linkSong;
    }

    public int getId_song() {
        return id_song;
    }

    public void setId_song(int id_song) {
        this.id_song = id_song;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public byte[] getImageSong() {
        return imageSong;
    }

    public void setImageSong(byte[] imageSong) {
        this.imageSong = imageSong;
    }

    public String getLinkSong() {
        return linkSong;
    }

    public void setLinkSong(String linkSong) {
        this.linkSong = linkSong;
    }

    public ArrayList<String> getId_album() {
        return id_album;
    }

    public void setId_album(ArrayList<String> id_album) {
        this.id_album = id_album;
    }

    public static class IdAlbumConverters{
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
