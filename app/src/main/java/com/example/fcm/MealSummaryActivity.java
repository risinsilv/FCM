package com.example.fcm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class MealSummaryActivity extends AppCompatActivity {

    private ImageView mealImageView;
    private TextView mealNameTextView, mealTypeTextView, mealDateTextView, mealWeightTextView;
    private Button editButton;
    private String mealName, mealType, mealDate;
    double calories, fats, proteins, carbohydrates, weight;
    private Bitmap mealImageBitmap;
    ImageStorage imageStorage = ImageStorage.getInstance();
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_summary);
        EdgeToEdge.enable(this);

        mealImageView = findViewById(R.id.imageView2);
        mealNameTextView = findViewById(R.id.mealNameTextView);
        mealTypeTextView = findViewById(R.id.mealTypeTextView);
        mealDateTextView = findViewById(R.id.mealDateTextView);
        mealWeightTextView = findViewById(R.id.textviewWeight);
        editButton = findViewById(R.id.editbutton);
        backButton = findViewById(R.id.backButton4);


        // Retrieve data from Intent
        Intent intent = getIntent();
        mealName = intent.getStringExtra("mealName");
        mealDate = intent.getStringExtra("mealDate");
        MealDAO mealDAO = MealDBinstance.getDataBase1(getApplicationContext()).mealDAO();
        Meal meal = mealDAO.getTheMeal(mealDate, mealName);

        mealImageBitmap = imageStorage.getImage(meal.getImage());
        weight = meal.getPortionSize();
        mealType = meal.getMealType();
        calories = meal.getCalories();
        fats = meal.getFats();
        proteins = meal.getProteins();
        carbohydrates = meal.getCarbohydrates();


        TextView calorieTextView = findViewById(R.id.calorieTextView);
        TextView fatTextView = findViewById(R.id.fatTextView);
        TextView carbsTextView = findViewById(R.id.carbsTextView);
        TextView proteinTextView = findViewById(R.id.proteinTextView);


        calorieTextView.setText("Calories: " + calories + " kcal");
        fatTextView.setText("Fat: " + fats + " g");
        carbsTextView.setText("Carbs: " + carbohydrates + " g");
        proteinTextView.setText("Protein: " + proteins + " g");


        if (mealImageBitmap != null) {
            mealImageView.setImageBitmap(mealImageBitmap);
        }

        // Set the data to views
        mealNameTextView.setText(mealName);
        mealTypeTextView.setText(mealType);
        mealDateTextView.setText(mealDate);
        mealWeightTextView.setText(weight + "g");


        // Handle the Edit button
        editButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(MealSummaryActivity.this, EditMeal.class);
            editIntent.putExtra("mealName", mealName);
            editIntent.putExtra("mealDate", mealDate);
            startActivity(editIntent);
        });

        backButton.setOnClickListener(v -> {
            Intent Intent = new Intent(MealSummaryActivity.this, recycleView.class);
            startActivity(Intent);
        });



    }
}

