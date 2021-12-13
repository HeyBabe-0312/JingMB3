package com.example.jingmb3.model.offline;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {MyAlbumObject.class},version = 1)
@TypeConverters({MyAlbumObject.IdSongConverters.class})
public abstract class MyAlbumDatabase extends RoomDatabase {
    private static final String database_name="album.db";
    private static MyAlbumDatabase instance;
    public static synchronized MyAlbumDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),MyAlbumDatabase.class,database_name)
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract MyAlbumDAO myAlbumDAO();
}
