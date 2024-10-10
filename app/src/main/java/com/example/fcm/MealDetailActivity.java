package com.example.fcm;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText weightEditText;
    private Bitmap mealImageBitmap;
    private Uri mealImageUri;
    private EditText editTextMealName;
    private Spinner spinnerMealType;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    Context context;
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


        findViewById(R.id.back_button).setOnClickListener(v -> finish());
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
       String date = getIntent().getStringExtra("selectedDate");
       Meal meal = new Meal();
       String imageUri = String.valueOf(System.currentTimeMillis());//getting unique reference to a image uri
       meal.setImage(imageUri);
       saveImage(imageUri);// Saving image to firebase
       meal.setDate(date);
       meal.setMealName(mealName);
       meal.setMealType(mealType);
       meal.setPortionSize(mealWeight);

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

       ; // Get date from intent or use the current date

        // Create an Intent to pass the meal details to MealSummaryActivity

        Intent intentp = new Intent(MealDetailActivity.this, MealSummaryActivity.class);
        intentp.putExtra("mealName", mealName);
        intentp.putExtra("mealDate", date);
        fetchAndSetNutritionData(mealName, mealWeight, meal,intentp);

//        if (mealImageUri != null) {
//            // Pass the image URI instead of Bitmap
//            intentp.putExtra("mealImageUri", mealImageUri.toString());
//        } else if (mealImageBitmap != null) {
//            // If using a small image bitmap, pass as Parcelable (Be cautious with large images)
//            intentp.putExtra("mealImageBitmap", mealImageBitmap);
//        }



        //Toast.makeText(this, "Meal saved: " + mealName + " (" + mealWeight + "g) - " + mealType, Toast.LENGTH_SHORT).show();
    }
     private void saveImage(String imageUri){
         StorageReference fileReference = FirebaseStorage.getInstance().getReference(imageUri);

         fileReference.putFile(mealImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 Toast.makeText(MealDetailActivity.this, "Sucessfully uploaded", Toast.LENGTH_SHORT).show();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(MealDetailActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
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
                    startActivity(intent);


                } else {
                    Log.e("ERROR", "API Response Failed or No Items: " + response.code());  // Log error code and details
                    Toast.makeText(MealDetailActivity.this, "Failed to get nutrition info", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<NutritionResponse> call, Throwable t) {
                Toast.makeText(MealDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                mealDAO.insert(meal);
                startActivity(intent);
            }
        });

    }

}