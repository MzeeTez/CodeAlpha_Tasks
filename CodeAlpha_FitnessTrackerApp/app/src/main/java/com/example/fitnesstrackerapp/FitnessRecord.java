package com.example.fitnesstrackerapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fitness_records")
public class FitnessRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String date; // e.g., "YYYY-MM-DD"
    public String exerciseType;
    public int steps;
    public int workoutDurationMinutes;
    public int caloriesBurned;
}