package com.example.e_commerce_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_app.db.ECommerceDAO;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> mProductList;
    private OnItemRemoveListener mOnItemRemoveListener;
    private Context context;

    private ECommerceDAO mECommerceDAO;

    public CartAdapter(List<Product> productList, OnItemRemoveListener onItemRemoveListener, Context context, ECommerceDAO ECommerceDAO) {
        this.mProductList = productList;
        this.mOnItemRemoveListener = onItemRemoveListener;
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

        Button mButtonCartRemoveItem;

        public CartViewHolder(View itemView){
            super(itemView);

            mTextViewPId = itemView.findViewById(R.id.textViewPId);
            mTextViewProductName = itemView.findViewById(R.id.textViewProductName);
            mTextViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            mTextViewProductQty = itemView.findViewById(R.id.textViewProductQuantity);
            mTextViewTotalCost = itemView.findViewById(R.id.textViewTotalCost);

            mButtonCartRemoveItem = itemView.findViewById(R.id.buttonCartRemoveItem);

            mButtonCartRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemRemoveListener != null){
                        mOnItemRemoveListener.onRemoveClick(getLayoutPosition());
                    }
                }
            });
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

    public interface OnItemRemoveListener {
        void onRemoveClick(int productId);
    }


}
