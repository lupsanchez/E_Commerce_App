package com.example.e_commerce_app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.e_commerce_app.db.AppDatabase;

@Entity(tableName = AppDatabase.PRODUCT_TABLE)
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int mProductId;

    private String mProductName;
    private double mProductPrice;
    private int mProductQuantity;

    public Product(int productId, String productName, double productPrice, int productQuantity) {
        mProductId = productId;
        mProductName = productName;
        mProductPrice = productPrice;
        mProductQuantity = productQuantity;
    }

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        mProductId = productId;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public double getProductPrice() {
        return mProductPrice;
    }

    public void setProductPrice(double productPrice) {
        mProductPrice = productPrice;
    }

    public int getProductQuantity() {
        return mProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        mProductQuantity = productQuantity;
    }
}