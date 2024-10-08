package com.example.fcm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class recycleView extends AppCompatActivity {
    ArrayList<Meal> recycleList;
    ArrayList<DailyIntake> recycleListDate;
    RecyclerView recyclerView;
    RecyclerView recyclerViewDate;
    recycleViewAdapter recycleViewAdapter;
    DailyIntakeDAO dailyIntakeDAO;
    MealDAO mealDAO;


    private DailyIntake selectedDateData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recycle_view);

        dailyIntakeDAO = DailyIntakeDBinstance.getDataBase(getApplicationContext()).dailyIntakeDAO();
        mealDAO = MealDBinstance.getDataBase1(getApplicationContext()).mealDAO();

        recyclerViewDate = findViewById(R.id.recyclerViewDate);
        recyclerViewDate.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDate.setLayoutManager(layoutManager);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        List<String> dates = DateUtils.getDatesOfMonth(month, year);

        // Populate the recycleListDate with DailyIntake objects
        recycleListDate = new ArrayList<>();
        for (String date : dates) {
            DailyIntake dailyIntake = new DailyIntake();
            dailyIntake.setDate(date);
            recycleListDate.add(dailyIntake);
        }

        // Set up the date adapter
        recycleViewDateAdapter dateAdapter = new recycleViewDateAdapter(recycleListDate, this, new recycleViewDateAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(DailyIntake dateData) {
                updateMainRecyclerViewData(dateData);
                selectedDateData = dateData; // Set selected date data
            }
        });


        recyclerViewDate.setAdapter(dateAdapter);

        List<Meal> temp = mealDAO.getMealsByDate("10-Sep-2024");
        recycleList = new ArrayList<>(temp);
        recycleViewAdapter = new recycleViewAdapter(recycleList, this, new recycleViewAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal mealItem, int position) {
                // You can add functionality to handle meal item clicks here if needed.
            }
        });
        recyclerView.setAdapter(recycleViewAdapter);


        //.setAdapter(dateAdapter);
        // Meal meal = new Meal();
        // meal.setDate("10-Sep-2024");
        // meal.setMealName("Empty");
        // meal.setImage(R.drawable.test2);
        // mealDAO.insert(meal);


        //recyclerView.setAdapter(recycleViewAdapter);
      // Meal meal = new Meal();
      // meal.setDate("13-Sep-2024");
      // meal.setMealName("Rice");
      // meal.setImage(R.drawable.test2);
      // mealDAO.insert(meal);
      // meal.setDate("13-Sep-2024");
      // meal.setMealName("Chicken");
      // meal.setImage(R.drawable.test2);
      // mealDAO.insert(meal);
      // meal.setDate("13-Sep-2024");
      // meal.setMealName("Pizza");
      // meal.setImage(R.drawable.test2);
      // mealDAO.insert(meal);


        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            if (selectedDateData != null) {
                // Launch MealDetailActivity and pass the selected date data
                Intent intent = new Intent(recycleView.this, MealDetailActivity.class);
                intent.putExtra("selectedDate", selectedDateData.getDate()); // Passing the selected date
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select a date first", Toast.LENGTH_SHORT).show();
            }
        });

        }

    private void updateMainRecyclerViewData(DailyIntake dateData) {
        List<Meal> meal = mealDAO.getMealsByDate(dateData.getDate());

        recycleList.clear();
        recycleList.addAll(new ArrayList<>(meal));
        recycleViewAdapter.notifyDataSetChanged();
    }

        // private void showMealInputDialog(recycleViewDateData dateData) {
        //     // Create a dialog
        //     AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //     LayoutInflater inflater = this.getLayoutInflater();
        //     View dialogView = inflater.inflate(R.layout.dialog_add_meal, null);
        //     builder.setView(dialogView);
        //
        //     // Get the UI elements from the dialog
        //     EditText editTextMealName = dialogView.findViewById(R.id.editTextMealName);
        //     EditText editTextCalories = dialogView.findViewById(R.id.editTextCalories);
        //     EditText editTextIngredients = dialogView.findViewById(R.id.editTextIngredients);
        //     Button buttonSaveMeal = dialogView.findViewById(R.id.buttonSaveMeal);
        //
        //     // Show the dialog
        //     AlertDialog dialog = builder.create();
        //     dialog.show();
        //
        //     // Save button click listener
        //     buttonSaveMeal.setOnClickListener(v -> {
        //         String mealName = editTextMealName.getText().toString();
        //         int calories = Integer.parseInt(editTextCalories.getText().toString());
        //         String ingredients = editTextIngredients.getText().toString();
        //
        //         // Create a new meal data object
        //         MealData mealData = new MealData(mealName, calories, ingredients);
        //
        //         // Save this data to the selected date
        //         saveMealDataForDate(dateData, mealData);
        //
        //         // Close the dialog
        //         dialog.dismiss();
        //     });
        // }

        // private void saveMealDataForDate(recycleViewDateData dateData, MealData mealData) {
        //     // Retrieve the meal list for the date or create a new one if it doesn't exist
        //     ArrayList<MealData> mealsForDate = mealDataMap.getOrDefault(dateData, new ArrayList<>());
        //     mealsForDate.add(mealData);
        //     mealDataMap.put(dateData, mealsForDate);
        //     updateMainRecyclerViewData(dateData);
        // }

        //private void showMealDetailsDialog(recycleViewDateData dateData, int position) {
        //     // Retrieve the meal list for the date
        //     ArrayList<MealData> mealsForDate = mealDataMap.get(dateData);
        //
        //     // Ensure the list isn't null and the position is valid
        //     if (mealsForDate != null && position < mealsForDate.size()) {
        //         MealData mealData = mealsForDate.get(position);
        //
        //         // Show meal details in an alert dialog
        //         AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //         builder.setTitle("Meal Details");
        //         builder.setMessage("Meal: " + mealData.getMealName() + "\n" +
        //                 "Calories: " + mealData.getCalories() + "\n" +
        //                 "Ingredients: " + mealData.getIngredients());
        //         builder.setPositiveButton("OK", null);
        //         builder.show();
        //     } else {
        //         Toast.makeText(this, "No meal data found.", Toast.LENGTH_SHORT).show();
        //     }
        // }

    }

