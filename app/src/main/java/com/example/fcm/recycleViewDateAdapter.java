package com.example.fcm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycleViewDateAdapter extends RecyclerView.Adapter<recycleViewDateAdapter.ViewHolder>{
    ArrayList<DailyIntake> recyclelist;
    Context context;
    OnDateClickListener onDateClickListener;

    public interface OnDateClickListener {
        void onDateClick(DailyIntake dateData);
    }

    public recycleViewDateAdapter(ArrayList<DailyIntake> recyclelist, Context context, OnDateClickListener listener) {
        this.recyclelist = recyclelist;
        this.context = context;
        this.onDateClickListener = listener;
    }

    @NonNull
    @Override
    public recycleViewDateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_date,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleViewDateAdapter.ViewHolder holder, int position)
    {
        DailyIntake dateData = recyclelist.get(position);
        String[] parts = dateData.getDate().split("-");

        String day = parts[0];   // "01"
        String month = parts[1]; // "sep"
        String year = parts[2];  // "2024"
        holder.year.setText(year);
        holder.month.setText(month);
        holder.day.setText(day);

        holder.itemView.setOnClickListener(v -> {
            onDateClickListener.onDateClick(dateData);
        });
    }

    @Override
    public int getItemCount() {
        return recyclelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView year;
        TextView month;
        TextView day;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            year = itemView.findViewById(R.id.textView4);
            month = itemView.findViewById(R.id.textView5);
            day = itemView.findViewById(R.id.textView6);

        }
    }
}
