package com.example.fcm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MealDetailActivity extends AppCompatActivity {

    private ImageView mealImageView;
    private EditText weightEditText;
    private Bitmap mealImageBitmap;
    private Uri mealImageUri;
    private EditText editTextMealName;
    private Spinner spinnerMealType;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        editTextMealName = findViewById(R.id.editTextMealName);
        weightEditText = findViewById(R.id.weightEditText);
        spinnerMealType = findViewById(R.id.spinnerMealType);
        Button buttonSaveMeal = findViewById(R.id.buttonSaveMeal);
        mealImageView = findViewById(R.id.mealImageView);
        Button captureButton = findViewById(R.id.captureButton);
        Button galleryButton = findViewById(R.id.galleryButton);
        Button nutritionButton = findViewById(R.id.nutritionButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(adapter);

        buttonSaveMeal.setOnClickListener(v -> saveMeal());

        // Enable back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Meal Details");
        }

        // Get meal name from Intent
        Intent intent = getIntent();
        String mealName = intent.getStringExtra("mealName");


        // Camera launcher to capture image
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        mealImageBitmap = (Bitmap) extras.get("data");
                        mealImageView.setImageBitmap(mealImageBitmap);
                    }
                }
        );

        // Gallery launcher to select image from gallery
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        mealImageUri = result.getData().getData();
                        mealImageView.setImageURI(mealImageUri);
                    }
                }
        );

        captureButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(takePictureIntent);
        });

        galleryButton.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(galleryIntent);
        });

        nutritionButton.setOnClickListener(v -> {
            String weightStr = weightEditText.getText().toString();
            if (weightStr.isEmpty()) {
                Toast.makeText(this, "Please enter weight", Toast.LENGTH_SHORT).show();
            } else {
                double weight = Double.parseDouble(weightStr);
                Intent nutritionIntent = new Intent(MealDetailActivity.this, NutritionActivity.class);
                nutritionIntent.putExtra("mealName", mealName);
                nutritionIntent.putExtra("weight", weight);
                nutritionIntent.putExtra("protein", 28);   // The actual consumed protein value
                nutritionIntent.putExtra("proteinGoal", 60); // The total protein goal
                nutritionIntent.putExtra("carbs", 100);     // The actual consumed carbs value
                nutritionIntent.putExtra("carbsGoal", 225); // The total carbs goal
                nutritionIntent.putExtra("fat", 83);        // The actual consumed fat value
                nutritionIntent.putExtra("fatGoal", 77);    // The total fat goal
                startActivity(nutritionIntent);
            }
        });

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveMeal() {
        String mealName = editTextMealName.getText().toString();
        String mealWeight = weightEditText.getText().toString();
        String mealType = spinnerMealType.getSelectedItem().toString();

        if (mealName.isEmpty() || mealWeight.isEmpty() || (mealImageBitmap == null && mealImageUri == null)) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // FUCKING ADD THESE MOTHERFUCKERS TO THE DATABASE WITH DATES AND THE FUCK ALL YA DUMB CUNT


        Toast.makeText(this, "Meal saved: " + mealName + " (" + mealWeight + "g) - " + mealType, Toast.LENGTH_SHORT).show();
        finish(); // Optionally, close this activity and return to the previous one
    }
}