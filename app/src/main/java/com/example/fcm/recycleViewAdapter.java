package com.example.fcm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class recycleViewAdapter extends RecyclerView.Adapter<recycleViewAdapter.ViewHolder> {
    private ArrayList<Meal> recycleList;
    private Context context;
    private OnMealClickListener onMealClickListener;

    ImageStorage imageStorage = ImageStorage.getInstance();

    public interface OnMealClickListener {
        void onMealClick(Meal mealItem, int position);
    }

    public recycleViewAdapter(ArrayList<Meal> recycleList, Context context, OnMealClickListener onMealClickListener) {
        this.recycleList = recycleList;
        this.context = context;
        this.onMealClickListener = onMealClickListener;
    }

    @NonNull
    @Override
    public recycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleViewAdapter.ViewHolder holder, int position) {
        Meal mealItem = recycleList.get(position);
        if(imageStorage.getImage(mealItem.getImage())!= null){
            Bitmap image = imageStorage.getImage(mealItem.getImage());
            holder.imageView.setImageBitmap(image);
        }else {
            setImage(mealItem, holder.imageView);
        }
        holder.textView.setText(mealItem.getMealName());

        holder.itemView.setOnClickListener(v -> {
            // Create an intent to navigate to MealDetailActivity
            Intent intent = new Intent(context, MealSummaryActivity.class);

            // Optionally pass data about the clicked meal to the next activity (e.g., meal name)
            intent.putExtra("mealName", mealItem.getMealName());
            intent.putExtra("mealDate", mealItem.getDate());

            // Start the activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recycleList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);

        }
    }
    public void setImage(Meal mealItem, ImageView imageView){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(mealItem.getImage());

        try{
            File tempFile = File.createTempFile("tempFile","jpg");
            storageReference.getFile(tempFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                    String imageKey = mealItem.getImage();
                    Bitmap imageBitmap = bitmap;
                    imageStorage.addImage(imageKey,imageBitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
