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
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recycle_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

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