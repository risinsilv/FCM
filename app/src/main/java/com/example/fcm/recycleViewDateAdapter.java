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
    ArrayList<recycleViewDateData> recyclelist;
    Context context;

    public recycleViewDateAdapter(ArrayList<recycleViewDateData> recyclelist, Context context) {
        this.recyclelist = recyclelist;
        this.context = context;
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
        holder.year.setText(String.valueOf(recyclelist.get(position).getYear()));
        holder.month.setText(recyclelist.get(position).getMonth());
        holder.day.setText(String.valueOf(recyclelist.get(position).getDay()));
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
