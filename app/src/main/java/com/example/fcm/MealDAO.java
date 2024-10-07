package com.example.fcm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface MealDAO {

    @Insert
    void insert(Meal... meals);

    @Update
    void update(Meal... meals);

    @Delete
    void delete(Meal... meals);

    @Query("SELECT * FROM Meal WHERE date = :date")
    List<Meal> getMealsByDate(String date);


}
