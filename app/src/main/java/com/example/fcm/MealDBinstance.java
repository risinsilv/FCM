package com.example.fcm;

import android.content.Context;
import androidx.room.Room;

public class MealDBinstance {
    private static  MealDataBase database;

    public static MealDataBase getDataBase1(Context context){
        if (database == null) {
            database = Room.databaseBuilder(context,
                            MealDataBase.class, "app_database1")
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }

}
