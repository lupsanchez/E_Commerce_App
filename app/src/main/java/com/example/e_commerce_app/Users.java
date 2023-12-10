package com.example.e_commerce_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

import java.util.List;

public class Users extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    private TextView mTextViewUserTile;

    private Button mButtonAddNewUser;

    private RecyclerView mRecyclerViewUsers;

    private ECommerceDAO mECommerceDAO;

    private List<User> mUsers;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        getDatabase();

        mUsers = mECommerceDAO.getAllUsers()
;
        wireUpdDisplay();

    }

    private void wireUpdDisplay() {

        mTextViewUserTile = findViewById(R.id.textViewUsersTitle);

        mButtonAddNewUser = findViewById(R.id.buttonAddUser);

        mRecyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(mUsers);
        mRecyclerViewUsers.setAdapter(userAdapter);

        //refreshDisplay();


    }

    private void getDatabase(){
        mECommerceDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getECommerceDAO();
    }

    private void refreshDisplay(){
        mUsers = mECommerceDAO.getAllUsers();

        userAdapter = new UserAdapter(mUsers);
        mRecyclerViewUsers.setAdapter(userAdapter);

    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, Users.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

}