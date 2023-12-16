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

public class Users extends AppCompatActivity implements UserAdapter.OnItemClickListener{
    private static final String USER_ID_KEY = "com.example.e_commerce_app.USER_ID_KEY";
    private TextView mTextViewUserTile;

    private Button mButtonAddNewUser;

    private RecyclerView mRecyclerViewUsers;

    private ECommerceDAO mECommerceDAO;

    private List<User> mUsers;
    private UserAdapter userAdapter;
    private User mUser;
    private int mUserId;

    private User mSelectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getDatabase();

        mUsers = mECommerceDAO.getAllUsers();

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

        mTextViewUserTile = findViewById(R.id.textViewUsersTitle);

        mButtonAddNewUser = findViewById(R.id.buttonAddUser);

        mRecyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(mUsers, this, this, mECommerceDAO, this::onDeleteClick);
        mRecyclerViewUsers.setAdapter(userAdapter);



        mButtonAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUserDialog();
            }
        });

    }

    private void getDatabase(){
        mECommerceDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getECommerceDAO();
    }

    public void refreshDisplay(){
        mUsers = mECommerceDAO.getAllUsers();

        userAdapter = new UserAdapter(mUsers, this, this, mECommerceDAO, this::onDeleteClick);
        mRecyclerViewUsers.setAdapter(userAdapter);

    }

    @Override
    public void onItemClick(int userId){
        mSelectedUser = mECommerceDAO.getUserByUserId(userId);
        showEditUserDialog();
    }

    private void showAddUserDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_user, null);

        EditText editTextUsername = dialogView.findViewById(R.id.editTextUsername);
        EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);
        CheckBox checkBoxIsAdmin = dialogView.findViewById(R.id.checkBoxIsAdmin);

        builder.setView(dialogView)
                .setTitle("Add User")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the "Save" button click
                        String newUsername = editTextUsername.getText().toString();
                        String newPassword = editTextPassword.getText().toString();
                        boolean newIsAdmin = checkBoxIsAdmin.isChecked();
                        // Get other updated values

                        if(mECommerceDAO.getUserByUsername(newUsername) != null){
                            Toast.makeText(Users.this, "Username" + newUsername + " already in use.", Toast.LENGTH_SHORT).show();
                        }else {
                            User newUser = new User(newUsername, newPassword, newIsAdmin);
                            mECommerceDAO.insert(newUser);

                            int newUserId = mECommerceDAO.getUserByUsername(newUser.getUserName()).getUserId();

                            Cart newCart = new Cart(newUserId);

                            long newCartId = mECommerceDAO.insert(newCart);

                            newUser = mECommerceDAO.getUserByUserId(newCart.getUserId());

                            newUser.setCurrentCartId(Integer.valueOf((int) newCartId));
                            mECommerceDAO.update(newUser);

                            Toast.makeText(Users.this, newUser.getUserName() + " added." + "newCart" + newCartId + " "+ newUser.getCurrentCartId(), Toast.LENGTH_SHORT).show();
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

    private void showEditUserDialog() {
        if(mSelectedUser == null){
            Toast.makeText(this, "No user selected", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_user, null);

        // Reference your views in the dialog layout
        EditText editTextUsername = dialogView.findViewById(R.id.editTextUsername);
        EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);
        CheckBox checkBoxIsAdmin = dialogView.findViewById(R.id.checkBoxIsAdmin);
        // Add other views as needed

        // Set initial values from the user object
        editTextUsername.setText(mSelectedUser.getUserName());
        editTextPassword.setText(mSelectedUser.getPassword());
        if(mSelectedUser.isAdmin()){
            checkBoxIsAdmin.setChecked(true);
        }else{
            checkBoxIsAdmin.setChecked(false);
        }

        builder.setView(dialogView)
                .setTitle("Edit User")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the "Save" button click
                        String newUsername = editTextUsername.getText().toString();
                        String newPassword = editTextPassword.getText().toString();
                        boolean newIsAdmin = checkBoxIsAdmin.isChecked();
                        // Get other updated values

                        // Update the user object or perform any necessary actions
                        mSelectedUser.setUserName(newUsername);
                        mSelectedUser.setPassword(newPassword);
                        mSelectedUser.setAdmin(newIsAdmin);

                        mECommerceDAO.update(mSelectedUser);
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

    //@Override
    public void onDeleteClick(int position) {
        showDeleteConfirmationDialog(position);
    }

    private void showDeleteConfirmationDialog(int position) {
            User userToDelete = mUsers.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete User");
            builder.setMessage("Are you sure you want to delete " + userToDelete.getUserName());

            // Add buttons for confirmation
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mECommerceDAO.delete(userToDelete);
                    Toast.makeText(Users.this, userToDelete.getUserName() + " deleted", Toast.LENGTH_SHORT).show();
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


    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, Users.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}