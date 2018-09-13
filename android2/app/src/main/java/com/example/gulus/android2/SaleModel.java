package com.example.gulus.android2;

import java.util.Date;

public class SaleModel {
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getBuyer() {
        return buyer;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getDate() {
        return date;
    }

    public int getProductId() {
        return productId;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    private int id;
    private String buyer;
    private int quantity;
    private Date date;
    private int productId;

    public SaleModel(int id, int productId, String buyer, int quantity, Date date) {
        this.id = id;
        this.buyer = buyer;
        this.quantity = quantity;
        this.date = date;
        this.productId = productId;
    }


}