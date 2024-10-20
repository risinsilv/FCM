package com.example.fcm;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.List;

public class Nutrition extends AppCompatActivity {
    DailyIntakeDAO dailyIntakeDAO;
    MealDAO mealDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nutrition);
        ProgressBar calorieProgressBar = findViewById(R.id.calorieProgressBar);
        TextView caloriesTextView = findViewById(R.id.caloriesTextView);
        TextView caloriesGoalTextView = findViewById(R.id.caloriesGoalTextView);

        dailyIntakeDAO = DailyIntakeDBinstance.getDataBase(getApplicationContext()).dailyIntakeDAO();
        mealDAO = MealDBinstance.getDataBase1(getApplicationContext()).mealDAO();

        String date = getIntent().getStringExtra("selectedDate");
        Double goalIntake = getIntent().getDoubleExtra("goalIntake",0);
        double totalCalories = 0.0;

        List<Meal> meals = mealDAO.getMealsByDate(date);
        for (Meal meal : meals) {
            totalCalories += meal.getCalories();
        }

        int progress = (int) ((totalCalories * 100)/goalIntake);

        calorieProgressBar.setProgress(progress);
        caloriesTextView.setText(String.valueOf(totalCalories));
        caloriesGoalTextView.setText(String.valueOf(goalIntake));






    }
}