package com.example.fcm;

import static java.lang.String.valueOf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

 public class EditMeal extends AppCompatActivity {
     private ActivityResultLauncher<Intent> cameraLauncher;
     private ActivityResultLauncher<Intent> galleryLauncher;
     private Bitmap mealImageBitmap;
     private Uri mealImageUri;
     EditText mealName,mealWeight,calories,fats,protein,carbs;
     String mealNameS,mealDateS;
     Spinner spinner;
     ImageStorage imageStorage = ImageStorage.getInstance();
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         EdgeToEdge.enable(this);
         setContentView(R.layout.activity_edit_meal);

         ImageButton imageButton = findViewById(R.id.imageButton);
         mealName = findViewById(R.id.editTextMealName2);
         spinner = findViewById(R.id.spinnerMealType2);
         mealWeight = findViewById(R.id.weightEditText2);
         calories = findViewById(R.id.editCalories);
         fats = findViewById(R.id.editFat);
         protein = findViewById(R.id.editProteins);
         carbs = findViewById(R.id.editCarbs);
         Button saveButton =  findViewById(R.id.saveButton);



         ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                 R.array.meal_types, android.R.layout.simple_spinner_item);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         Intent intent = getIntent();
         mealNameS = intent.getStringExtra("mealName");
         mealDateS = intent.getStringExtra("mealDate");
         MealDAO mealDAO = MealDBinstance.getDataBase1(getApplicationContext()).mealDAO();
         Meal meal = mealDAO.getTheMeal(mealDateS, mealNameS);
         spinner.setAdapter(adapter);

         mealImageBitmap = imageStorage.getImage(meal.getImage());
         mealImageUri = saveImageToTemp( mealImageBitmap);

         mealName.setText(mealNameS);
         imageButton.setImageBitmap(mealImageBitmap);
         mealWeight.setText(String.valueOf(meal.getPortionSize()));
         calories.setText(String.valueOf(meal.getCalories()));
         fats.setText(String.valueOf(meal.getFats()));
         protein.setText(String.valueOf(meal.getProteins()));
         carbs.setText(String.valueOf(meal.getCarbohydrates()));


         // Camera launcher to capture image
         cameraLauncher = registerForActivityResult(
                 new ActivityResultContracts.StartActivityForResult(),
                 result -> {
                     if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                         Bundle extras = result.getData().getExtras();
                         mealImageBitmap = (Bitmap) extras.get("data");
                         imageButton.setImageBitmap(mealImageBitmap);

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
                         imageButton.setImageURI(mealImageUri);
                     }
                 }
         );
         imageButton.setOnClickListener(v -> showImageSourceDialog());
         saveButton.setOnClickListener(v -> saveMeal());


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

     private Uri saveImageToTemp(Bitmap bitmap) {
         File tempfile = null;
         try {
             File cacheDir = getCacheDir();

             tempfile = File.createTempFile("meal_image_", ".jpg", cacheDir);

             FileOutputStream fos = new FileOutputStream(tempfile);
             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
             fos.flush();
             fos.close();

             return Uri.fromFile(tempfile);
         } catch (IOException e) {
             e.printStackTrace();
             return null;
         }
     }
     private void saveMeal(){
         String mName = mealName.getText().toString();
         double mWeight = Double.parseDouble(mealWeight.getText().toString());
         double mCalories = Double.parseDouble(calories.getText().toString());
         double mProteins =  Double.parseDouble(protein.getText().toString());
         double mFats =  Double.parseDouble(fats.getText().toString());
         double mCarbs = Double.parseDouble(carbs.getText().toString());
         String mealType = spinner.getSelectedItem().toString();
         MealDAO mealDAO = MealDBinstance.getDataBase1(getApplicationContext()).mealDAO();
         Meal mealT = mealDAO.getTheMeal(mealDateS,mealNameS);

         String imageUri = mealT.getImage();
         mealDAO.delete(mealT);//deleting the current meal


         Meal meal = new Meal();
         meal.setImage(imageUri);
         saveImage(imageUri);// Saving image to firebase
         meal.setDate(mealDateS);
         meal.setMealName(mName);
         meal.setMealType(mealType);
         meal.setPortionSize(mWeight);
         meal.setCalories(mCalories);
         meal.setCarbohydrates( mCarbs);
         meal.setFats(mFats );
         meal.setProteins(mProteins);
         mealDAO.insert(meal);

     }
     private void saveImage(String imageUri){
         StorageReference fileReference = FirebaseStorage.getInstance().getReference(imageUri);

         fileReference.putFile(mealImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 Toast.makeText(EditMeal.this, "Sucessfully uploaded", Toast.LENGTH_SHORT).show();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(EditMeal.this, "Upload failed", Toast.LENGTH_SHORT).show();
             }
         });

     }
 }