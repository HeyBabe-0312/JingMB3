package com.example.jingmb3.model.offline;

import java.util.ArrayList;

public class ListSearch {

    public static ListSearch Instance;
    public static ListSearch getInstance(){
        if(Instance==null){
            return Instance=new ListSearch();
        }
        return Instance;
    }
    ArrayList<MySongObject> listSong;
    ArrayList<MyAlbumObject> listAlbum;
    ArrayList<MyArtistObject> listArtist;
    boolean checkUpdateListSong=false;
    boolean CheckUpdateListAlbum=false;
    boolean CheckUpdateListArtist=false;
    boolean CheckSearch=false;

    public boolean isCheckUpdateListSong() {
        return checkUpdateListSong;
    }

    public void setCheckUpdateListSong(boolean checkUpdateListSong) {
        this.checkUpdateListSong = checkUpdateListSong;
    }

    public boolean isCheckUpdateListAlbum() {
        return CheckUpdateListAlbum;
    }

    public void setCheckUpdateListAlbum(boolean checkUpdateListAlbum) {
        CheckUpdateListAlbum = checkUpdateListAlbum;
    }

    public boolean isCheckUpdateListArtist() {
        return CheckUpdateListArtist;
    }

    public void setCheckUpdateListArtist(boolean checkUpdateListArtist) {
        CheckUpdateListArtist = checkUpdateListArtist;
    }

    public boolean isCheckSearch() {
        return CheckSearch;
    }

    public void setCheckSearch(boolean checkSearch) {
        CheckSearch = checkSearch;
    }

    public ArrayList<MySongObject> getListSong() {
        return listSong;
    }

    public void setListSong(ArrayList<MySongObject> listSong) {
        this.listSong = listSong;
    }

    public ArrayList<MyAlbumObject> getListAlbum() {
        return listAlbum;
    }

    public void setListAlbum(ArrayList<MyAlbumObject> listAlbum) {
        this.listAlbum = listAlbum;
    }

    public ArrayList<MyArtistObject> getListArtist() {
        return listArtist;
    }

    public void setListArtist(ArrayList<MyArtistObject> listArtist) {
        this.listArtist = listArtist;
    }
}
