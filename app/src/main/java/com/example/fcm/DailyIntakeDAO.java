package com.example.fcm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface DailyIntakeDAO {
    @Insert
    void insert(DailyIntake... dailyIntake);

    @Update
    void update(DailyIntake... dailyIntake);

    @Delete
    void delete(DailyIntake... dailyIntake);

    @Query("SELECT * FROM daily_intake")
    List<DailyIntake> getAlldates();
}
