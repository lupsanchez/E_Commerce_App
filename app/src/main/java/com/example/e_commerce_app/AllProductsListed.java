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
import android.widget.TextView;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

import java.util.List;

public class AllProductsListed extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    private TextView mTextViewAllProductsTitle;
    private RecyclerView mRecyclerViewProducts;

    private ECommerceDAO mECommerceDAO;

    private List<Product> mProductList;
    private ProductListingAdapter mProductListingAdapter;
    private Product mProduct;
    private int mProductId;

    private Product mSelectedProduct;

    private User mUser;
    private int mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products_listed);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getDatabase();

        mProductList = mECommerceDAO.getAllProducts();

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mECommerceDAO.getUserByUserId(mUserId);

        wireUpdDisplay();
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

    private void wireUpdDisplay() {
        mTextViewAllProductsTitle = findViewById(R.id.textViewAllProductsTitle);

        mRecyclerViewProducts = findViewById(R.id.recyclerViewProductListings);
        mRecyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        mProductListingAdapter = new ProductListingAdapter(mProductList, this::onItemClick, this, mECommerceDAO);
        mRecyclerViewProducts.setAdapter(mProductListingAdapter);
    }

    private void getDatabase() {
        mECommerceDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getECommerceDAO();
    }

    public void refreshDisplay(){
        mProductList = mECommerceDAO.getAllProducts();

        mProductListingAdapter = new ProductListingAdapter(mProductList, this::onItemClick, this, mECommerceDAO);
        mRecyclerViewProducts.setAdapter(mProductListingAdapter);

    }

    //@Override
    public void onItemClick(int productId){
        mSelectedProduct = mECommerceDAO.getProductById(productId);
        Intent intent = ProductPage.intentFactory(getApplicationContext(), mUser.getUserId(), mSelectedProduct.getProductId());
        startActivity(intent);
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, AllProductsListed.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}