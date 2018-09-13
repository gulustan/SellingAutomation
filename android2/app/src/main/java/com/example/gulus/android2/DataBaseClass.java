package com.example.gulus.android2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class DataBaseClass extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public List<Model> getProductListReference() {
        if (productListReference == null)
            productListReference = new ArrayList<>();
        return productListReference;
    }

    enum STATUS {
        SUCCESSFUL,MISSING_INPUT,DATABASE_ERROR,LOGIC_ERROR,DELETE_SOLD_PRODUCT_ERROR
    }

    private static final String DATABASE_NAME = "project_database";

    private static final String TABLE_NAME_PRODUCTS = "products_table";
    private static final String COL1_PRODUCTS = "ID";
    private static final String COL2_PRODUCTS = "NAME";
    private static final String COL3_PRODUCTS = "CATEGORY";
    private static final String COL4_PRODUCTS = "PROVIDER";
    private static final String COL5_PRODUCTS = "PRICE";
    private static final String COL6_PRODUCTS = "STOCK";

    private static final String TABLE_NAME_SALES = "sales_table";
    private static final String COL1_SALES = "ID";
    private static final String COL2_SALES = "PRODUCT_ID";
    private static final String COL3_SALES = "BUYER";
    private static final String COL4_SALES = "QUANTITY";
    private static final String COL5_SALES = "DATE";

    private List<Model> productListReference;
    private List<SaleModel> saleListReference;

    public DataBaseClass(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    public void setProductReference(List<Model> productReference) {
        this.productListReference = productReference;
        syncProductReference();
    }

    public void setSaleListReference (List<SaleModel> saleListReference) {
        this.saleListReference = saleListReference;
        syncSaleReference();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME_PRODUCTS + "(" +
                COL1_PRODUCTS + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL2_PRODUCTS + " TEXT NOT NULL," +
                COL3_PRODUCTS + " TEXT NOT NULL," +
                COL4_PRODUCTS + " TEXT NOT NULL," +
                COL5_PRODUCTS + " FLOAT NOT NULL," +
                COL6_PRODUCTS + " INTEGER NOT NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME_SALES + "(" +
                COL1_SALES +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL2_SALES + " INTEGER NOT NULL," +
                COL3_SALES + " TEXT NOT NULL," +
                COL4_SALES + " INTEGER NOT NULL," +
                COL5_SALES + " DATE NOT NULL," +
                "FOREIGN KEY(" + COL2_SALES + ") REFERENCES " + TABLE_NAME_PRODUCTS + "(" + COL1_PRODUCTS + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PRODUCTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SALES);
        onCreate(sqLiteDatabase);
    }
    public STATUS addProduct(Model mdl){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2_PRODUCTS,mdl.getName());
        contentValues.put(COL3_PRODUCTS,mdl.getCategory());
        contentValues.put(COL4_PRODUCTS,mdl.getProvider());
        contentValues.put(COL5_PRODUCTS,mdl.getPrice());
        contentValues.put(COL6_PRODUCTS,mdl.getStockNum());
        Log.d(TAG, "addData: Adding " + mdl.getName() + " to " + TABLE_NAME_PRODUCTS);
        long result = db.insert(TABLE_NAME_PRODUCTS, null, contentValues);

        if(result == -1){
            db.close();
            return STATUS.DATABASE_ERROR;
        }


        if(productListReference != null)
            productListReference.add(new Model(getProductsLastId(),mdl.getName(),mdl.getCategory(),mdl.getProvider(),mdl.getPrice(),mdl.getStockNum()));

        return STATUS.SUCCESSFUL;
    }


    public STATUS addSale(SaleModel mdl){


        Model currentProduct = getProduct(mdl.getProductId());

        if(currentProduct.getStockNum() < mdl.getQuantity() || currentProduct.getStockNum() < 0)

            return STATUS.DATABASE_ERROR;


        currentProduct.setStockNum(currentProduct.getStockNum() - mdl.getQuantity());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2_SALES,mdl.getProductId());
        contentValues.put(COL3_SALES,mdl.getBuyer());
        contentValues.put(COL4_SALES,mdl.getQuantity());
        contentValues.put(COL5_SALES,mdl.getDate().getTime());
        Log.d(TAG, "addData: Adding " + mdl.getDate() + " to " + TABLE_NAME_SALES);
        long result = db.insert(TABLE_NAME_SALES, null, contentValues);



        editProduct(currentProduct);

        if(result == -1){
            db.close();
            return STATUS.DATABASE_ERROR;
        }


        if(saleListReference != null)
            saleListReference.add(new SaleModel(getSaleLastId(),mdl.getProductId(),mdl.getBuyer(),mdl.getQuantity(),mdl.getDate()));

        if (productListReference != null){
            for(int i = 0;i < productListReference.size();i++){
                if(productListReference.get(i).getId() == currentProduct.getId()){
                    productListReference.get(i).setStockNum(currentProduct.getStockNum());
                    break;
                }
            }
        }


        return STATUS.SUCCESSFUL;
    }

    public STATUS editProduct(Model mdl){


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2_PRODUCTS,mdl.getName());
        contentValues.put(COL3_PRODUCTS,mdl.getCategory());
        contentValues.put(COL4_PRODUCTS,mdl.getProvider());
        contentValues.put(COL5_PRODUCTS,mdl.getPrice());
        contentValues.put(COL6_PRODUCTS,mdl.getStockNum());
        Log.d(TAG, "editData: Updating " + mdl.getName() + " to " + TABLE_NAME_PRODUCTS);
        long result = db.update(TABLE_NAME_PRODUCTS,contentValues, COL1_PRODUCTS + " = ? ", new String[] {String.valueOf(mdl.getId())} );

        if(result == 0){
            db.close();
            return STATUS.DATABASE_ERROR;
        }

        if(productListReference != null){
            for(int i = 0;i < productListReference.size();i++){
                if(productListReference.get(i).getId() == mdl.getId()){
                    productListReference.get(i).setName(mdl.getName());
                    productListReference.get(i).setCategory(mdl.getCategory());
                    productListReference.get(i).setProvider(mdl.getProvider());
                    productListReference.get(i).setPrice(mdl.getPrice());
                    productListReference.get(i).setStockNum(mdl.getStockNum());
                    break;
                }
            }
        }
        return STATUS.SUCCESSFUL;
    }


    public void syncProductReference(){
        if(productListReference == null)
            getProductListReference();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT * FROM " + TABLE_NAME_PRODUCTS, null);
        productListReference.clear();
        while (data.moveToNext()) {
            productListReference.add(new Model(data.getInt(0),
                    data.getString(1),
                    data.getString(2),
                    data.getString(3),
                    data.getFloat(4),
                    data.getInt(5)));
        }
        db.close();
    }

    private void syncSaleReference(){
        if(saleListReference == null)
            return;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT * FROM " + TABLE_NAME_SALES, null);
        saleListReference.clear();
        while (data.moveToNext()) {
            saleListReference.add(new SaleModel(data.getInt(0),
                    data.getInt(1),
                    data.getString(2),
                    data.getInt(3),
                    new Date(data.getLong(4))));
        }
        db.close();
    }

    public Model getProduct(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_PRODUCTS + " WHERE " + COL1_PRODUCTS + " = " + String.valueOf(id),null);

        try {
            if (data != null)
                while (data.moveToFirst())
                    return new Model(data.getInt(0), data.getString(1), data.getString(2), data.getString(3), data.getFloat(4), data.getInt(5));
        }finally {
            data.close();
        }
        return null;
    }

    public SaleModel getSale(int id) {
        //int id, int productId, String buyer, int quantity, Date date
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_SALES + " WHERE " + COL1_SALES + " = " + String.valueOf(id),null);
        if(data != null)
            data.moveToFirst();

        return new SaleModel(data.getInt(0),data.getInt(1),data.getString(2),data.getInt(3),Date.valueOf(data.getString(4)));
    }

    private int getProductsLastId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT " + COL1_PRODUCTS + " FROM " + TABLE_NAME_PRODUCTS + " ORDER BY ID DESC LIMIT 1 ", null);
        data.moveToNext();
        int output = data.getInt(0);
        db.close();
        return output;
    }

    private int getSaleLastId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT " + COL1_SALES + " FROM " + TABLE_NAME_SALES + " ORDER BY ID DESC LIMIT 1 ", null);
        data.moveToNext();
        int output = data.getInt(0);
        db.close();
        return output;
    }

    public STATUS deleteProduct (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        if(isProductSaleExists(id))
            return STATUS.DELETE_SOLD_PRODUCT_ERROR;

        long result = db.delete(TABLE_NAME_PRODUCTS, COL1_PRODUCTS + " = ?", new String[] {String.valueOf(id)});
        db.close();
        if(result == 0) {
            return STATUS.DATABASE_ERROR;
        }

        if(productListReference != null) {
            for(int i = 0; i < productListReference.size();i++) {
                if(productListReference.get(i).getId() == id){
                    productListReference.remove(i);
                    break;
                }
            }
        }
        return STATUS.SUCCESSFUL;
    }

    public boolean isProductSaleExists (int id) {
        //not sure
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL2_SALES + " FROM " + TABLE_NAME_SALES + " WHERE " + COL2_SALES + " = " + String.valueOf(id),null);
        data.moveToNext();
        if(data.getCount() == 0)
            return false;
        return true;
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_SALES,null,null);
        db.delete(TABLE_NAME_PRODUCTS, null, null);
        if(productListReference != null)
            productListReference.clear();

        db.close();
    }


    public static void deleteDatabase(Context mContext) {
        mContext.deleteDatabase(DATABASE_NAME);
    }

}