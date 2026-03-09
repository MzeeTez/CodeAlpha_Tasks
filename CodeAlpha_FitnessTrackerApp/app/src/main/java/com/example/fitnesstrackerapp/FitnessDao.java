package com.example.fitnesstrackerapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FitnessDao {
    @Insert
    void insert(FitnessRecord record);

    @Query("SELECT * FROM fitness_records WHERE date = :todayDate")
    List<FitnessRecord> getRecordsForDate(String todayDate);

    // FIX: Add COALESCE to prevent NullPointerException when the database is empty
    @Query("SELECT COALESCE(SUM(caloriesBurned), 0) FROM fitness_records WHERE date = :todayDate")
    int getTotalCaloriesForDate(String todayDate);

    @Query("SELECT COALESCE(SUM(steps), 0) FROM fitness_records WHERE date = :todayDate")
    int getTotalStepsForDate(String todayDate);
}