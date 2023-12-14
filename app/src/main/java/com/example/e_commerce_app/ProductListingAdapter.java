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

public class ProductListingAdapter extends RecyclerView.Adapter<ProductListingAdapter.ProductListingViewHolder> {

    private List<Product> productList;
    private ProductListingAdapter.OnItemClickListener listener;
    private Context context;

    private ECommerceDAO mECommerceDAO;


    public ProductListingAdapter(List<Product> productList, ProductListingAdapter.OnItemClickListener listener, Context context, ECommerceDAO eCommerceDAO) {
        this.productList = productList;
        this.listener = listener;
        this.context = context;
        this.mECommerceDAO = eCommerceDAO;
    }

    @NonNull
    @Override
    public ProductListingAdapter.ProductListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productListingView = inflater.inflate(R.layout.item_product_listing, parent, false);

        // Return a new holder instance
        return new ProductListingViewHolder(productListingView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListingAdapter.ProductListingViewHolder holder, int position) {
        // Get the data model based on the position
        Product product = productList.get(position);


        holder.mTextViewProductId.setText(String.valueOf(product.getProductId()));
        holder.mTextViewProductName.setText(product.getProductName());
        holder.mTextViewProductPrice.setText(String.valueOf(product.getProductPrice()));
        holder.mTextViewProductQuantity.setText(String.valueOf(product.getProductQuantity()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductListingViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewProductId;
        TextView mTextViewProductName;
        TextView mTextViewProductPrice;
        TextView mTextViewProductQuantity;

        public ProductListingViewHolder(View itemView) {
            super(itemView);

            mTextViewProductId = itemView.findViewById(R.id.textViewProductId);
            mTextViewProductName = itemView.findViewById(R.id.textViewProductName);
            mTextViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            mTextViewProductQuantity = itemView.findViewById(R.id.textViewProductQuantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(productList.get(position).getProductId());
                    }

                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int productId);
    }
}
