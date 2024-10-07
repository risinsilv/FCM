package com.example.fcm;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "daily_intake")
public class DailyIntake {
    @NonNull
    @PrimaryKey
    private String date;

    @ColumnInfo(name = "target_intake")
    private double tergatIntake;

    @ColumnInfo(name = "total_intake")
    private double totalIntake;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTergatIntake() {
        return tergatIntake;
    }

    public void setTergatIntake(double tergatIntake) {
        this.tergatIntake = tergatIntake;
    }

    public double getTotalIntake() {
        return totalIntake;
    }

    public void setTotalIntake(double totalIntake) {
        this.totalIntake = totalIntake;
    }
}
