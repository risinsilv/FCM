package com.example.fcm;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NutritionActivity extends AppCompatActivity {

    // Declare UI components
    private TextView proteinLabel, proteinValue, proteinGoal;
    private TextView carbLabel, carbValue, carbGoal;
    private TextView fatLabel, fatValue, fatGoal;
    private ProgressBar proteinProgress, carbProgress, fatProgress;

    private TextView caloriesTextView;
    private ProgressBar calorieProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Nutrition Information");
        }


        proteinLabel = findViewById(R.id.protein_label);
        proteinValue = findViewById(R.id.protein_value);
        proteinGoal = findViewById(R.id.protein_goal);
        proteinProgress = findViewById(R.id.protein_progress);

        carbLabel = findViewById(R.id.carb_label);
        carbValue = findViewById(R.id.carb_value);
        carbGoal = findViewById(R.id.carb_goal);
        carbProgress = findViewById(R.id.carb_progress);

        fatLabel = findViewById(R.id.fat_label);
        fatValue = findViewById(R.id.fat_value);
        fatGoal = findViewById(R.id.fat_goal);
        fatProgress = findViewById(R.id.fat_progress);

        caloriesTextView = findViewById(R.id.caloriesTextView);
        calorieProgressBar = findViewById(R.id.calorieProgressBar);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int protein = extras.getInt("protein", 0); // default to 0 if not provided
            int proteinMax = extras.getInt("proteinGoal", 60);

            int carbs = extras.getInt("carbs", 0);
            int carbsMax = extras.getInt("carbsGoal", 225);

            int fat = extras.getInt("fat", 0);
            int fatMax = extras.getInt("fatGoal", 77);

            int calories = (protein * 4) + (carbs * 4) + (fat * 9);
            int calorieGoal = 2000; // Example calorie goal

            updateNutritionValues(protein, proteinMax, carbs, carbsMax, fat, fatMax, calories, calorieGoal);
        }

        findViewById(R.id.back_button).setOnClickListener(v -> {
            finish();
        });
    }

    private void updateNutritionValues(int protein, int proteinGoalValue, int carbs, int carbsGoalValue, int fat, int fatGoalValue, int calories, int calorieGoal) {
        proteinValue.setText(protein + "g");
        proteinGoal.setText("/ " + proteinGoalValue + "g");

        carbValue.setText(carbs + "g");
        carbGoal.setText("/ " + carbsGoalValue + "g");

        fatValue.setText(fat + "g");
        fatGoal.setText("/ " + fatGoalValue + "g");

        // Calculate progress percentages and set them to the ProgressBars
        int proteinProgressValue = (int) ((float) protein / proteinGoalValue * 100);
        int carbsProgressValue = (int) ((float) carbs / carbsGoalValue * 100);
        int fatProgressValue = (int) ((float) fat / fatGoalValue * 100);

        proteinProgress.setProgress(proteinProgressValue);
        carbProgress.setProgress(carbsProgressValue);
        fatProgress.setProgress(fatProgressValue);

        // Update calorie progress
        calorieProgressBar.setMax(calorieGoal);
        calorieProgressBar.setProgress(calories);
        caloriesTextView.setText(calories + " / " + calorieGoal + " Calories");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
