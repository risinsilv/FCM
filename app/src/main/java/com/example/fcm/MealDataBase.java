package com.example.fcm;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Meal.class}, version = 1)
public abstract class MealDataBase extends RoomDatabase {
    public abstract MealDAO mealDAO();

}
