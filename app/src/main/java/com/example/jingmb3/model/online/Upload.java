package com.example.jingmb3.model.online;

public class Upload {
    String name;
    String url;
    String songsCategory;

    public Upload(String name, String songCategory, String url) {
        this.name = name;
        this.url = url;
        this.songsCategory = songCategory;
    }
    public Upload(){}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSongsCategory() {
        return songsCategory;
    }

    public void setSongsCategory(String songsCategory) {
        this.songsCategory = songsCategory;
    }
}
