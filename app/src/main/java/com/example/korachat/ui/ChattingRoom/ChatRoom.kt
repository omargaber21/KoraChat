package com.example.korachat.ui.ChattingRoom

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.korachat.R
import com.example.korachat.data.Repository
import com.example.korachat.models.ChatMessages
import com.example.korachat.models.Users
import kotlinx.android.synthetic.main.chat_room.*
import kotlinx.android.synthetic.main.message_sent.*
import java.text.SimpleDateFormat
import java.util.*

class ChatRoom : AppCompatActivity() {
    lateinit var adapter:ChatRoomAdapter
    lateinit var user:Users
    lateinit var chatUID: String
    lateinit var currentUser: String
    lateinit var imageUri:Uri
    lateinit var chatMessages:ChatMessages
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_room)
        val intent=intent.extras!!
        user=intent["user"] as Users
        chatUID=intent["chatUID"] as String
        currentUser=intent["currentUser"] as String
        initViewModel()
        initObservers()
        initChatRecycler()
        initListeners()
        initUserDetails(user)

    }

    private fun initUserDetails(user: Users) {
    user_name.text=user.username
        if(user.imageURL!=""&&user.imageURL!=null){
            Glide.with(applicationContext).load(user.imageURL).into(profile_circle_imageview)

        }
    }

    private fun initListeners() {
        chatMessages=ChatMessages()
        val format=SimpleDateFormat("yyyy-MM-dd h:mm a")
        val date=format.format(Date())
        chatMessages.messageDate= date
        chatMessages.sentBy=currentUser
        button.setOnClickListener{
            val message=edit_text.text.toString()
            chatMessages.message=message
            if(message!=""){
                chatMessages.imageUrl=""
                chatRoomVM?.sendMessage(chatUID,chatMessages)
                edit_text.text.clear()
            }
        }
        send_image_button.setOnClickListener{
            val intent= Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type="image/*"
            startActivityForResult(intent,2)
        }
    }

    private fun initAdapter(chatMessages: List<ChatMessages>) {
        adapter.chatMessages=chatMessages
        adapter.notifyDataSetChanged()
    }

    private fun initObservers() {
        chatRoomVM?.loadMessages(chatUID,this)
        chatRoomVM?.messagesMLD?.observe(this, androidx.lifecycle.Observer {
            initAdapter(it)
            adapter.notifyDataSetChanged()
            messages_recyclerview.scrollToPosition(it.size-1)
            Log.w("myXTag",it.size.toString())
        })
    }

    private fun initChatRecycler() {
        adapter=ChatRoomAdapter(
            this,
            listOf(),
            currentUser
        )
        messages_recyclerview.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        messages_recyclerview.adapter=adapter


    }

    private fun initViewModel() {
        chatRoomVM=ViewModelProvider(this).get(ChatRoomVM::class.java)
    }
    companion object{
        var chatRoomVM:ChatRoomVM?=null

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK&&requestCode==2){
            if(data!=null){
                imageUri=data.data as Uri
                sendImage()

            }
        }
    }
    fun sendImage(){
        chatRoomVM?.uploadImage(imageUri,this,chatUID,chatMessages)

    }
}