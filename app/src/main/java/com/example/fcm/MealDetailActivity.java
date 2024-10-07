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
    MealDAO mealDAO;

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
        mealDAO = MealDBinstance.getDataBase1(getApplicationContext()).mealDAO();

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
//        String mealName = editTextMealName.getText().toString();
        double mealWeight = Double.parseDouble(weightEditText.getText().toString());
//        String mealType = spinnerMealType.getSelectedItem().toString();
//        String date = getIntent().getStringExtra("selectedDate");
//        Meal meal = new Meal();
//        meal.setDate(date);
//        meal.setMealName(mealName);
//        meal.setImage(R.drawable.test2);
//        meal.setMealType(mealType);
//        meal.setPortionSize(mealWeight);
//        mealDAO.insert(meal);

        String mealNamep = editTextMealName.getText().toString();
        String mealTypep = spinnerMealType.getSelectedItem().toString();

        // Validate the meal name
        if (mealNamep.isEmpty()) {
            Toast.makeText(this, "Please enter a meal name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate weight
        if (weightEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter the weight of the meal", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate image
//        if (mealImageUri == null && mealImageBitmap == null) {
//            Toast.makeText(this, "Please capture or select a meal image", Toast.LENGTH_SHORT).show();
//            return;
//        }

        String mealDate = getIntent().getStringExtra("mealDate"); // Get date from intent or use the current date

        // Create an Intent to pass the meal details to MealSummaryActivity
        Intent intentp = new Intent(MealDetailActivity.this, MealSummaryActivity.class);
        intentp.putExtra("mealName", mealNamep);
        intentp.putExtra("mealType", mealTypep);
        intentp.putExtra("mealDate", mealDate);

//        if (mealImageUri != null) {
//            // Pass the image URI instead of Bitmap
//            intentp.putExtra("mealImageUri", mealImageUri.toString());
//        } else if (mealImageBitmap != null) {
//            // If using a small image bitmap, pass as Parcelable (Be cautious with large images)
//            intentp.putExtra("mealImageBitmap", mealImageBitmap);
//        }

        startActivity(intentp);

        //Toast.makeText(this, "Meal saved: " + mealName + " (" + mealWeight + "g) - " + mealType, Toast.LENGTH_SHORT).show();
    }
}