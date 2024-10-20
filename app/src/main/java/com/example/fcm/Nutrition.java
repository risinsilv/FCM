package com.example.fcm;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Nutrition extends AppCompatActivity {
    DailyIntakeDAO dailyIntakeDAO;
    MealDAO mealDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nutrition);

        dailyIntakeDAO = DailyIntakeDBinstance.getDataBase(getApplicationContext()).dailyIntakeDAO();
        mealDAO = MealDBinstance.getDataBase1(getApplicationContext()).mealDAO();

        String date = getIntent().getStringExtra("selectedDate");
        Double goalIntake = getIntent().getDoubleExtra("goalIntake",0);



    }
}