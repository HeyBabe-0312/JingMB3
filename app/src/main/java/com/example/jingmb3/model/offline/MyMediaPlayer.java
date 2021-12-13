package com.example.jingmb3.model.offline;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MyMediaPlayer {
    public static MyMediaPlayer Instance;
    MediaPlayer mediaPlayer;
    ArrayList<MySongObject> ListPlaySong=new ArrayList<>();
    int position=0;
    int IdAlbum;
    int IdArtist;
    int IdSong=-1;
    boolean PlayAlbum=false;
    boolean checkFavSong=false;
    boolean checkSongArtist=false;
    boolean checkStopMedia =false;
    boolean checkRandom=false;
    boolean checkRepeat=false;
    boolean checkSongAlbum=false;
    boolean checkUpdateSong=false;
    boolean checkUpdateAlbum=false;
    boolean checkUpdateArtist=false;
    boolean checkUpdateFavorite=false;
    boolean checkStopFavSong=false;

    public static MyMediaPlayer getInstance(){
        if(Instance==null){
            return Instance=new MyMediaPlayer();
        }
        return Instance;
    }
    public void playAudioFile(Context context, String uri, int position){
        mediaPlayer=MediaPlayer.create(context,Uri.parse(uri));
        this.position=position;
        this.IdSong=ListPlaySong.get(position).getId_song();
    }

    public void Start(){
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }


    public void stopAudioFile(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            checkStopMedia=true;
        }
    }
    public void pauseAudioFile(){
        if(mediaPlayer!=null){
            mediaPlayer.pause();
        }
    }
    public boolean chechMedia(){
        if(mediaPlayer!=null && mediaPlayer.isPlaying())
            return true;
        else return false;
    }
    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }
    public void setStopMedia(){
        this.checkStopMedia=false;
    }
    public boolean isCheckStopMedia(){
        return checkStopMedia;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition(){
        return position;
    }

    public int getIdSong() {
        return IdSong;
    }

    public boolean isCheckRandom() {
        return checkRandom;
    }

    public void setCheckRandom(boolean checkRandom) {
        this.checkRandom = checkRandom;
    }

    public boolean isCheckRepeat() {
        return checkRepeat;
    }

    public void setCheckRepeat(boolean checkRepeat) {
        this.checkRepeat = checkRepeat;
    }

    public boolean isCheckSongAlbum() {
        return checkSongAlbum;
    }

    public void setCheckSongAlbum(boolean checkSongAlbum) {
        this.checkSongAlbum = checkSongAlbum;
    }

    public int getIdAlbum() {
        return IdAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        IdAlbum = idAlbum;
    }

    public boolean isPlayAlbum() {
        return PlayAlbum;
    }

    public void setPlayAlbum(boolean playAlbum) {
        PlayAlbum = playAlbum;
    }

    public ArrayList<MySongObject> getListPlaySong() {
        return ListPlaySong;
    }

    public void setListPlaySong(ArrayList<MySongObject> listPlaySong) {
        ListPlaySong = listPlaySong;
    }

    public int getIdArtist() {
        return IdArtist;
    }

    public void setIdArtist(int idArtist) {
        IdArtist = idArtist;
    }

    public boolean isCheckSongArtist() {
        return checkSongArtist;
    }

    public void setCheckSongArtist(boolean checkSongArtist) {
        this.checkSongArtist = checkSongArtist;
    }

    public boolean isCheckFavSong() {
        return checkFavSong;
    }

    public void setCheckFavSong(boolean checkFavSong) {
        this.checkFavSong = checkFavSong;
    }

    public boolean isCheckUpdateAlbum() {
        return checkUpdateAlbum;
    }

    public void setCheckUpdateAlbum(boolean checkUpdateAlbum) {
        this.checkUpdateAlbum = checkUpdateAlbum;
    }

    public boolean isCheckUpdateSong() {
        return checkUpdateSong;
    }

    public void setCheckUpdateSong(boolean checkUpdateSong) {
        this.checkUpdateSong = checkUpdateSong;
    }

    public boolean isCheckUpdateArtist() {
        return checkUpdateArtist;
    }

    public void setCheckUpdateArtist(boolean checkUpdateArtist) {
        this.checkUpdateArtist = checkUpdateArtist;
    }

    public boolean isCheckUpdateFavorite() {
        return checkUpdateFavorite;
    }

    public void setCheckUpdateFavorite(boolean checkUpdateFavorite) {
        this.checkUpdateFavorite = checkUpdateFavorite;
    }

    public boolean isCheckStopFavSong() {
        return checkStopFavSong;
    }

    public void setCheckStopFavSong(boolean checkStopFavSong) {
        this.checkStopFavSong = checkStopFavSong;
    }
}
