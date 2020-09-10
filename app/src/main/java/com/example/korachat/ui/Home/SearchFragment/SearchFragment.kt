package com.example.korachat.ui.Home.SearchFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.korachat.R
import com.example.korachat.data.Repository
import com.example.korachat.models.UserChats
import com.example.korachat.models.Users
import com.example.korachat.ui.ChattingRoom.ChatRoom
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_search.view.progress_bar

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {
    var mUsers: ArrayList<Users>? = null
    val mAuth = FirebaseAuth.getInstance()
    var searchFragmentViewModel: SearchFragmentVM? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress_bar.visibility=View.VISIBLE
        searchFragmentViewModel = ViewModelProvider(this).get(SearchFragmentVM::class.java)
        mUsers = ArrayList<Users>()
       // Repository().getConnectedUsers()
        searchFragmentViewModel!!.getUsers(this)
        var adapter =
            UsersSearchAdapter(
                context!!,
                listOf(),
                this
            )
        searchFragmentViewModel!!.mUsers.observe(this, Observer {
            if (it != null) {
                Log.w("myTag", "search" + it.size)
            }
            mUsers = it
            adapter.usersList = it
            adapter.notifyDataSetChanged()
            progress_bar.visibility=View.GONE
        })
        if(adapter.usersList.isNullOrEmpty()){
            progress_bar.visibility=View.GONE
        }
        view.search_recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.search_recyclerview.adapter = adapter
    }

    fun connectToUser(position: Int) {
        if(mUsers!!.size<=0){
            return
        }
        progress_bar.visibility=View.VISIBLE
        val userUID = mUsers!![position].uid.toString()
        val userName = mUsers!![position].username.toString()
        val chatMembers = ArrayList<String>()
        chatMembers.add(mAuth.uid.toString())
        chatMembers.add(userUID)
        val userChats = UserChats()
        userChats.lastMsg = userName
        userChats.members = chatMembers
        searchFragmentViewModel!!.checkUsersConnected(userUID,this,userChats)
        searchFragmentViewModel!!.sendConnectRequest(mAuth.uid.toString(),userUID)
    }
}
