package com.example.jingmb3.model.offline;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MySongsDAO {
    @Insert
    void insertSong(MySongObject mySongObject);

    @Query("SELECT * FROM MySongs")
    List<MySongObject> getListSong();

    @Query("SELECT `Nghệ sĩ` FROM MySongs")
    List<String> getListArtist();


    @Update
    void editSong(MySongObject mySongObject);

    @Delete
    void deleteSong(MySongObject mySongObject);

    @Query("DELETE FROM MySongs")
    void deleteAllSong();

    @Query("SELECT * FROM mysongs WHERE id_song=:id_song")
    MySongObject getMySongByID(int id_song);

    @Query("SELECT * FROM mysongs WHERE `Nghệ sĩ`=:nameArtist")
    List<MySongObject> getListSongByArtist(String nameArtist);

}
