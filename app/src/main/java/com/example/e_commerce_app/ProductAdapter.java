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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private UserAdapter.OnItemClickListener listener;
    private UserAdapter.OnDeleteClickListener onDeleteClickListener;
    private Context context;

    private ECommerceDAO mECommerceDAO;


    public ProductAdapter(List<Product> productList, UserAdapter.OnItemClickListener listener, Context context, ECommerceDAO eCommerceDAO, UserAdapter.OnDeleteClickListener onDeleteClickListener) {
        this.productList = productList;
        this.listener = listener;
        this.context = context;
        this.mECommerceDAO = eCommerceDAO;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_product, parent, false);

        // Return a new holder instance
        return new ProductAdapter.ProductViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
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

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewProductId;
        TextView mTextViewProductName;
        TextView mTextViewProductPrice;
        TextView mTextViewProductQuantity;
        Button mButtonDelete;

        public ProductViewHolder(View itemView) {
            super(itemView);

            mTextViewProductId = itemView.findViewById(R.id.textViewProductId);
            mTextViewProductName = itemView.findViewById(R.id.textViewProductName);
            mTextViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            mTextViewProductQuantity = itemView.findViewById(R.id.textViewProductQuantity);
            mButtonDelete = itemView.findViewById(R.id.buttonDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(productList.get(position).getProductId());
                    }

                }
            });

            mButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onDeleteClickListener != null){
                        onDeleteClickListener.onDeleteClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(int userId);
    }
}
