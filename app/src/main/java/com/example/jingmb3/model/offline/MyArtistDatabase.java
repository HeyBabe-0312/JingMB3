package com.example.jingmb3.model.offline;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {MyArtistObject.class},version = 1)
@TypeConverters({MyArtistObject.IdSongConverters.class})
public abstract class MyArtistDatabase extends RoomDatabase {
    private static final String database_name="artist.db";
    private static MyArtistDatabase instance;
    public static synchronized MyArtistDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),MyArtistDatabase.class,database_name)
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract MyArtistDAO myArtistDAO();
}
