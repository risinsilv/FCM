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

    public recycleViewAdapter(ArrayList<recycleViewData> recycleList, Context context) {
        this.recycleList = recycleList;
        this.context = context;
    }

    @NonNull
    @Override
    public recycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleViewAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(recycleList.get(position).getImage());
        holder.textView.setText(recycleList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return recycleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);

        }
    }
}
