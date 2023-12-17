package com.example.e_commerce_app;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Index;

import com.example.e_commerce_app.db.ECommerceDAO;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Cart> mOrdersList;
    private OnCancelClickListener mOnCancelClickListener;
    private List<Product> mProductsList;
    private List<Integer> mProductIds;

    private Context mContext;
    private ECommerceDAO mECommerceDAO;

    public OrderAdapter(List<Cart> ordersList, Context context, ECommerceDAO ECommerceDAO, OnCancelClickListener onCancelClickListener) {
        mOnCancelClickListener = onCancelClickListener;
        mOrdersList = ordersList;
        mContext = context;
        mECommerceDAO = ECommerceDAO;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View orderView = inflater.inflate(R.layout.item_order, parent, false);

        return new OrderViewHolder(orderView);
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView mTextViewOrderStatus;
        TextView mTextViewCartId;
        TextView mTextViewUserId;
        TextView mTextViewTotalCartCost;

        Button mButtonCancel;

        public OrderViewHolder(View itemView){
            super(itemView);

            mTextViewOrderStatus = itemView.findViewById(R.id.textViewOrderStatus);
            mTextViewCartId = itemView.findViewById(R.id.textViewCartId);
            mTextViewUserId = itemView.findViewById(R.id.textViewUserId);
            mTextViewTotalCartCost = itemView.findViewById(R.id.textViewTotalCartCost);

            mButtonCancel = itemView.findViewById(R.id.buttonCancel);

            mButtonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnCancelClickListener != null){
                        mOnCancelClickListener.onCancelClick(getLayoutPosition());
                    }

                }
            });
        }
    }

    public  void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Cart order = mOrdersList.get(position);

        String OrderStatus;

        if (!order.isCartOrdered() && !order.isCartCancelled()) {
            OrderStatus = "IN CART - ORDER NOT PLACED";
        } else if (order.isCartOrdered() && order.isCartCancelled()) {
            OrderStatus = "ORDER CANCELLED";
        } else {
            OrderStatus = "ORDER PLACED";
        }

        holder.mTextViewOrderStatus.setText(OrderStatus);
        holder.mTextViewCartId.setText("Order #: " + String.valueOf(order.getCartId()));
        holder.mTextViewUserId.setText("User Id: " + String.valueOf(order.getUserId()));
        holder.mTextViewTotalCartCost.setText("Cart Total: $" + String.format("%.2f",order.getCartTotalCost()));

    }

    @Override
    public int getItemCount(){return mOrdersList.size();}

    public interface OnCancelClickListener {
        void onCancelClick(int position);
    }

}
