package com.gfg.google_sheet_java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserRVAdapter extends RecyclerView.Adapter<UserRVAdapter.ViewHolder> {

    private ArrayList<UserModal> userModalArrayList;
    private Context context;

    public UserRVAdapter(ArrayList<UserModal> userModalArrayList, Context context) {
        this.userModalArrayList = userModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating our layout file.
        View view = LayoutInflater.from(context).inflate(R.layout.items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Getting data from our array list in our modal class.
        UserModal userModal = userModalArrayList.get(position);

        // Setting data to our text views.
        holder.firstNameTV.setText(userModal.getFirst_name());
        holder.lastNameTV.setText(userModal.getLast_name());
        holder.emailTV.setText(userModal.getEmail());

        Glide.with(context).load(userModal.getAvatar()).into(holder.userIV);
    }

    @Override
    public int getItemCount() {
        // Returning the size of the array list.
        return userModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Creating variables for our text views and image view.
        public TextView firstNameTV, lastNameTV, emailTV;
        public ImageView userIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initializing our variables.
            firstNameTV = itemView.findViewById(R.id.idTVFirstName);
            lastNameTV = itemView.findViewById(R.id.idTVLastName);
            emailTV = itemView.findViewById(R.id.idTVEmail);
            userIV = itemView.findViewById(R.id.idIVUser);
        }
    }
}
