package com.importio.nitin.bakers.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StepDao {
    @Query("select * from step")
    List<StepEntry> getAllSteps();

    @Insert
    void insertStep(StepEntry step);
}
