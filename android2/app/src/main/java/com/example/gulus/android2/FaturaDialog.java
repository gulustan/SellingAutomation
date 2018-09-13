package com.example.gulus.android2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class FaturaDialog extends AppCompatDialogFragment {
    DataBaseClass database;


    DatePicker datePicker;
    EditText buyerText;
    TextView totalPrice;
    EditText stockNumText;
    int itemId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fatura_dialog,null);

        database = new DataBaseClass(getActivity());
        itemId = getArguments().getInt("id");
        final Model itemModel = database.getProduct(itemId);

        datePicker = view.findViewById(R.id.fatura_dialog_datePicker);
        buyerText = view.findViewById(R.id.fatura_dialog_buyer);
        totalPrice = view.findViewById(R.id.fatura_dialog_total);
        stockNumText = view.findViewById(R.id.fatura_dialog_stocknum);




        builder.setView(view).setTitle(itemModel.getName())
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Sell", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!stockNumText.getText().toString().equals("")
                        && !buyerText.getText().toString().equals("")){
                    int quantity = Integer.valueOf(stockNumText.getText().toString()).intValue();
                    String buyer = buyerText.getText().toString();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                    Date date = new Date(calendar.getTimeInMillis());
                    database.addSale(new SaleModel(0,itemId,buyer,quantity,date));
                }
            }
        });

        stockNumText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshTotalPrice(itemModel.getPrice());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return builder.create();
    }

    void refreshTotalPrice(float price){
        if(stockNumText.getText().toString().equals(""))
            totalPrice.setText("0");
        else{
            int multiplier = Integer.valueOf(stockNumText.getText().toString()).intValue();
            totalPrice.setText(String.valueOf(price * multiplier));
        }
    }
}