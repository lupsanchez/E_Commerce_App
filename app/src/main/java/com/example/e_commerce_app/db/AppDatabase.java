package com.example.e_commerce_app.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.e_commerce_app.Cart;
import com.example.e_commerce_app.Converters;
import com.example.e_commerce_app.User;
import com.example.e_commerce_app.Product;

@Database(entities = {User.class, Product.class, Cart.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "E_COMMERCE_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String PRODUCT_TABLE = "PRODUCT_TABLE";
    public static final String CART_TABLE = "CART_TABLE";

    public abstract ECommerceDAO getECommerceDAO();
}
