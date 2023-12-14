package com.example.e_commerce_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

import java.util.List;

public class MyCart extends AppCompatActivity implements CartAdapter.OnItemClickListener {
    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    private TextView mTextViewMyCartTitle;

    private RecyclerView mRecyclerViewCartItems;

    private TextView mTextViewCartTotal;

    private Button mButtonPurchase;

    private ECommerceDAO mECommerceDAO;

    private List<Product> mProductsList;
    private CartAdapter mCartAdapter;
    private User mUser;
    private int mUserId;

    private Product mProduct;
    private Product mSelectedProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getDatabase();

        mProductsList = mECommerceDAO.getAllProducts();

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mECommerceDAO.getUserByUserId(mUserId);

        wireUpdDisplay();
    }

    private void wireUpdDisplay() {
        mTextViewMyCartTitle = findViewById(R.id.textViewMyCartTitle);

        mRecyclerViewCartItems = findViewById(R.id.recyclerViewCartItems);
        mRecyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));

        mCartAdapter = new CartAdapter(mProductsList, this::onItemClick, this, mECommerceDAO);

        mTextViewCartTotal = findViewById(R.id.textViewCartTotal);

        mButtonPurchase = findViewById(R.id.buttonPurchase);

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

    private void getDatabase() {
        mECommerceDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getECommerceDAO();
    }

     //@Override
    public void onItemClick(int productId){
        mSelectedProduct = mECommerceDAO.getProductById(productId);
        showEditCartItemDialog();
    }

    private void showEditCartItemDialog() {

    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MyCart.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}