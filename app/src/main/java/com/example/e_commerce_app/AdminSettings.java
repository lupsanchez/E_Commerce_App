package com.example.e_commerce_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

public class AdminSettings extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    private TextView mTextViewAdminLogin;

    private Button mButtonCustomerLogin;
    private Button mButtonInventory;
    private Button mButtonUsers;
    private Button mButtonOrders;

    private ECommerceDAO mECommerceDAO;

    private User mUser;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        getDatabase();

        userId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mECommerceDAO.getUserByUserId(userId);

        wireUpDisplay();
    }

    private void wireUpDisplay() {
        mTextViewAdminLogin = findViewById(R.id.textDisplayAdminLogin);

        mButtonCustomerLogin = findViewById(R.id.buttonCustomerLogin);
        mButtonInventory = findViewById(R.id.buttonInventory);
        mButtonOrders = findViewById(R.id.buttonOrders);
        mButtonUsers = findViewById(R.id.buttonUsers);

        if (mUser != null) {
            // Use mUser safely
            mTextViewAdminLogin.setText("Welcome " + mUser.getUserName().toString() + "!");
        }

        mButtonCustomerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = LoginActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                    startActivity(intent);
            }
        });

        mButtonUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Users.intentFactory(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });


    }

    private void getDatabase(){
        mECommerceDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getECommerceDAO();
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, AdminSettings.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}