package com.example.e_commerce_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_app.db.ECommerceDAO;

import org.w3c.dom.Text;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> mProductList;
    private CartAdapter.OnItemClickListener mOnItemClickListener;
    private Context context;

    private ECommerceDAO mECommerceDAO;

    public CartAdapter(List<Product> productList, CartAdapter.OnItemClickListener onItemClickListener, Context context, ECommerceDAO ECommerceDAO) {
        this.mProductList = productList;
        this.mOnItemClickListener = onItemClickListener;
        this.context = context;
        this.mECommerceDAO = ECommerceDAO;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cartView = inflater.inflate(R.layout.item_cart, parent, false);

        return new CartViewHolder(cartView);
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        TextView mTextViewPId;
        TextView mTextViewProductName;
        TextView mTextViewProductPrice;
        TextView mTextViewProductQty;
        TextView mTextViewTotalCost;

        public CartViewHolder(View itemView){
            super(itemView);

            mTextViewPId = itemView.findViewById(R.id.textViewPId);
            mTextViewProductName = itemView.findViewById(R.id.textViewProductName);
            mTextViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            mTextViewProductQty = itemView.findViewById(R.id.textViewProductQuantity);
            mTextViewTotalCost = itemView.findViewById(R.id.textViewTotalCost);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Product product = mProductList.get(position);

        holder.mTextViewPId.setText(String.valueOf(product.getProductId()));
        holder.mTextViewProductName.setText(String.valueOf(product.getProductName()));
        holder.mTextViewProductPrice.setText(String.valueOf(product.getProductPrice()));
        holder.mTextViewProductQty.setText(String.valueOf(product.getProductQuantity()));
        double totalCost = product.getProductPrice()*product.getProductQuantity();
        holder.mTextViewTotalCost.setText(String.valueOf(totalCost));

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int productId);
    }


}
