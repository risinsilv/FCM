package com.example.fcm;

import android.content.Context;

import androidx.room.Room;

public class DailyIntakeDBinstance {
    private  static DailyIntakeDataBase database;

    public static DailyIntakeDataBase getDataBase(Context context){
        if (database == null) {
            database = Room.databaseBuilder(context,
                            DailyIntakeDataBase.class, "app_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }

}
