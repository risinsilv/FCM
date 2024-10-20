package com.example.fcm;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
        List<String> generatedDates = getThreeMonthsDates();

        // Fetch the saved dates from the database as a List<String>
        List<DailyIntake> savedDailyIntakes = dailyIntakeDAO.getAlldates();
        List<String> savedDates = new ArrayList<>();
        for (DailyIntake dailyIntake : savedDailyIntakes) {
            savedDates.add(dailyIntake.getDate());
        }

        // Merge the generated dates and saved dates, and remove duplicates
        List<String> allDates = mergeAndRemoveDuplicates(generatedDates, savedDates);

        // Populate the recycleListDate with DailyIntake objects
        recycleListDate = new ArrayList<>();
        for (String date : allDates) {
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

        List<Meal> temp = mealDAO.getMealsByDate(currentDate);
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

                if (isDateAlreadyPresent(formattedDate)) {
                    Toast.makeText(recycleView.this, "Date already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a new DailyIntake object for the selected date
                    DailyIntake newDate = new DailyIntake();
                    newDate.setDate(formattedDate);

                    // Insert new date into the list
                    insertNewDate(newDate);
                }
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
        });//lol


        ImageButton addButton = findViewById(R.id.imageButton2);
        addButton.setOnClickListener(v -> {
            if (selectedDateData != null) {
                // Launch MealDetailActivity and pass the selected date data
                if (dailyIntakeDAO.getData(selectedDateData.getDate()) == null){
                    DailyIntake date = new DailyIntake();
                    date.setDate(selectedDateData.getDate());
                    dailyIntakeDAO.insert(selectedDateData);
            }
                Intent intent = new Intent(recycleView.this, MealDetailActivity.class);
                intent.putExtra("selectedDate", selectedDateData.getDate()); // Passing the selected date
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select a date first", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton dailyGoal = findViewById(R.id.button3);
        dailyGoal.setOnClickListener(v -> {
            if (selectedDateData != null) {
                if(dailyIntakeDAO.getData(selectedDateData.getDate()).getTergatIntake() == 0) {
                    showGoalInputDialog();
                }else{
                    Double goalIntake = dailyIntakeDAO.getData(selectedDateData.getDate()).getTergatIntake();
                    Intent intent = new Intent(recycleView.this, Nutrition.class);
                    intent.putExtra("selectedDate", selectedDateData.getDate());
                    intent.putExtra("goalIntake", goalIntake);
                    startActivity(intent);
                }
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

    private List<String> mergeAndRemoveDuplicates(List<String> list1, List<String> list2) {
        TreeSet<String> uniqueDates = new TreeSet<>((d1, d2) -> compareDates(d1, d2));  // TreeSet automatically sorts and removes duplicates
        uniqueDates.addAll(list1);
        uniqueDates.addAll(list2);

        return new ArrayList<>(uniqueDates);
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
    private boolean isDateAlreadyPresent(String date) {
        for (DailyIntake intake : recycleListDate) {
            if (intake.getDate().equals(date)) {
                return true; // Date is already in the list
            }
        }
        return false;
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
    private void showGoalInputDialog() {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate a custom layout with an EditText for user input
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_calorie_goal, null);

        // Reference the EditText inside the custom layout
        final EditText inputGoal = dialogView.findViewById(R.id.calorie_goal_input);

        // Set the dialog view to the builder
        builder.setView(dialogView)
                .setTitle("Set Daily Calorie Goal")
                .setMessage("Please enter your daily calorie goal")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the value entered by the user
                        String calorieGoalStr = inputGoal.getText().toString();

                        // Validate the input (check if it's not empty and is a valid number)
                        if (!calorieGoalStr.isEmpty() && calorieGoalStr.matches("\\d+")) {
                            double goalIntake = Double.parseDouble(calorieGoalStr);
                            DailyIntake dailyIntake2 = new DailyIntake();
                            dailyIntake2.setDate(selectedDateData.getDate());
                            dailyIntake2.setTergatIntake(goalIntake);
                            dailyIntakeDAO.update(dailyIntake2);

                            // Launch MealDetailActivity and pass the selected date and calorie goal
                            Intent intent = new Intent(recycleView.this, Nutrition.class);
                            intent.putExtra("selectedDate", selectedDateData.getDate());
                            intent.putExtra("goalIntake", goalIntake);
                            startActivity(intent);

                        } else {
                            Toast.makeText(recycleView.this, "Invalid input. Please enter a number.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Close the dialog on cancel
                    }
                }); //o

        // Show the dialog
        builder.create().show();
    }


    }

