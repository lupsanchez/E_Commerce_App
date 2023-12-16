package com.example.e_commerce_app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.e_commerce_app.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = AppDatabase.CART_TABLE)
public class Cart {

    @PrimaryKey(autoGenerate = true)
    private int mCartId;

    private int mUserId;

    private List<Integer> mProductIds;

    private boolean mCartOrdered;

    private boolean mCartCancelled;

    public Cart(int userId){
        mUserId = userId;
        mProductIds = new ArrayList<>();
        mCartCancelled = false;
        mCartOrdered = false;
    }

    public int getCartId() {
        return mCartId;
    }

    public void setCartId(int cartId) {
        mCartId = cartId;
    }

    public List<Integer> getProductIds() {
        return mProductIds;
    }

    public void setProductIds(List<Integer> productIds) {
        mProductIds = productIds;
    }

    public int getUserId() {
        return mUserId;
    }

    public void addProductId(int productId) {
        mProductIds.add(productId);
    }

    public void removeProductId(int productId) {
        mProductIds.remove(Integer.valueOf(productId));
    }

    public boolean isCartCancelled() {
        return mCartCancelled;
    }

    public void setCartCancelled(boolean cartCancelled) {
        mCartCancelled = cartCancelled;
    }

    public boolean isCartOrdered() {
        return mCartOrdered;
    }

    public void setCartOrdered(boolean cartOrdered) {
        mCartOrdered = cartOrdered;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }
}
