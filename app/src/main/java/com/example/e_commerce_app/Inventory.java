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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce_app.db.AppDatabase;
import com.example.e_commerce_app.db.ECommerceDAO;

import java.util.List;

public class Inventory extends AppCompatActivity implements ProductAdapter.OnItemClickListener{
    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    TextView mTextViewProductsTitle;

    Button mButtonAddProduct;

    RecyclerView mRecyclerViewProducts;

    private ECommerceDAO mECommerceDAO;

    private List<Product> mProductsList;
    private ProductAdapter mProductAdapter;
    private User mUser;
    private int mUserId;

    private Product mProduct;
    private Product mSelectedProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getDatabase();

        mProductsList = mECommerceDAO.getAllProducts();

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

        mTextViewProductsTitle = findViewById(R.id.textViewProductsTitle);

        mButtonAddProduct = findViewById(R.id.buttonAddProduct);

        mRecyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        mRecyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        mProductAdapter = new ProductAdapter(mProductsList, this::onItemClick, this, mECommerceDAO, this::onDeleteClick);
        mRecyclerViewProducts.setAdapter(mProductAdapter);



        mButtonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog();
            }
        });
    }

    public void refreshDisplay(){
        mProductsList = mECommerceDAO.getAllProducts();

        mProductAdapter = new ProductAdapter(mProductsList, this::onItemClick, this, mECommerceDAO, this::onDeleteClick);
        mRecyclerViewProducts.setAdapter(mProductAdapter);

    }

    private void onDeleteClick(int position) {
        showDeleteConfirmationDialog(position);
    }

    private void showEditProductDialog() {
        if(mSelectedProduct == null){
            Toast.makeText(this, "No product selected", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_product, null);

        // Reference your views in the dialog layout
        EditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        EditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);
        EditText editTextProductQuantity = dialogView.findViewById(R.id.editTextProductQuantity);

        // Set initial values from the user object
        editTextProductName.setText(mSelectedProduct.getProductName());
        editTextProductPrice.setText(String.valueOf(mSelectedProduct.getProductPrice()));
        editTextProductQuantity.setText(String.valueOf(mSelectedProduct.getProductQuantity()));

        builder.setView(dialogView)
                .setTitle("Edit Product")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the "Save" button click
                        String newProductName = editTextProductName.getText().toString();
                        String newProductPrice = editTextProductPrice.getText().toString();
                        String newProductQuantity = editTextProductQuantity.getText().toString();

                        // Update the user object or perform any necessary actions
                        mSelectedProduct.setProductName(newProductName);
                        mSelectedProduct.setProductPrice(Double.valueOf(newProductPrice));
                        mSelectedProduct.setProductQuantity(Integer.valueOf(newProductQuantity));

                        mECommerceDAO.update(mSelectedProduct);
                        // Update other user information

                        // Refresh the RecyclerView to reflect the changes
                        refreshDisplay();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the "Cancel" button click
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_product, null);

       // EditText editTextProductId = dialogView.findViewById(R.id.editTextProductId);
        EditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        EditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);
        EditText editTextProductQuantity = dialogView.findViewById(R.id.editTextProductQuantity);

        builder.setView(dialogView)
                .setTitle("Add Product")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the "Save" button click
                        //String newProductId = editTextProductId.getText().toString();
                        String newProductName = editTextProductName.getText().toString();
                        String newProductPrice = editTextProductPrice.getText().toString();
                        String newProductQuantity = editTextProductQuantity.getText().toString();
                        // Get other updated values

                        if(mECommerceDAO.getProductByProductName(newProductName) != null){
                            Toast.makeText(Inventory.this,  newProductName + " already in use.", Toast.LENGTH_SHORT).show();
                        }else {
                            double doubleProductPrice = Double.valueOf(newProductPrice);
                            int intProductQuantity = Integer.valueOf(newProductQuantity);
                            Product newProduct = new Product(newProductName, doubleProductPrice, intProductQuantity);
                            mECommerceDAO.insert(newProduct);
                            Toast.makeText(Inventory.this, newProduct.getProductName() + " added.", Toast.LENGTH_SHORT).show();
                        }

                        // Refresh the RecyclerView to reflect the changes
                        refreshDisplay();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the "Cancel" button click
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void showDeleteConfirmationDialog(int position) {
        Product productToDelete = mProductsList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Product");
        builder.setMessage("Are you sure you want to delete " + productToDelete.getProductName());

        // Add buttons for confirmation
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mECommerceDAO.delete(productToDelete);
                Toast.makeText(Inventory.this, productToDelete.getProductName() + " deleted", Toast.LENGTH_SHORT).show();
                refreshDisplay();
            }
        });

        // Add button for cancellation
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled, do nothing
            }
        }).show();
    }

    private void getDatabase() {
        mECommerceDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getECommerceDAO();
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, Inventory.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

    @Override
    public void onItemClick(int productId) {
        mSelectedProduct = mECommerceDAO.getProductById(productId);
        showEditProductDialog();
    }
}