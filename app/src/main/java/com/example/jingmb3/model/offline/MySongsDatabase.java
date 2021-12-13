package com.example.jingmb3.model.offline;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {MySongObject.class},version = 2,exportSchema = false)
@TypeConverters({MySongObject.IdAlbumConverters.class})
public abstract class MySongsDatabase extends RoomDatabase {
    private static final String database_name="song.db";
    private static MySongsDatabase instance;
    public static synchronized MySongsDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),MySongsDatabase.class,database_name)
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract MySongsDAO mySongsDAO();
}