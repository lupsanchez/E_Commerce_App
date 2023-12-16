package com.example.e_commerce_app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.e_commerce_app.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = AppDatabase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;
    private String mPassword;

    private boolean mAdmin;

    private int mCurrentCartId;

    public User(String userName, String password) {
        mUserName = userName;
        mPassword = password;
        mAdmin = false;
        mCurrentCartId = 0;
    }

    public User(String userName, String password, boolean isAdmin) {
        mUserName = userName;
        mPassword = password;
        mAdmin = isAdmin;
        mCurrentCartId = 0;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean isAdmin() {
        return mAdmin;
    }

    public void setAdmin(boolean admin) {
        mAdmin = admin;
    }

    public int getCurrentCartId() {
        return mCurrentCartId;
    }

    public void setCurrentCartId(int currentCartId) {
        mCurrentCartId = currentCartId;
    }

}
