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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

public class ProductPage extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    private static final String PRODUCT_ID_KEY = "com.example.e_commerce_app.PRODUCT_ID_KEY";

    private TextView mTextViewProductNameTitle;

    private ImageView mImageViewProductImage;

    private TextView mTextViewProductId;
    private TextView mTextViewProductQuantity;
    private TextView mTextViewProductPrice;
    private TextView mTextViewProductsInStock;

    private EditText mEditTextNumOfProducts;

    private Button mButtonAddToCart;

    private ECommerceDAO mECommerceDAO;

    private User mUser;
    private Integer mUserId;

    private Product mProduct;
    private int mProductId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getDatabase();

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mECommerceDAO.getUserByUserId(mUserId);

        mProductId = getIntent().getIntExtra(PRODUCT_ID_KEY, -1);
        mProduct = mECommerceDAO.getProductById(mProductId);

        wireUpDisplay();
    }

    private void wireUpDisplay() {
        mTextViewProductNameTitle = findViewById(R.id.textViewProductTitle);

        mImageViewProductImage = findViewById(R.id.imageViewProductImage);

        mTextViewProductsInStock = findViewById(R.id.textViewProductsInStock);
        //mTextViewProductId = findViewById(R.id.textViewProductId);
        mTextViewProductQuantity = findViewById(R.id.textViewProductQuantity);
        mTextViewProductPrice = findViewById(R.id.textViewProductPageProductPrice);
        mEditTextNumOfProducts = findViewById(R.id.editTextNumOfProducts);

        mButtonAddToCart = findViewById(R.id.buttonAddToCart);

        mTextViewProductNameTitle.setText("ID: "+ mProductId + " " + mProduct.getProductName());
        mTextViewProductPrice.setText("Price: $" + String.valueOf(mProduct.getProductPrice()));
        mTextViewProductsInStock.setText("In Stock: " + String.valueOf(mProduct.getProductQuantity()));

        mEditTextNumOfProducts.setText("1");

        mButtonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtyAddingToCart = Integer.valueOf(String.valueOf(mEditTextNumOfProducts.getText()));
                if(qtyAddingToCart > mProduct.getProductQuantity()){
                    Toast.makeText(ProductPage.this, "Not enough items in stock", Toast.LENGTH_SHORT).show();
                } else{
                    Cart cart = mECommerceDAO.getCartById(mUser.getCurrentCartId());

                    // Ensure that the cart is not null before proceeding
                    if (cart != null) {
                        // Add the current product to the cart
                        cart.addProductId(mProductId);

                        // Add additional quantities of the product to the cart
                        for (int i = 1; i < qtyAddingToCart; i++) {
                            cart.addProductId(mProductId);
                        }

                        // Update the cart in the DAO or wherever it's stored
                        mECommerceDAO.update(cart);

                        Toast.makeText(ProductPage.this, qtyAddingToCart + " " + mProduct.getProductName() + " added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        // If the cart is null, you may want to handle this case (e.g., create a new cart)
                        // Alternatively, display an error message
                        Toast.makeText(ProductPage.this, mUser.getUserName()+" userId: "+ mUser.getUserId() + "CartId " + mUser.getCurrentCartId() + "Cart not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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

    public static Intent intentFactory(Context context, int userId, int productId){
        Intent intent = new Intent(context, ProductPage.class);
        intent.putExtra(USER_ID_KEY, userId);
        intent.putExtra(PRODUCT_ID_KEY,productId);
        return intent;
    }
}