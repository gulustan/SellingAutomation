package com.example.gulus.android2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StockEditActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button addButton;
    Button editButton;
    Button deleteButton;
    EditText nameText;
    EditText categoryText;
    EditText providerText;
    EditText priceText;
    EditText stockNumText;


    boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_edit);

        final DataBaseClass database = new DataBaseClass(getApplicationContext());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Buttons
        addButton = findViewById(R.id.stock_edit_create);
        editButton = findViewById(R.id.stock_activity_edit);
        deleteButton = findViewById(R.id.stock_activity_delete);


        //EditTexts
        nameText = findViewById(R.id.stock_edit_name);
        categoryText = findViewById(R.id.stock_edit_category);
        providerText = findViewById(R.id.stock_edit_provider);
        priceText = findViewById(R.id.stock_edit_price);
        stockNumText = findViewById(R.id.stock_edit_stockNum);

        final Intent passedIntent = getIntent();
        if (passedIntent.getExtras() != null) {
            editMode = true;
            Model edittedModel = database.getProduct(passedIntent.getIntExtra("id", 0));
            nameText.setText(edittedModel.getName());
            categoryText.setText(edittedModel.getCategory());
            providerText.setText(edittedModel.getProvider());
            priceText.setText(String.valueOf(edittedModel.getPrice()));
            stockNumText.setText(String.valueOf(edittedModel.getStockNum()));
        }

        if (editMode == true) {
            addButton.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Edit Activity");
        } else if (editMode == false) {
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Create Activity");
        }


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                String category = categoryText.getText().toString();
                String provider = providerText.getText().toString();
                Float price = priceText.getText().toString().equals("") ? null : Float.valueOf(priceText.getText().toString());
                Integer stock = stockNumText.getText().toString().equals("") ? null : Integer.valueOf(stockNumText.getText().toString());
                if (editMode == false /*&&
                        !name.equals("") &&
                        !category.equals("") &&
                        !provider.equals("") &&
                        price != null &&
                        price.floatValue() >= 0 &&
                        stock != null &&
                        stock.intValue() >= 0*/) {
                    database.addProduct(new Model(0,name, category, provider, price, stock));
                    //database.addProduct(new Model(0, name, category, provider, price, stock));
                    startActivity(new Intent(getApplicationContext(), ListActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Fill all places!", Toast.LENGTH_LONG).show();
                    if (stock == null || stock.intValue() < 0)
                        stockNumText.setText(String.valueOf(0));
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            //TODO revise, restrict with sales
            @Override
            public void onClick(View view) {
                if (editMode == true) {
                    database.deleteProduct(getIntent().getIntExtra("id", 0));
                    startActivity(new Intent(getApplicationContext(), ListActivity.class));
                }
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                String category = categoryText.getText().toString();
                String provider = providerText.getText().toString();
                Float price = priceText.getText().toString().equals("") ? null : Float.valueOf(priceText.getText().toString());
                Integer stock = stockNumText.getText().toString().equals("") ? null : Integer.valueOf(stockNumText.getText().toString());
                if (editMode == true /*&&
                        !name.equals("") &&
                        !category.equals("") &&
                        !provider.equals("") &&
                        price != null &&
                        price.floatValue() >= 0 &&
                        stock != null &&
                        stock.intValue() >= 0*/) {
                    database.editProduct(new Model(passedIntent.getIntExtra("id", 0), name, category, provider, price, stock));
                    startActivity(new Intent(getApplicationContext(), ListActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Fill all places!", Toast.LENGTH_LONG).show();
                    if (stock == null || stock.intValue() < 0)
                        stockNumText.setText(String.valueOf(0));
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_app_bar_stock_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}