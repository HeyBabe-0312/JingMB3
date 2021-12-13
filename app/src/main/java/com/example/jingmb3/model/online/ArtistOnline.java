package com.example.jingmb3.model.online;

public class ArtistOnline {
    String url;
    String artist;

    public ArtistOnline(){}
    public ArtistOnline(String url, String artist) {
        this.url = url;
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
