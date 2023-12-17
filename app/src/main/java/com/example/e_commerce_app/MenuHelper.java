package com.example.e_commerce_app;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.e_commerce_app.db.ECommerceDAO;

import java.util.List;

public class MenuHelper {

    public static void onCreateOptionsMenu(MenuInflater inflater, Menu menu, User user, ECommerceDAO mECommerceDAO) {
        List<Product> productList = mECommerceDAO.getAllProducts();
        inflater.inflate(R.menu.user_menu, menu);

        MenuItem username = menu.findItem(R.id.menuUsername);
        username.setTitle(user.getUserName());

        MenuItem searchItem = menu.findItem(R.id.menuActionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        MenuItem mMenuItemAdminSettings = menu.findItem(R.id.menuAdminSettings);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ProductListingAdapter adapter = new ProductListingAdapter(productList);

                adapter.getFilter().filter(newText);
                return false;
            }
        });

        boolean isAdmin = user.isAdmin();
        mMenuItemAdminSettings.setVisible(isAdmin);
    }

    private static boolean isAdminUser(Context context, int userId) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return preferences.getBoolean("is_admin", false);
    }

    public static boolean onOptionsItemSelected(Context context, MenuItem item, User user) {
        int itemId = item.getItemId();

        if (itemId == R.id.menuShoppingCart) {
            openCart(context, user);
        } else if (itemId == R.id.menuMyOrders) {
            openMyOrders(context, user);
            return true;
        } else if (itemId == R.id.menuAdminSettings) {
            openAdminSettings(context, user);
            return true;
        } else if (itemId == R.id.menuLogout) {
            logoutUser(context);
            return true;
        } else if (itemId == R.id.menuShoppProducts){
            openAllProductsListed(context, user);
        }
        else if (itemId == R.id.menuMyOrders){

        }
        return false;
    }

    private static void returnToLoginScreen(Context context, User user){
        Intent intent = LoginActivity.intentFactory(context, user.getUserId());
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private static void openMyOrders(Context context, User user){
        Intent intent = MyOrders.intentFactory(context, user.getUserId());
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private static void openAdminSettings(Context context, User user){
        Intent intent = AdminSettings.intentFactory(context, user.getUserId());
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private static void openCart(Context context, User user){
        Intent intent = MyCart.intentFactory(context, user.getUserId());
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private static void openAllProductsListed(Context context, User user){
        Intent intent = AllProductsListed.intentFactory(context, user.getUserId());
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private static void logoutUser(Context context) {
        // Clear user-related information (e.g., from SharedPreferences)
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        // Add buttons for confirmation
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
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
}
