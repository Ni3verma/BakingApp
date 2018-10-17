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

    @Query("select id,shortDesc from step where receipeId= :recId")
    List<ShortStepEntry> getStepsOfReceipe(int recId);

    @Query("select * from step where id=:id")
    StepEntry getStepById(int id);

    @Query("select receipeId from step where id=:id")
    int getReceipeId(int id);

    @Query("select id from step where receipeId=:id")
    List<Integer> getRangeOfIds(int id);
}
