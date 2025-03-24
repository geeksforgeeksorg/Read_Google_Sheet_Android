package com.example.google_sheet_read

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide;

class UserRVAdapter(
    private val userModalArrayList: ArrayList<UserModal>,
    private val context: Context
) : RecyclerView.Adapter<UserRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflating our layout file.
        val view = LayoutInflater.from(context).inflate(R.layout.items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Getting data from our array list in our modal class.
        val userModal = userModalArrayList[position]

        // Setting data to our text views.
        holder.firstNameTV.text = userModal.first_name
        holder.lastNameTV.text = userModal.last_name
        holder.emailTV.text = userModal.email

        Glide.with(context).load(userModal.avatar).into(holder.userIV)
    }

    override fun getItemCount(): Int {
        // Returning the size of the array list.
        return userModalArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Creating variables for our text views and image view.
        val firstNameTV: TextView = itemView.findViewById(R.id.idTVFirstName)
        val lastNameTV: TextView = itemView.findViewById(R.id.idTVLastName)
        val emailTV: TextView = itemView.findViewById(R.id.idTVEmail)
        val userIV: ImageView = itemView.findViewById(R.id.idIVUser)
    }
}