package com.example.fcm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class recycleView extends AppCompatActivity {
    ArrayList<recycleViewData> recycleList;
    ArrayList<recycleViewDateData> recycleListDate;
    RecyclerView recyclerView;
    RecyclerView recyclerViewDate;
    recycleViewAdapter recycleViewAdapter;
    //HashMap<recycleViewDateData, MealData> mealDataMap;
    private Map<recycleViewDateData, ArrayList<MealData>> mealDataMap = new HashMap<>();
    private recycleViewDateData selectedDateData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recycle_view);

        mealDataMap = new HashMap<>();

        recyclerViewDate = findViewById(R.id.recyclerViewDate);
        recyclerViewDate.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDate.setLayoutManager(layoutManager);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        recycleListDate = new ArrayList<recycleViewDateData>();
        recycleListDate.add(new recycleViewDateData(2024,"Oct",2));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",3));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",4));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",5));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",6));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",7));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",8));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",9));

        recycleViewDateAdapter dateAdapter = new recycleViewDateAdapter(recycleListDate, this, new recycleViewDateAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(recycleViewDateData dateData) {
                selectedDateData = dateData;
                updateMainRecyclerViewData(dateData);
            }
        });
        recyclerViewDate.setAdapter(dateAdapter);

        recycleList = new ArrayList<>();
        recycleViewAdapter = new recycleViewAdapter(recycleList, this, new recycleViewAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(recycleViewData mealItem, int position) {
                showMealDetailsDialog(selectedDateData, position);
            }
        });
        recyclerView.setAdapter(recycleViewAdapter);


        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            if (selectedDateData != null) {
                showMealInputDialog(selectedDateData);
            } else {
                Toast.makeText(this, "Please select a date first", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateMainRecyclerViewData(recycleViewDateData dateData) {
        recycleList.clear();

        if (mealDataMap.containsKey(dateData)) {
            ArrayList<MealData> mealsForDate = mealDataMap.get(dateData);

            for (MealData mealData : mealsForDate) {
                recycleList.add(new recycleViewData(R.drawable.test2, mealData.getMealName()));
            }
        } else {
            recycleList.add(new recycleViewData(R.drawable.test2, "No meal data for this date."));
        }

        recycleViewAdapter.notifyDataSetChanged();
    }

    private void showMealInputDialog(recycleViewDateData dateData) {
        // Create a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_meal, null);
        builder.setView(dialogView);

        // Get the UI elements from the dialog
        EditText editTextMealName = dialogView.findViewById(R.id.editTextMealName);
        EditText editTextCalories = dialogView.findViewById(R.id.editTextCalories);
        EditText editTextIngredients = dialogView.findViewById(R.id.editTextIngredients);
        Button buttonSaveMeal = dialogView.findViewById(R.id.buttonSaveMeal);

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Save button click listener
        buttonSaveMeal.setOnClickListener(v -> {
            String mealName = editTextMealName.getText().toString();
            int calories = Integer.parseInt(editTextCalories.getText().toString());
            String ingredients = editTextIngredients.getText().toString();

            // Create a new meal data object
            MealData mealData = new MealData(mealName, calories, ingredients);

            // Save this data to the selected date
            saveMealDataForDate(dateData, mealData);

            // Close the dialog
            dialog.dismiss();
        });
    }

    private void saveMealDataForDate(recycleViewDateData dateData, MealData mealData) {
        // Retrieve the meal list for the date or create a new one if it doesn't exist
        ArrayList<MealData> mealsForDate = mealDataMap.getOrDefault(dateData, new ArrayList<>());
        mealsForDate.add(mealData);
        mealDataMap.put(dateData, mealsForDate);
        updateMainRecyclerViewData(dateData);
    }

    private void showMealDetailsDialog(recycleViewDateData dateData, int position) {
        // Retrieve the meal list for the date
        ArrayList<MealData> mealsForDate = mealDataMap.get(dateData);

        // Ensure the list isn't null and the position is valid
        if (mealsForDate != null && position < mealsForDate.size()) {
            MealData mealData = mealsForDate.get(position);

            // Show meal details in an alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Meal Details");
            builder.setMessage("Meal: " + mealData.getMealName() + "\n" +
                    "Calories: " + mealData.getCalories() + "\n" +
                    "Ingredients: " + mealData.getIngredients());
            builder.setPositiveButton("OK", null);
            builder.show();
        } else {
            Toast.makeText(this, "No meal data found.", Toast.LENGTH_SHORT).show();
        }
    }

}
