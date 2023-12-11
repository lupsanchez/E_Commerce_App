package com.example.e_commerce_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;


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

    private MenuItem mMenuItemAdminSettings;

    private User mUser;
    private int mUserId;

    private SharedPreferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getDatabase();

        wireUpDisplay();

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        updateUserUI(mUserId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        MenuHelper.onCreateOptionsMenu(inflater, menu, mUser);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return MenuHelper.onOptionsItemSelected(this, item, mUser) || super.onOptionsItemSelected(item);
    }

    private void updateUserUI(int userId) {
        mUser = mECommerceDAO.getUserByUserId(userId);

        mLoginTextDisplay.setText("Welcome " + mUser.getUserName().toString() + "!");

        if (mUser != null) {
            // Set the visibility of the admin button based on the user's role
            if (mUser.isAdmin()) {
                mButtonAdminSettings.setVisibility(View.VISIBLE);
                //mMenuItemAdminSettings.setVisible(true);
            } else {
                mButtonAdminSettings.setVisibility(View.GONE);
                //mMenuItemAdminSettings.setVisible(false);
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

        mMenuItemAdminSettings = findViewById(R.id.menuAdminSettings);

        mButtonAdminSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminSettings.intentFactory(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });

    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}
