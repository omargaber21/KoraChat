package com.example.korachat.ui.Home.SearchFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.korachat.data.Repository
import com.example.korachat.models.UserChats
import com.example.korachat.models.Users
import com.example.korachat.ui.Home.SearchFragment.SearchFragment
import com.google.firebase.database.core.Repo

class SearchFragmentVM : ViewModel() {
    val repo = Repository()
    var mUsers = MutableLiveData<ArrayList<Users>>()
    fun getUsers(context: SearchFragment) {
        repo.getUsers()
        repo.usersMutableLiveData!!.observe(context, Observer {
            mUsers.postValue(it as ArrayList<Users>)
        })
    }

    fun checkUsersConnected(userUID: String, fragment: SearchFragment, userChats: UserChats) {
        repo.checkUsersConnected(userUID, userChats)
    }
    fun sendConnectRequest(currentUserUID: String,userUID:String){
        repo.sendConnectRequest(currentUserUID,userUID)
    }
}