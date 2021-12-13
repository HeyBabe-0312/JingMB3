package com.example.jingmb3.model.offline;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface FavoriteDAO {
    @Insert
    void insertSong(FavoriteObject favoriteObject);

    @Query("SELECT * FROM FavoriteSongs")
    List<FavoriteObject> getListFavSong();

    @Update
    void editSong(FavoriteObject favoriteObject);

    @Delete
    void deleteSong(FavoriteObject favoriteObject);

    @Query("DELETE FROM FavoriteSongs")
    void deleteAllSong();

    @Query("SELECT * FROM FavoriteSongs WHERE `ID bài hát`=:id_song")
    FavoriteObject getMyFavSongByID(int id_song);

    @Query("SELECT `ID bài hát` FROM FavoriteSongs")
    List<Integer> getListIdSong();
}
