package com.example.fcm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.List;

public class Nutrition extends AppCompatActivity {
    DailyIntakeDAO dailyIntakeDAO;
    MealDAO mealDAO;
    String date;
    Double goalIntake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nutrition);
        ProgressBar calorieProgressBar = findViewById(R.id.calorieProgressBar);
        TextView caloriesTextView = findViewById(R.id.caloriesTextView);
        TextView caloriesGoalTextView = findViewById(R.id.caloriesGoalTextView);
        Button editButton = findViewById(R.id.editButton);

        dailyIntakeDAO = DailyIntakeDBinstance.getDataBase(getApplicationContext()).dailyIntakeDAO();
        mealDAO = MealDBinstance.getDataBase1(getApplicationContext()).mealDAO();

         date = getIntent().getStringExtra("selectedDate");
        goalIntake = getIntent().getDoubleExtra("goalIntake",0);
        double totalCalories;

        List<Meal> meals = mealDAO.getMealsByDate(date);
        totalCalories = meals.stream().mapToDouble(Meal::getCalories).sum();

        int progress = (int) ((totalCalories * 100)/goalIntake);

        calorieProgressBar.setProgress(progress);
        caloriesTextView.setText(String.valueOf(totalCalories));
        caloriesGoalTextView.setText(String.valueOf(goalIntake));



        editButton.setOnClickListener(v -> showEditGoalDialog(goalIntake, caloriesGoalTextView, calorieProgressBar, totalCalories));


    }

    private void showEditGoalDialog(double currentGoal, TextView goalTextView, ProgressBar progressBar, double totalCalories) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate a custom layout with an EditText for user input
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_calorie_goal, null);

        final EditText inputGoal = dialogView.findViewById(R.id.calorie_goal_input);

        // Set the current goal value in the EditText
        inputGoal.setText(String.valueOf(currentGoal));

        builder.setView(dialogView)
                .setTitle("Edit Daily Calorie Goal")
                .setMessage("Enter your new daily calorie goal")
                .setPositiveButton("OK", (dialog, which) -> {
                    String newGoalStr = inputGoal.getText().toString();
                    if (!newGoalStr.isEmpty() && newGoalStr.matches("\\d+")) {
                        double newGoalIntake = Double.parseDouble(newGoalStr);
                        DailyIntake dailyIntake2 = new DailyIntake();
                        dailyIntake2.setDate(date);
                        dailyIntake2.setTergatIntake(newGoalIntake);
                        dailyIntakeDAO.update(dailyIntake2);


                        // Update the goal in the TextView and ProgressBar
                        goalTextView.setText(String.valueOf(newGoalIntake));

                        // Recalculate the progress and update the ProgressBar
                        int newProgress = (int) ((totalCalories * 100) / newGoalIntake);
                        progressBar.setProgress(newProgress);
                    } else {
                        Toast.makeText(Nutrition.this, "Invalid input. Please enter a number.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}