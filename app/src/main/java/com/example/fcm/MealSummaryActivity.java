package com.example.fcm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MealSummaryActivity extends AppCompatActivity {

    private ImageView mealImageView;
    private TextView mealNameTextView, mealTypeTextView, mealDateTextView;
    private Button editButton, nutritionButton;

    private String mealName, mealType, mealDate;
    double calories,fats,proteins,carbohydrates;
    private Bitmap mealImageBitmap;
    private Uri mealImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_summary);

        mealImageView = findViewById(R.id.mealImageView);
        mealNameTextView = findViewById(R.id.mealNameTextView);
        mealTypeTextView = findViewById(R.id.mealTypeTextView);
        mealDateTextView = findViewById(R.id.mealDateTextView);
        editButton = findViewById(R.id.editButton);
        nutritionButton = findViewById(R.id.nutritionButton);

        // Retrieve data from Intent
        Intent intent = getIntent();
        mealName = intent.getStringExtra("mealName");
        mealDate = intent.getStringExtra("mealDate");
        MealDAO mealDAO = MealDBinstance.getDataBase1(getApplicationContext()).mealDAO();
        Meal meal = mealDAO.getTheMeal(mealDate,mealName);

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

        String mealImageUriString = intent.getStringExtra("mealImageUri");
        if (mealImageUriString != null) {
            mealImageUri = Uri.parse(mealImageUriString);
            mealImageView.setImageURI(mealImageUri);
        } else {
            mealImageBitmap = intent.getParcelableExtra("mealImageBitmap");
            mealImageView.setImageBitmap(mealImageBitmap);
        }

        // Set the data to views
        mealNameTextView.setText(mealName);
        mealTypeTextView.setText(mealType);
        mealDateTextView.setText(mealDate);

        // Handle the Edit button
     //  editButton.setOnClickListener(v -> {
     //      Intent editIntent = new Intent(MealSummaryActivity.this, MealDetailActivity.class);
     //      editIntent.putExtra("mealName", mealName);
     //      editIntent.putExtra("mealType", mealType);
     //      editIntent.putExtra("mealDate", mealDate);
     //      //editIntent.putExtra("mealImageUri", mealImageUri != null ? mealImageUri.toString() : null);
     //      startActivity(editIntent);
     //  });

        // Handle the Nutrition Info button
  //     nutritionButton.setOnClickListener(v -> {
  //         Intent nutritionIntent = new Intent(MealSummaryActivity.this, NutritionActivity.class);
  //         nutritionIntent.putExtra("mealName", mealName);
  //         startActivity(nutritionIntent);
  //     });
   }
}

