package com.example.jingmb3.model.online;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;

import java.util.List;

public class MediaOnline {
    public static MediaOnline Instance;
    private Bitmap urlImage;
    private MediaPlayer mediaPlayer;
    private int position = 0;
    private boolean checkStop = false;
    private boolean checkRepeat = false;
    private boolean checkRand = false;
    private boolean checkSongAlbum = false;
    private boolean checkSongArtist = false;
    private String nameAlbum;
    private String nameArtist;

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public boolean isCheckSongAlbum() {
        return checkSongAlbum;
    }

    public void setCheckSongAlbum(boolean checkSongAlbum) {
        this.checkSongAlbum = checkSongAlbum;
    }

    public boolean isCheckSongArtist() {
        return checkSongArtist;
    }

    public void setCheckSongArtist(boolean checkSongArtist) {
        this.checkSongArtist = checkSongArtist;
    }

    public List<GetSongs> getGetSongsList() {
        return getSongsList;
    }

    public boolean isCheckRepeat() {
        return checkRepeat;
    }

    public Bitmap getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(Bitmap urlImage) {
        this.urlImage = urlImage;
    }

    public void setCheckRepeat(boolean checkRepeat) {
        this.checkRepeat = checkRepeat;
    }

    public boolean isCheckRand() {
        return checkRand;
    }

    public void setCheckRand(boolean checkRand) {
        this.checkRand = checkRand;
    }

    public boolean isCheckStop() {
        return checkStop;
    }

    public void setCheckStop(boolean checkStop) {
        this.checkStop = checkStop;
    }

    public void setGetSongsList(List<GetSongs> getSongsList) {
        this.getSongsList = getSongsList;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private List<GetSongs> getSongsList;
    private List<GetSongs> getSongAlbumList;
    private List<GetSongs> getSongArtistList;
    private List<Upload> albumList;
    private List<ArtistOnline> artistOnlineList;

    public List<Upload> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<Upload> albumList) {
        this.albumList = albumList;
    }

    public List<ArtistOnline> getArtistOnlineList() {
        return artistOnlineList;
    }

    public void setArtistOnlineList(List<ArtistOnline> artistOnlineList) {
        this.artistOnlineList = artistOnlineList;
    }

    public List<GetSongs> getGetSongAlbumList() {
        return getSongAlbumList;
    }

    public void setGetSongAlbumList(List<GetSongs> getSongAlbumList) {
        this.getSongAlbumList = getSongAlbumList;
    }

    public List<GetSongs> getGetSongArtistList() {
        return getSongArtistList;
    }

    public void setGetSongArtistList(List<GetSongs> getSongArtistList) {
        this.getSongArtistList = getSongArtistList;
    }

    public static MediaOnline getInstance() {
        if(Instance==null){
            return Instance = new MediaOnline();
        }
        return Instance;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void playSongOnline(Context context, String songUrl, int position){
        mediaPlayer=MediaPlayer.create(context, Uri.parse(songUrl));
        this.position=position;
    }
    public void startPlaySong(){
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                 mediaPlayer.start();
            }
        });
    }
    public void pauseSong(){
        if(mediaPlayer!=null){
            mediaPlayer.pause();
        }
    }
    public void stopPlaySong(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            checkStop = true;
        }
    }
    public boolean checkPlaying(){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()) return true;
        else return false;
    }
}
