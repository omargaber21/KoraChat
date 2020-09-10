package com.example.korachat.ui.Home.ChatsFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.korachat.R
import com.example.korachat.data.Repository
import com.example.korachat.models.Users
import com.example.korachat.ui.ChattingRoom.ChatRoom
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_chats.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChatsFragment : Fragment() {
    var listOfHashMap=ArrayList<HashMap<String,Any>>()
    val mAuth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress_bar.visibility=View.VISIBLE
        users_chat_recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter=UsersChatAdapter(context!!,this)
        val chatsFragmentViewModel=ViewModelProvider(this).get(ChatsFragmentVM::class.java)
        chatsFragmentViewModel.showConnectedUsers(this)
        chatsFragmentViewModel.mapMLD.observe(this, Observer {
            listOfHashMap=it
            adapter.usersList= listOfHashMap
            adapter.notifyDataSetChanged()
            users_chat_recyclerview.adapter=adapter
            progress_bar.visibility=View.GONE
        })
        if(adapter.usersList?.size==0||adapter.usersList.isNullOrEmpty()){
            progress_bar.visibility=View.GONE

        }
    }
    fun goToChatRoom(position: Int) {
        val intent: Intent = Intent(context, ChatRoom::class.java)
        val map=listOfHashMap[position]
        val chatUID=map["chatUID"].toString()
        val user=map["user"]
        intent.putExtra("user", user as Users)
        intent.putExtra("chatUID",chatUID)
        intent.putExtra("currentUser", mAuth.currentUser!!.uid)
        startActivity(intent)



    }

}
