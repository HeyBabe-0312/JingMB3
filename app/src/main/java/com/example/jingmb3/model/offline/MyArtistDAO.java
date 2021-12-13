package com.example.jingmb3.model.offline;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface MyArtistDAO {
    @Insert
    void insertArtist(MyArtistObject myArtistObject);

    @Query("SELECT * FROM MyArtist")
    List<MyArtistObject> getMyArtist();

    @Query("SELECT `Tên nghệ sĩ` FROM MyArtist")
    List<String> getListNameArtist();

    @Update
    void editArtist(MyArtistObject myArtistObject);

    @Delete
    void deleteArtist(MyArtistObject myArtistObject);

    @Query("DELETE FROM MyArtist")
    void deleteAllArtist();

    @Query("SELECT * FROM MyArtist WHERE id_artist= :artistId")
    MyArtistObject getArtistById(int artistId);

    @Query("SELECT * FROM MyArtist WHERE `Tên nghệ sĩ`= :artistName")
    MyArtistObject getArtistByName(String artistName);
}
