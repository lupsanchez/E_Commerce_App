package com.example.e_commerce_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

public class LoginActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    private TextView mLoginTextDisplay;

    private Button mButtonViewInventory;
    private Button mButtonViewOrders;
    private Button mButtonAdminSettings;

    private ECommerceDAO mECommerceDAO;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getDatabase();

        wireUpDisplay();

        int userId = getIntent().getIntExtra(USER_ID_KEY, -1);
        updateUserUI(userId);

    }

    private void updateUserUI(int userId) {
        mUser = mECommerceDAO.getUserByUserId(userId);

        mLoginTextDisplay.setText("Welcome " + mUser.getUserName().toString() + "!");

        if (mUser != null) {
            // Set the visibility of the admin button based on the user's role
            if (mUser.isAdmin()) {
                mButtonAdminSettings.setVisibility(View.VISIBLE);
            } else {
                mButtonAdminSettings.setVisibility(View.GONE);
            }
        }
    }

    private void getDatabase(){
        mECommerceDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getECommerceDAO();
    }

    public void wireUpDisplay(){
        mLoginTextDisplay = findViewById(R.id.loginTextDisplay);

        mButtonViewOrders = findViewById(R.id.buttonViewOrders);
        mButtonViewInventory = findViewById(R.id.buttonViewInventory);
        mButtonAdminSettings = findViewById(R.id.buttonAdminSettings);

    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}
