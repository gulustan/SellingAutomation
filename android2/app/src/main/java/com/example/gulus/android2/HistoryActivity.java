package com.example.gulus.android2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    //Data
    DataBaseClass database;
    List<SaleModel> sale_list;

    RecyclerView recyclerView;
    SaleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        sale_list = new ArrayList<>();
        database = new DataBaseClass(this);
        database.setSaleListReference(sale_list);


        //RECYCLEVIEW
        recyclerView = findViewById(R.id.historyRV);
        adapter = new SaleAdapter(getApplicationContext(),sale_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }



}
