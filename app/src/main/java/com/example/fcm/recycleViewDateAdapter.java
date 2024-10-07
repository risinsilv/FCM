package com.example.fcm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class recycleViewDateAdapter extends RecyclerView.Adapter<recycleViewDateAdapter.DateViewHolder> {
    private List<DailyIntake> dateList;
    private Context context;
    private OnDateClickListener onDateClickListener;

    public recycleViewDateAdapter(List<DailyIntake> dateList, Context context, OnDateClickListener onDateClickListener) {
        this.dateList = dateList;
        this.context = context;
        this.onDateClickListener = onDateClickListener;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_date, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position) {
        DailyIntake dailyIntake = dateList.get(position);
        holder.dateTextView.setText(dailyIntake.getDate());
        holder.itemView.setOnClickListener(v -> onDateClickListener.onDateClick(dailyIntake));
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;

        public DateViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.textViewDate); // Replace with your date TextView ID
        }
    }

    public interface OnDateClickListener {
        void onDateClick(DailyIntake dateData);
    }
}

