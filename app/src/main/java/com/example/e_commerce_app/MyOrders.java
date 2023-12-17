package com.example.e_commerce_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrders extends AppCompatActivity implements OrderAdapter.OnCancelClickListener{
    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    private TextView mTextViewMyOrdersTitle;
    private RecyclerView mRecyclerViewMyOrders;

    private ECommerceDAO mECommerceDAO;

    private List<User> mUsers;
    private User mUser;
    private int mUserId;

    private int mCartId;

    private List<Cart> mOrdersList;
    private List<Product> mProductsList;
    private OrderAdapter mOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getDatabase();

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mECommerceDAO.getUserByUserId(mUserId);

        mOrdersList = (List<Cart>) mECommerceDAO.getOrderedCartsByUserId(mUserId);

        wireUpdDisplay();

        refreshDisplay();
    }

    private List<Product> getProductsListWithQuantities(List<Integer> productIds) {
        List<Product> productsList = new ArrayList<>();
        Map<Integer, Integer> productCounts = new HashMap<>();

        // Iterate through the product IDs to count quantities
        for (int productId : productIds) {
            // Increment the count for each product ID
            productCounts.put(productId, productCounts.getOrDefault(productId, 0) + 1);
        }

        // Iterate through the product counts to create Product objects with quantities
        for (Map.Entry<Integer, Integer> entry : productCounts.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            // Get the product details from the database
            Product product = mECommerceDAO.getProductById(productId);

            // Create a new Product object with the quantity
            Product productWithQuantity = new Product(product.getProductName(), product.getProductPrice(), quantity);
            productWithQuantity.setProductId(productId);

            // Add the product with quantity to the list
            productsList.add(productWithQuantity);
        }

        return productsList;
    }

    private void wireUpdDisplay() {
        mTextViewMyOrdersTitle = findViewById(R.id.textViewMyOrdersTitle);

        mRecyclerViewMyOrders = findViewById(R.id.recyclerViewMyOrders);
        mRecyclerViewMyOrders.setLayoutManager(new LinearLayoutManager(this));

        mOrderAdapter = new OrderAdapter(mOrdersList, this, mECommerceDAO, this::onCancelClick);
        mRecyclerViewMyOrders.setAdapter(mOrderAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        MenuHelper.onCreateOptionsMenu(inflater, menu, mUser, mECommerceDAO);
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
        mOrdersList = (List<Cart>) mECommerceDAO.getOrderedCartsByUserId(mUserId);

        mOrderAdapter = new OrderAdapter(mOrdersList, this, mECommerceDAO, this);
        mRecyclerViewMyOrders.setAdapter(mOrderAdapter);
    }

    public void onCancelClick(int position){
        showCancelConfirmationDialog(position);
    }

    private void showCancelConfirmationDialog(int position) {
        Cart cartToCancel = mOrdersList.get(position);
        mProductsList = getProductsListWithQuantities(cartToCancel.getProductIds());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Order");
        builder.setMessage("Are you sure you want to cancel order #: " + cartToCancel.getCartId() + "?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateInventory(mProductsList);
                markCartAsCancelled(cartToCancel);
                Toast.makeText(MyOrders.this, "Order #: " + cartToCancel.getCartId() + " has been cancelled", Toast.LENGTH_SHORT).show();

                refreshDisplay();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }

        }).show();
    }

    private void markCartAsCancelled(Cart cart) {
        cart.setCartCancelled(true);
        mECommerceDAO.update(cart);
    }

    private void updateInventory(List<Product> productsList) {
        for (Product product : productsList) {
            Product inventory = mECommerceDAO.getProductById(product.getProductId());
            int newQty = inventory.getProductQuantity() + product.getProductQuantity();
            inventory.setProductQuantity(newQty);
            mECommerceDAO.update(inventory);
        }
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MyOrders.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}