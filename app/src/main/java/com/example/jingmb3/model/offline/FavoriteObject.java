package com.example.jingmb3.model.offline;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
@Entity(tableName = "FavoriteSongs")
public class FavoriteObject {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    @ColumnInfo(name = "Tên bài hát")
    private String nameSong="";
    @ColumnInfo(name = "Nghệ sĩ")
    private String nameArtist;
    @ColumnInfo(name = "Ảnh bài hát")
    private byte[] imageSong=null;
    @ColumnInfo(name = "Nhạc")
    private String linkSong="";
    @ColumnInfo(name = "ID bài hát")
    private int id_song;

    public FavoriteObject(String nameSong, String nameArtist, byte[] imageSong, String linkSong, int id_song) {
        this.nameSong = nameSong;
        this.nameArtist = nameArtist;
        this.imageSong = imageSong;
        this.linkSong = linkSong;
        this.id_song = id_song;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public int getId_song() {
        return id_song;
    }

    public void setId_song(int id_song) {
        this.id_song = id_song;
    }
}
