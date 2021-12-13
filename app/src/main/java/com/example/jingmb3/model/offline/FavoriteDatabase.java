package com.example.jingmb3.model.offline;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteObject.class},version = 1,exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {
    private static final String database_name="FavoriteSong.db";
    private static FavoriteDatabase instance;
    public static synchronized FavoriteDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),FavoriteDatabase.class,database_name)
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract FavoriteDAO favoriteDAO();
}
