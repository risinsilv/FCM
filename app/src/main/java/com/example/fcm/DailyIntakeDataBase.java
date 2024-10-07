package com.example.fcm;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DailyIntake.class}, version = 1)
public abstract class DailyIntakeDataBase extends RoomDatabase{
    public abstract DailyIntakeDAO dailyIntakeDAO();
}
