package com.example.e_commerce_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_app.db.ECommerceDAO;

import java.util.ArrayList;
import java.util.List;

public class ProductListingAdapter extends RecyclerView.Adapter<ProductListingAdapter.ProductListingViewHolder> implements Filterable {

    private List<Product> productList;
    private List<Product> productListFull;
    private ProductListingAdapter.OnItemClickListener listener;
    private Context context;

    private ECommerceDAO mECommerceDAO;


    public ProductListingAdapter(List<Product> productList, ProductListingAdapter.OnItemClickListener listener, Context context, ECommerceDAO eCommerceDAO) {
        this.productList = productList;
        this.listener = listener;
        this.context = context;
        this.mECommerceDAO = eCommerceDAO;
    }

    public ProductListingAdapter(List<Product> productList){
        this.productList = productList;
        productListFull = new ArrayList<>(productList);
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


        holder.mTextViewProductId.setText(String.valueOf("ID: " + product.getProductId()));
        holder.mTextViewProductName.setText(product.getProductName());
        holder.mTextViewProductPrice.setText("$ " + String.format("%.2f",product.getProductPrice()));
        holder.mTextViewProductQuantity.setText("In Stock: " + String.valueOf(product.getProductQuantity()));
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

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(productListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Product product : productListFull){
                    if(product.getProductName().toLowerCase().contains(filterPattern)){
                        filteredList.add(product);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productList.clear();
            productList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener {
        void onItemClick(int productId);
    }
}
