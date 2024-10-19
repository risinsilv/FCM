package com.example.fcm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class recycleView extends AppCompatActivity {
    ArrayList<Meal> recycleList;
    ArrayList<DailyIntake> recycleListDate;
    RecyclerView recyclerView;
    RecyclerView recyclerViewDate;
    recycleViewAdapter recycleViewAdapter;
    recycleViewDateAdapter dateAdapter;
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
        List<String> dates = getThreeMonthsDates();

        // Populate the recycleListDate with DailyIntake objects
        recycleListDate = new ArrayList<>();
        for (String date : dates) {
            DailyIntake dailyIntake = new DailyIntake();
            dailyIntake.setDate(date);
            recycleListDate.add(dailyIntake);
        }


        // Find the position of the current date
        String currentDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(calendar.getTime());
        int currentPosition = IntStream.range(0, recycleListDate.size()).filter(i -> recycleListDate.get(i).getDate().equals(currentDate)).findFirst().orElse(-1);

        // Set up the date adapter with the current date highlighted
        dateAdapter = new recycleViewDateAdapter(recycleListDate, this, new recycleViewDateAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(DailyIntake dateData) {
                updateMainRecyclerViewData(dateData);
                selectedDateData = dateData; // Set selected date data
                Toast.makeText(recycleView.this, dateData.getDate(), Toast.LENGTH_SHORT).show();
            }
        }, currentPosition);  // Pass the current date position to the


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

        // Scroll to the current date position
        if (currentPosition != -1) {
            recyclerViewDate.scrollToPosition(currentPosition);
        }

        Button addDateButton = findViewById(R.id.addDateButton);
        addDateButton.setOnClickListener(v -> {
            // Show a DatePickerDialog to let the user select a new date
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                // Format the date (e.g., 10-Sep-2024)
                String formattedDate = String.format("%02d-%s-%d", dayOfMonth, getMonthShortName(month + 1), year);

                // Create a new DailyIntake object for the new date
                DailyIntake newDate = new DailyIntake();
                newDate.setDate(formattedDate);

                // Insert the new date into the list at the correct position
                insertNewDate(newDate);
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });

        Button scrollToCurrentDateButton = findViewById(R.id.scrollToCurrentDateButton);
        scrollToCurrentDateButton.setOnClickListener(v -> {
            // Scroll to the current date position
            if (currentPosition != -1) {
                recyclerViewDate.scrollToPosition(currentPosition);
                if (recycleListDate.size() > currentPosition) {
                    DailyIntake currentDateData = recycleListDate.get(currentPosition);

                    dateAdapter.setSelectedPosition(currentPosition); // Mark as selected if needed
                    Toast.makeText(recycleView.this, currentDateData.getDate(), Toast.LENGTH_SHORT).show();
                    dateAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(this, "Current date not found", Toast.LENGTH_SHORT).show();
            }
        });


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

    private List<String> getThreeMonthsDates() {
        List<String> allDates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        // Get dates for the previous month
        calendar.add(Calendar.MONTH, -1);
        allDates.addAll(DateUtils.getDatesOfMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));

        // Get dates for the current month
        calendar.add(Calendar.MONTH, 1);
        allDates.addAll(DateUtils.getDatesOfMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));

        // Get dates for the next month
        calendar.add(Calendar.MONTH, 1);
        allDates.addAll(DateUtils.getDatesOfMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));

        return allDates;
    }
    private List<DailyIntake> removeDuplicatesAndSort(List<DailyIntake> dateList) {
        // Use a TreeSet to remove duplicates and sort dates
        TreeSet<DailyIntake> sortedSet = new TreeSet<>((d1, d2) -> compareDates(d1.getDate(), d2.getDate()));
        sortedSet.addAll(dateList);

        return new ArrayList<>(sortedSet);
    }

    private String getMonthShortName(int month) {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month - 1];
    }

    // Method to insert the new date into the correct position in the list
    private void insertNewDate(DailyIntake newDate) {
        int insertPosition = 0;

        // Find the correct position to insert the new date
        for (int i = 0; i < recycleListDate.size(); i++) {
            DailyIntake existingDate = recycleListDate.get(i);
            if (compareDates(newDate.getDate(), existingDate.getDate()) < 0) {
                insertPosition = i;
                break;
            } else if (i == recycleListDate.size() - 1) {
                // If the new date is after all existing dates, insert it at the end
                insertPosition = recycleListDate.size();
            }
        }

        // Insert the new date into the list
        recycleListDate.add(insertPosition, newDate);

        // Insert the new date into the database (Make sure DailyIntakeDAO has an insert method)
        dailyIntakeDAO.insert(newDate);

        // Notify the adapter and scroll to the new date
        dateAdapter.notifyItemInserted(insertPosition);
        recyclerViewDate.scrollToPosition(insertPosition); // Optionally scroll to the new date
    }

    // Helper method to compare dates in format dd-MMM-yyyy (e.g., 10-Sep-2024)
    private int compareDates(String date1, String date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            Date d1 = dateFormat.parse(date1);
            Date d2 = dateFormat.parse(date2);
            return d1.compareTo(d2); // Returns negative if d1 is before d2, 0 if equal, positive if after
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
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

