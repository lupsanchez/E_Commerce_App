package com.example.e_commerce_app.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.e_commerce_app.User;
import com.example.e_commerce_app.Product;

@Database(entities = {User.class, Product.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "E_COMMERCE_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String PRODUCT_TABLE = "PRODUCT_TABLE";

    public abstract ECommerceDAO getECommerceDAO();
}
