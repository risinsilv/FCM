package com.example.fcm;

public class MealData {
    private String mealName;
    private int calories;
    private String ingredients;

    public MealData(String mealName, int calories, String ingredients) {
        this.mealName = mealName;
        this.calories = calories;
        this.ingredients = ingredients;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}

