package com.example.fcm;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycleView extends AppCompatActivity {
    ArrayList<recycleViewData> recycleList;
    ArrayList<recycleViewDateData> recycleListDate;
    RecyclerView recyclerView;
    RecyclerView recyclerViewDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recycle_view);

        recyclerViewDate = findViewById(R.id.recyclerViewDate);
        recyclerViewDate.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDate.setLayoutManager(layoutManager);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        recycleListDate = new ArrayList<recycleViewDateData>();
        recycleListDate.add(new recycleViewDateData(2024,"Oct",2));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",3));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",4));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",5));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",6));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",7));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",8));
        recycleListDate.add(new recycleViewDateData(2024,"Oct",9));
        recycleViewDateAdapter recycleViewDateAdapter = new recycleViewDateAdapter(recycleListDate,this);
        recyclerViewDate.setAdapter(recycleViewDateAdapter);


        recycleList = new ArrayList<recycleViewData>();
        recycleList.add(new recycleViewData(R.drawable.test2,"Test 1"));
        recycleList.add(new recycleViewData(R.drawable.test2,"Test 2"));
        recycleList.add(new recycleViewData(R.drawable.test2,"Test 3"));
        recycleList.add(new recycleViewData(R.drawable.test2,"Test 4"));
        recycleList.add(new recycleViewData(R.drawable.test2,"Test 5"));
        recycleList.add(new recycleViewData(R.drawable.test2,"Test 6"));
        recycleList.add(new recycleViewData(R.drawable.test2,"Test 7"));
        recycleList.add(new recycleViewData(R.drawable.test2,"Test 8"));
        recycleList.add(new recycleViewData(R.drawable.test2,"Test 9"));
        recycleList.add(new recycleViewData(R.drawable.test2,"Test 10"));
        recycleList.add(new recycleViewData(R.drawable.test2,"Test 11"));

        recycleViewAdapter recycleViewAdapter = new recycleViewAdapter(recycleList,this);
        recyclerView.setAdapter(recycleViewAdapter);

    }
}