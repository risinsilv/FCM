package com.example.fcm;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealDetailActivity extends AppCompatActivity {

    private ImageButton mealImageButton;
    private EditText weightEditText,textViewWeight;
    private Bitmap mealImageBitmap;
    private Uri mealImageUri;
    private EditText editTextMealName;
    private Spinner spinnerMealType;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ProgressBar loading;
    Button buttonSaveMeal;
    MealDAO mealDAO;
    ImageStorage imageStorage = ImageStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        EdgeToEdge.enable(this);

        editTextMealName = findViewById(R.id.editTextMealName);
        weightEditText = findViewById(R.id.weightEditText);
        spinnerMealType = findViewById(R.id.spinnerMealType);
        buttonSaveMeal = findViewById(R.id.buttonSaveMeal);
        mealImageButton = findViewById(R.id.mealImageButton);
        mealDAO = MealDBinstance.getDataBase1(getApplicationContext()).mealDAO();
        loading = findViewById(R.id.progressBar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(adapter);


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

                        mealImageUri = saveImageToTemp(mealImageBitmap);
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
        buttonSaveMeal.setOnClickListener(v -> saveMeal());


    }
    public interface NutritionDataCallback {
        void onNutritionDataFetched(Meal meal);
        void onNutritionDataFailed(String errorMessage);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Uri saveImageToTemp(Bitmap bitmap){
        File tempfile = null;
        try{
            File cacheDir = getCacheDir();

            tempfile = File.createTempFile("meal_image_",".jpg", cacheDir);

            FileOutputStream fos = new FileOutputStream(tempfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();

            return Uri.fromFile(tempfile);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
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

        //Validate image
        if (mealImageUri == null && mealImageBitmap == null) {
            Toast.makeText(this, "Please capture or select a meal image", Toast.LENGTH_SHORT).show();
            return;
        }
       String date = getIntent().getStringExtra("selectedDate");
       Meal meal = new Meal();
       String imageUri = String.valueOf(System.currentTimeMillis());//getting unique reference to a image uri
       meal.setImage(imageUri);// Saving image to firebase
       meal.setDate(date);
       meal.setMealName(mealName);
       meal.setMealType(mealType);
       meal.setPortionSize(mealWeight);


        Intent intentp = new Intent(MealDetailActivity.this, MealSummaryActivity.class);
        intentp.putExtra("mealName", mealName);
        intentp.putExtra("mealDate", date);

        saveImage(imageUri, meal, intentp);

    }
    private void saveImage(String imageUri, Meal meal, Intent intent) {
        buttonSaveMeal.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
        StorageReference fileReference = FirebaseStorage.getInstance().getReference(imageUri);

        fileReference.putFile(mealImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Image successfully uploaded
                Toast.makeText(MealDetailActivity.this, "Image successfully uploaded", Toast.LENGTH_SHORT).show();
                try {
                    imageStorage.addImage(imageUri,uriToBitmap(mealImageUri));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Now proceed to save the meal and start the next activity

                fetchAndSetNutritionData(meal.getMealName(), meal.getPortionSize(), meal, intent);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MealDetailActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                buttonSaveMeal.setEnabled(true);
                loading.setVisibility(View.GONE);
            }
        });
    }
    // API call to fetch nutrition data
    private void fetchAndSetNutritionData(String query, double weight,Meal meal,Intent intent) {
        CalorieNinjasApiService apiService = ApiClient.getClient().create(CalorieNinjasApiService.class);
        Call<NutritionResponse> call = apiService.getNutritionInfo(query);

        Log.d("DEBUG", "Making API Call with Query: " + query);

        call.enqueue(new Callback<NutritionResponse>() {
            @Override
            public void onResponse(Call<NutritionResponse> call, Response<NutritionResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().items != null && !response.body().items.isEmpty()) {

                    Log.d("DEBUG", "API Response Success: " + response.body().toString());  // Log the response

                    NutritionItem item = response.body().items.get(0);

                    // Calculate nutrition values based on weight
                    double calories = item.calories * (weight / 100);
                    double fats = item.fat_total_g * (weight / 100);
                    double proteins = item.protein_g * (weight / 100);
                    double carbohydrates = item.carbohydrates_total_g * (weight / 100);

                    // Start NutritionActivity to display the data
                    meal.setCalories(calories);
                    meal.setCarbohydrates(carbohydrates);
                    meal.setFats(fats);
                    meal.setProteins(proteins);
                    mealDAO.insert(meal);
                    loading.setVisibility(View.GONE);
                    startActivity(intent);


                } else {
                    Log.e("ERROR", "API Response Failed or No Items: " + response.code());  // Log error code and details
                    Toast.makeText(MealDetailActivity.this, "Failed to get nutrition info", Toast.LENGTH_SHORT).show();
                    showManualInputDialog(meal, intent);

                }
            }

            @Override
            public void onFailure(Call<NutritionResponse> call, Throwable t) {
                Toast.makeText(MealDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                showManualInputDialog(meal, intent);
            }
        });

    }
    private Bitmap uriToBitmap(Uri uri)throws IOException{
        ContentResolver contentResolver = getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }

    private void showManualInputDialog(Meal meal, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Nutrition Data Manually");

        // Create a custom layout for the dialog with input fields for calories, carbs, fats, and proteins
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_manual_nutrition_input, null);
        builder.setView(dialogView);

        // Find the EditText fields in the custom layout
        EditText inputCalories = dialogView.findViewById(R.id.inputCalories);
        EditText inputCarbs = dialogView.findViewById(R.id.inputCarbohydrates);
        EditText inputFats = dialogView.findViewById(R.id.inputFats);
        EditText inputProteins = dialogView.findViewById(R.id.inputProteins);

        // Set the buttons for the dialog
        builder.setPositiveButton("Save", (dialog, which) -> {
            // Retrieve values entered by the user
            double calories = Double.parseDouble(inputCalories.getText().toString());
            double carbohydrates = Double.parseDouble(inputCarbs.getText().toString());
            double fats = Double.parseDouble(inputFats.getText().toString());
            double proteins = Double.parseDouble(inputProteins.getText().toString());

            // Set the values in the Meal object
            meal.setCalories(calories);
            meal.setCarbohydrates(carbohydrates);
            meal.setFats(fats);
            meal.setProteins(proteins);

            // Save the meal to the database and start the next activity
            mealDAO.insert(meal);
            startActivity(intent);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }

}