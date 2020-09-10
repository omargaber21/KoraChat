package com.example.korachat.ui.Home.ChatsFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.korachat.R
import com.example.korachat.models.Users
import kotlinx.android.synthetic.main.chats_rowitem.view.*

class UsersChatAdapter(var mContext: Context,var fragment: ChatsFragment) :
    RecyclerView.Adapter<UsersChatAdapter.MyViewHolder>() {

     var usersList:ArrayList<HashMap<String,Any>>?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.chats_rowitem, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return usersList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user=usersList!![position]["user"] as Users
        val chatUID=usersList!![position]["chatUID"] as String
        holder.user_name.text=user.username
        holder.last_msg.text=chatUID
        holder.user_circle_image.circleBackgroundColor=getColor(mContext,R.color.baseImageBackground)
        Glide.with(mContext)
            .load(user.imageURL)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_person_grey_24dp)
            .into(holder.user_circle_image)
        holder.layout_holder.setOnClickListener {
            goToChat(position)
        }
    }

    private fun goToChat(position: Int) {
        fragment.goToChatRoom(position)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val user_circle_image = view.circleImageView
        val user_name = view.username
        val last_msg = view.lastMsg
        val layout_holder=view.chats_holder_layout
    }
}