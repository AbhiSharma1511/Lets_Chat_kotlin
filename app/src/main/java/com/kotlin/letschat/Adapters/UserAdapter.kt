package com.kotlin.letschat.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.kotlin.letschat.ChatActivity
import com.kotlin.letschat.Models.User
import com.kotlin.letschat.R

class UserAdapter(private val context: Context, private val userList: ArrayList<User> ): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.userlist_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = userList[position]
        holder.username.text = user.username
        holder.quote.text = user.quote

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("name",user.username)
            intent.putExtra("uid",user.uid)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val username: TextView = itemView.findViewById<TextView>(R.id.txtUsername)
        val quote: TextView = itemView.findViewById<TextView>(R.id.txtQuote)
//        val img: TextView = itemView.findViewById<TextView>(R.id.imgUser)
    }
}