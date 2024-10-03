package com.example.fcm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycleViewAdapter extends RecyclerView.Adapter<recycleViewAdapter.ViewHolder> {
    private ArrayList<recycleViewData> recycleList;
    private Context context;
    private OnMealClickListener onMealClickListener;

    public interface OnMealClickListener {
        void onMealClick(recycleViewData mealItem, int position);
    }

    public recycleViewAdapter(ArrayList<recycleViewData> recycleList, Context context, OnMealClickListener onMealClickListener) {
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
        recycleViewData mealItem = recycleList.get(position);
        holder.imageView.setImageResource(mealItem.getImage());
        holder.textView.setText(mealItem.getText());

        holder.itemView.setOnClickListener(v -> {
            onMealClickListener.onMealClick(mealItem, position);
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
}
