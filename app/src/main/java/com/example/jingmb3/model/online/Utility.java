package com.example.jingmb3.model.online;

public class Utility {
    public static String convertDuration(long duration){
        long minutes = (duration/1000)/60;
        long seconds = (duration/1000)%60;
        String converted = String.format("%d:%02d",minutes,seconds);
        return converted;
    }
}
