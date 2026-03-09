package com.example.fitnesstrackerapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FitnessRecord.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FitnessDao fitnessDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "fitness_database")
                    .allowMainThreadQueries() // Note: For production, run queries on a background thread
                    .build();
        }
        return INSTANCE;
    }
}