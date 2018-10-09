package com.importio.nitin.bakers.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {
    @Query("select * from ingredient")
    List<IngredientEntry> getAllIngredients();

    @Insert
    void insertIngredient(IngredientEntry entry);
}
