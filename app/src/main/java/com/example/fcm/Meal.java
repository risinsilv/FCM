package com.example.fcm;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Meal" ,primaryKeys = {"date","meal_name"})
public class Meal {
    @NonNull
    @ColumnInfo(name = "date")
    private String date;
    @NonNull
    @ColumnInfo(name = "meal_name")
    private String mealName;

    @ColumnInfo(name = "portion_size")
    private double portionSize;

    @ColumnInfo(name = "meal_type")
    private String mealType;

    @ColumnInfo(name = "calories")
    private double calories;

    @ColumnInfo(name = "fats")
    private double fats;

    @ColumnInfo(name = "proteins")
    private double proteins;

    @ColumnInfo(name = "carbohydrates")
    private double carbohydrates;

    @ColumnInfo(name = "image")
    private String image;

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public double getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(double portionSize) {
        this.portionSize = portionSize;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

}
