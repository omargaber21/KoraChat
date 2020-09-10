package com.example.korachat.ui.Home.SearchFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.korachat.R
import com.example.korachat.models.Users
import kotlinx.android.synthetic.main.chats_rowitem.view.*
import kotlinx.android.synthetic.main.search_rowitem.view.*

class UsersSearchAdapter(
    var mContext: Context,
    var usersList: List<Users>,
    var fragment: SearchFragment
) :
    RecyclerView.Adapter<UsersSearchAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.search_rowitem, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.user_name.text = usersList[position].username
        holder.favourite.text = usersList[position].favourite_team
        Glide.with(mContext)
            .load(usersList[position].imageURL)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_person_grey_24dp)
            .into(holder.user_circle_image)
        holder.connectButton.setOnClickListener {
            connectToUser(position)
            (usersList as ArrayList).removeAt(position)
            notifyDataSetChanged()
        }
    }

    private fun connectToUser(position: Int) {
        fragment.connectToUser(position)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val user_circle_image = view.search_circleImageView
        val user_name = view.search_username
        val favourite = view.search_favourite
        var layout = view.search_holder_layout
        var connectButton = view.connect_button
    }
}