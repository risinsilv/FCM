package com.example.fcm;

import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MealDetailActivity extends AppCompatActivity {

    private ImageButton mealImageButton;
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
        mealImageButton = findViewById(R.id.mealImageButton);
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
                        mealImageButton.setImageBitmap(mealImageBitmap);
                    }
                }
        );

        // Gallery launcher to select image from gallery
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        mealImageUri = result.getData().getData();
                        mealImageButton.setImageURI(mealImageUri);
                    }
                }
        );

        mealImageButton.setOnClickListener(v -> showImageSourceDialog());


        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");

        String[] options = {"Camera", "Gallery"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Camera option selected
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraLauncher.launch(takePictureIntent);
                } else if (which == 1) {
                    // Gallery option selected
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryLauncher.launch(galleryIntent);
                }
            }
        });

        builder.show();
    }

    private void saveMeal() {
       String mealName = editTextMealName.getText().toString();
     double mealWeight = Double.parseDouble(weightEditText.getText().toString());
       String mealType = spinnerMealType.getSelectedItem().toString();
       String date = getIntent().getStringExtra("selectedDate");
       Meal meal = new Meal();
       meal.setDate(date);
       meal.setMealName(mealName);
       meal.setImage(R.drawable.test2);
       meal.setMealType(mealType);
       meal.setPortionSize(mealWeight);
       mealDAO.insert(meal);

        //String mealNamep = editTextMealName.getText().toString();
        //String mealTypep = spinnerMealType.getSelectedItem().toString();

        // Validate the meal name
        if (mealName.isEmpty()) {
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

       ; // Get date from intent or use the current date

        // Create an Intent to pass the meal details to MealSummaryActivity
        Intent intentp = new Intent(MealDetailActivity.this, MealSummaryActivity.class);
        intentp.putExtra("mealName", mealName);
        intentp.putExtra("mealType", mealType);
        intentp.putExtra("mealDate", date);

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