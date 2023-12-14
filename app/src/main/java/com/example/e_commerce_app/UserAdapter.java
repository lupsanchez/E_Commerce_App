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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {


    private List<User> userList;
    private OnItemClickListener listener;
    private OnDeleteClickListener onDeleteClickListener;
    private Context context;

    private ECommerceDAO mECommerceDAO;


    public UserAdapter(List<User> userList, OnItemClickListener listener, Context context, ECommerceDAO eCommerceDAO, OnDeleteClickListener onDeleteClickListener) {
        this.userList = userList;
        this.listener = listener;
        this.context = context;
        this.mECommerceDAO = eCommerceDAO;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View userView = inflater.inflate(R.layout.item_user, parent, false);

        // Return a new holder instance
        return new UserViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Get the data model based on the position
        User user = userList.get(position);


        holder.mTextViewUserId.setText(String.valueOf(user.getUserId()));
        holder.mTextViewUsername.setText(user.getUserName());
        if(user.isAdmin()){
            holder.mTextViewIsAdmin.setText("Admin Account");
        } else{
            holder.mTextViewIsAdmin.setText("Customer Account");
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Define the view holder
    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewUserId;
        TextView mTextViewUsername;
        TextView mTextViewIsAdmin;
        Button mButtonDelete;

        public UserViewHolder(View itemView) {
            super(itemView);

            // Get references to the views defined in item_user.xml

            mTextViewUserId = itemView.findViewById(R.id.textViewUserId);
            mTextViewUsername = itemView.findViewById(R.id.textViewUsername);
            mTextViewIsAdmin = itemView.findViewById(R.id.textViewIsAdmin);
            mButtonDelete = itemView.findViewById(R.id.buttonDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(userList.get(position).getUserId());
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

    public void refreshDataSet(List<User> newDataSet) {
        userList.clear();
        userList.addAll(newDataSet);
        notifyDataSetChanged();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(int userId);
    }
}