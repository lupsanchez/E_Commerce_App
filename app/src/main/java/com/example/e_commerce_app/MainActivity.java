package com.example.e_commerce_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Dao;
import androidx.room.Room;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES_KEY = "com.example.e_commerce_app.PREFERENCES_KEY";
    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    private TextView mMainAppNameDisplay;

    private TextView mLoginDisplayText;

    private EditText mEditTextUsername;
    private EditText mEditTextPassword;

    private Button mButtonLogin;
    private Button mButtonSignup;

    private ECommerceDAO mECommerceDAO;

    private int mUserId = -1;
    private String mUsername;
    private String mPassword;
    private User mUser;

    private SharedPreferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();

        checkForProducts();

        checkForUser();

        //addUserToPreference(mUserId);

        //loginUser(mUserId);

        wireUpDisplay();

    }

    public void wireUpDisplay(){
        mMainAppNameDisplay = findViewById(R.id.mainAppNameDisplay);
        mLoginDisplayText = findViewById(R.id.loginTextDisplay);

        mEditTextUsername = findViewById(R.id.editTextUsername);
        mEditTextPassword = findViewById(R.id.editTextPassword);

        mButtonLogin = findViewById(R.id.buttonLogin);
        mButtonSignup = findViewById(R.id.buttonSignUp);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getValuesFromDisplay();
               if(checkForUserInDatabase()){
                    if(!validatePassword()){
                        Toast.makeText(MainActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }else {
                        mEditTextPassword.setText("");
                        Intent intent = LoginActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                        startActivity(intent);

                    }
               }
            }
        });

        mButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SignUpActivity.intentFactory(getApplicationContext());
                startActivity(intent);
                mEditTextPassword.setText("");
            }
        });
    }

    private boolean validatePassword() {
        return mUser.getPassword().equals(mPassword);
    }

    private void loginUser(int userId) {
        mUser = mECommerceDAO.getUserByUserId(userId);

        invalidateOptionsMenu();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser != null){
            MenuItem item = menu.findItem(R.id.menuLogout);
            item.setTitle(mUser.getUserName());
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private void getDatabase(){
        mECommerceDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getECommerceDAO();
    }

    private void getValuesFromDisplay(){
        mUsername = mEditTextUsername.getText().toString();
        mPassword = mEditTextPassword.getText().toString();
    }

    private boolean checkForUserInDatabase(){
        mUser = mECommerceDAO.getUserByUsername(mUsername);
        if(mUser == null){
            Toast.makeText(this, "no user " + mUsername + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void checkForProducts(){
        List<Product> productList = mECommerceDAO.getAllProducts();
        if (productList.size() <= 0) {
            Product item1 = new Product("Item 1", 9.99, 50);
            Product item2 = new Product("Item 2", 19.99, 50);
            Product item3 = new Product("Item 3", 29.99, 50);
            Product item4 = new Product("Item 4", 39.99, 50);
            Product item5 = new Product("Item 5", 49.99, 50);
            mECommerceDAO.insert(item1, item2, item3, item4, item5);
        }
    }

    private void checkForUser() {
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if (mUserId != -1) {
            return;
        }

//        if(mPreferences == null){
//            getPrefs();
//        }
//
//        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        SharedPreferences preferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        if (mUserId != -1) {
            return;
        }

        List<User> users = mECommerceDAO.getAllUsers();
        if (users.size() <= 0) {
            User defaultUser = new User("lsanchez", "pass123", true);
            mECommerceDAO.insert(defaultUser);

            int defaultUserId = mECommerceDAO.getUserByUsername(defaultUser.getUserName()).getUserId();

            Cart defaultCart = new Cart(defaultUserId);

            long defaultCartId = mECommerceDAO.insert(defaultCart);

            defaultUser = mECommerceDAO.getUserByUserId(defaultCart.getUserId());

            defaultUser.setCurrentCartId((int) defaultCartId);

            mECommerceDAO.update(defaultUser);

        }
    }

//    private void clearUserFromPref(){
//        addUserToPreference(-1);
//    }
//
//    private void clearUserFromIntent(){
//        getIntent().putExtra(USER_ID_KEY, -1);
//    }

//    private void addUserToPreference(int userId){
//        if(mPreferences == null){
//            getPrefs();
//        }
//        SharedPreferences.Editor editor = mPreferences.edit();
//        editor.putInt(USER_ID_KEY, userId);
//    }
//
//    private void getPrefs() {
//        SharedPreferences preferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
//    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}