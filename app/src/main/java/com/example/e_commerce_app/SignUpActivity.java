package com.example.e_commerce_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

public class SignUpActivity extends AppCompatActivity {

    private TextView mSignUpDisplayText;

    private EditText mEditTextNewUsername;
    private EditText mEditTextNewPassword;
    private EditText mEditTextReEnterPassword;

    private Button mButtonSignUpSUP;
    private Button mButtonReturnToLogin;

    private ECommerceDAO mECommerceDAO;

    private User mUser;
    private String mUsername;
    private String mPassword;
    private String mReEnteredPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getDatabase();

        wireUpDisplay();
    }



    public void wireUpDisplay(){
        mSignUpDisplayText = findViewById(R.id.signUpDisplayText);

        mEditTextNewUsername = findViewById(R.id.editTextNewUsername);
        mEditTextNewPassword = findViewById(R.id.editTextNewPassword);
        mEditTextReEnterPassword = findViewById(R.id.editTextReEnterPassword);

        mButtonSignUpSUP = findViewById(R.id.buttonSignUpSUP);
        mButtonReturnToLogin = findViewById(R.id.buttonReturnToLogin);

        mButtonReturnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        mButtonSignUpSUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(!checkForUserInDatabase()){
                    if(mPassword.equals(mReEnteredPassword)){
                        User newUser = new User(mUsername, mPassword);
                        mECommerceDAO.insert(newUser);

                        int newUserId = mECommerceDAO.getUserByUsername(newUser.getUserName()).getUserId();

                        Cart newCart = new Cart(newUserId);

                        long newCartId = mECommerceDAO.insert(newCart);

                        newUser = mECommerceDAO.getUserByUserId(newCart.getUserId());

                        newUser.setCurrentCartId(Integer.valueOf((int) newCartId));
                        mECommerceDAO.update(newUser);
                        Toast.makeText(SignUpActivity.this, "New user added", Toast.LENGTH_SHORT).show();
                        clearInputFields();
                    }else{
                        Toast.makeText(SignUpActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Username " + mUsername + " already in use", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDatabase(){
        mECommerceDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getECommerceDAO();
    }

    private void getValuesFromDisplay(){
        mUsername = mEditTextNewUsername.getText().toString();
        mPassword = mEditTextNewPassword.getText().toString();
        mReEnteredPassword = mEditTextReEnterPassword.getText().toString();
    }
    private boolean checkForUserInDatabase(){
        return mECommerceDAO.getUserByUsername(mUsername) != null;
    }

    private void clearInputFields() {
        mEditTextNewUsername.setText("");
        mEditTextNewPassword.setText("");
        mEditTextReEnterPassword.setText("");
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
    }

}
