package com.example.e_commerce_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
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

        // Set item views based on your views and data model
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
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewUsername;
        TextView mTextViewIsAdmin;

        public UserViewHolder(View itemView) {
            super(itemView);

            // Get references to the views defined in item_user.xml
            mTextViewUsername = itemView.findViewById(R.id.textViewUsername);
            mTextViewIsAdmin = itemView.findViewById(R.id.textViewIsAdmin);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}