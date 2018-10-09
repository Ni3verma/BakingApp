package com.importio.nitin.bakers.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ReceipeDao {
    @Query("select * from Receipe")
    List<ReceipeEntry> getAllReceipe();

    @Insert
    void insertReceipe(ReceipeEntry receipe);
}
