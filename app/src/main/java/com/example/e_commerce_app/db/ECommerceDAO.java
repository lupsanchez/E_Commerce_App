package com.example.e_commerce_app.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.e_commerce_app.Cart;
import com.example.e_commerce_app.Product;
import com.example.e_commerce_app.User;

import java.util.List;

@Dao
public interface ECommerceDAO {
    @Insert
    void insert(User... user);

    @Update
    void update(User...user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);

    @Insert
    void insert(Product... product);

    @Update
    void update(Product...product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM " + AppDatabase.PRODUCT_TABLE)
    List<Product> getAllProducts();

    @Query("SELECT * FROM " + AppDatabase.PRODUCT_TABLE + " WHERE mProductName = :productName")
    Product getProductByProductName(String productName);

    @Query("SELECT * FROM " + AppDatabase.PRODUCT_TABLE + " WHERE mProductId = :productId")
    Product getProductById(int productId);

    @Insert
    long[] insert(Cart... cart);

    @Insert
    long insert(Cart cart);

    @Update
    void update(Cart... cart);

    @Delete
    void delete(Cart cart);

    @Query("SELECT * FROM " + AppDatabase.CART_TABLE)
    List<Cart> getAllCarts();

    @Query("SELECT * FROM " + AppDatabase.CART_TABLE + " WHERE mCartId = :cartId")
    Cart getCartById(int cartId);

    @Query("SELECT * FROM " + AppDatabase.CART_TABLE + " WHERE mUserId = :userId AND mCartOrdered = 1")
    List<Cart> getOrderedCartsByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.CART_TABLE + " WHERE mCartOrdered = 1")
    List<Cart> getOrderedCarts();
}
