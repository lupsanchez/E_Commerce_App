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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyCart extends AppCompatActivity implements CartAdapter.OnItemRemoveListener {
    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    private TextView mTextViewMyCartTitle;

    private RecyclerView mRecyclerViewCartItems;

    private TextView mTextViewCartTotal;

    private Button mButtonPlaceOrder;

    private ECommerceDAO mECommerceDAO;

    private List<Product> mProductsList;
    private CartAdapter mCartAdapter;
    private User mUser;
    private int mUserId;

    private Cart mCart;
    private int mCartId;

    private Product mProduct;
    private Product mSelectedProduct;

    private double totalCartAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getDatabase();

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mECommerceDAO.getUserByUserId(mUserId);

        mCart = mECommerceDAO.getCartById(mUser.getCurrentCartId());

        createProductList();

        wireUpdDisplay();
    }



    private void createProductList() {
        // Get the list of product IDs from the cart
        List<Integer> productIds = mCart.getProductIds();


        // Initialize the list to hold products with quantities
        mProductsList = new ArrayList<>();

        // Create a map to store the count of each product ID
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
            Product productWithQuantity = new Product(product.getProductName(), product.getProductPrice(),quantity);
            productWithQuantity.setProductId(productId);
            // Add the product with quantity to the list
            mProductsList.add(productWithQuantity);

            double totalCost = product.getProductPrice() * quantity;
            totalCartAmount += totalCost;

        }
    }

    private void wireUpdDisplay() {
        mTextViewMyCartTitle = findViewById(R.id.textViewMyCartTitle);

        mRecyclerViewCartItems = findViewById(R.id.recyclerViewCartItems);
        mRecyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));

        mCartAdapter = new CartAdapter(mProductsList, this::onRemoveClick, this, mECommerceDAO);
        mRecyclerViewCartItems.setAdapter(mCartAdapter);

        mTextViewCartTotal = findViewById(R.id.textViewCartTotal);

        mTextViewCartTotal.setText("Total Cost: $ " + String.format("%.2f",mCart.getCartTotalCost()));

        mButtonPlaceOrder = findViewById(R.id.buttonPurchase);

        mButtonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPlaceOrder(v);
            }
        });

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

    private void getDatabase() {
        mECommerceDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getECommerceDAO();
    }

    public void onClickPlaceOrder(View view) {
        updateInventory();
        markCartAsOrdered();
        createNewCart();
        displayOrderPlacedMessage();
        refreshDisplay();
    }

    private void refreshDisplay() {
        mCart = mECommerceDAO.getCartById(mUser.getCurrentCartId());

        createProductList();

        wireUpdDisplay();
    }

    private void updateInventory() {
        for (Product product : mProductsList) {
            Product inventory = mECommerceDAO.getProductById(product.getProductId());
            int newQty = inventory.getProductQuantity() - product.getProductQuantity();
            inventory.setProductQuantity(newQty);
            mECommerceDAO.update(inventory);
        }
    }

    private void createNewCart(){
        Cart newCart = new Cart(mUserId);

        long newCartId = mECommerceDAO.insert(newCart);

        mUser.setCurrentCartId((int) newCartId);

        mECommerceDAO.update(mUser);
    }

    private void markCartAsOrdered() {
        mCart.setCartOrdered(true);
        mECommerceDAO.update(mCart);
    }

    private void displayOrderPlacedMessage() {
        Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
    }

     //@Override
     public void onRemoveClick(int position){
         showRemoveConfirmationDialog(position);
     }

    private void showRemoveConfirmationDialog(int position) {
        Product product = mProductsList.get(position);
        int productId = product.getProductId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Item");
        builder.setMessage("Are you sure you want to remove item #: " + product.getProductName() + "?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                List<Integer> productIds = mCart.getProductIds();

                Iterator<Integer> iterator = productIds.iterator();
                while (iterator.hasNext()) {
                    Integer cartProductID = iterator.next();
                    if (cartProductID == productId) {
                        iterator.remove();
                    }
                }

                mCart.setProductIds(productIds);

                mECommerceDAO.update(mCart);

                refreshDisplay();

                Toast.makeText(MyCart.this, "Item #: " + product.getProductName()+ " has been removed", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }

        }).show();
    }

    private void showEditCartItemDialog() {

    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MyCart.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}