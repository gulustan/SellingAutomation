package com.example.gulus.android2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SaleActivity extends AppCompatActivity implements AdapterListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Model> stockList = new ArrayList<>();
    private List<Model> searchStockList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        DataBaseClass dataBaseClass = new DataBaseClass(getApplicationContext());
        dataBaseClass.setProductReference(stockList);
        searchStockList = new ArrayList<>(stockList);
        mRecyclerView = findViewById(R.id.saleRV);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdapterRV(this,searchStockList,this);
        mRecyclerView.setAdapter(mAdapter);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sale_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.sale_menu_app_bar_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                searchStockList.clear();
                for (Model x:stockList) {
                    if(x.getName().toLowerCase().contains(s.toLowerCase()))
                        searchStockList.add(x);
                }
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;

    }


    @Override
    public void onItemClick(View view, int position) {
        FaturaDialog sellDialog = new FaturaDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("id", searchStockList.get(position).getId());
        sellDialog.setArguments(bundle);
        sellDialog.show(getSupportFragmentManager(),"sell dialog");

    }
}
