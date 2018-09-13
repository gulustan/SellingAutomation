package com.example.gulus.android2;

public class Model {
    private int id;
    private String name;
    private String provider;
    private String category;
    private float price;
    private int stockNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStockNum() {
        return stockNum;
    }

    public void setStockNum(int stockNum) {
        this.stockNum = stockNum;
    }

    public Model(int id, String name, String provider, String category, float price, int stockNum){
        this.id = id;
        this.name = name;
        this.provider = provider;
        this.category = category;
        this.price = price;
        this.stockNum = stockNum;

    }
}
