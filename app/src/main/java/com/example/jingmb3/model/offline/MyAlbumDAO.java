package com.example.jingmb3.model.offline;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface MyAlbumDAO {
    @Insert
    void insertAlbum(MyAlbumObject myAlbumObject);

    @Query("SELECT * FROM MyAlbum")
    List<MyAlbumObject> getMyAlbum();

    @Update
    void editAlbum(MyAlbumObject myAlbumObject);

    @Delete
    void deleteAlbum(MyAlbumObject myAlbumObject);

    @Query("DELETE FROM MyAlbum")
    void deleteAllAlbum();

    @Query("SELECT * FROM MyAlbum WHERE id_album= :albumId")
    MyAlbumObject getAlbumById(int albumId);
}
