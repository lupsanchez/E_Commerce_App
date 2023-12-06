package com.example.e_commerce_app.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.e_commerce_app.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "E_COMMERCE_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";

    public abstract ECommerceDAO getECommerceDAO();
}
