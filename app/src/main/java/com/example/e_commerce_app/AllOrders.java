package com.example.e_commerce_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

import java.util.List;

public class AllOrders extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    private TextView mTextViewAllOrdersTitle;
    private TextView mTextViewAllOrdersListings;

    private ECommerceDAO mECommerceDAO;

    private List<User> mUsers;
    private User mUser;
    private int mUserId;

    private int mCartId;

    private List<Cart> mCartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getDatabase();

        mUsers = mECommerceDAO.getAllUsers();

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mECommerceDAO.getUserByUserId(mUserId);

        wireUpdDisplay();

        refreshDisplay();
    }

    private void wireUpdDisplay() {
        mTextViewAllOrdersTitle = findViewById(R.id.textViewAllOrdersTitle);

        mTextViewAllOrdersListings = findViewById(R.id.textViewAllOrdersListings);


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

    private void getDatabase(){
        mECommerceDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getECommerceDAO();
    }

    private void refreshDisplay(){
        mCartList = mECommerceDAO.getAllCarts();

        if(mCartList.size() <= 0) {
            mTextViewAllOrdersListings.setText("No Carts");
        }

        StringBuilder sb = new StringBuilder();

        for(Cart cart : mCartList){
            sb.append("\nCartId: " + cart.getCartId());
            sb.append("\nUserId: " + cart.getUserId());
            sb.append("\nCartSize: " + cart.getProductIds().size());

            List<Integer> productIds = cart.getProductIds();
            sb.append("ProductIds:\n");

            for (Integer productId : productIds) {
                sb.append("- ").append(productId).append("\n");
            }
            sb.append("\n");
            sb.append("=-=-=-=-=-=-=-=-");
            sb.append("\n");
        }
        mTextViewAllOrdersListings.setText(sb.toString());
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, AllOrders.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}