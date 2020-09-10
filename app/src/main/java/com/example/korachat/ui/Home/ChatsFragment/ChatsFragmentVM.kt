package com.example.korachat.ui.Home.ChatsFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.korachat.data.Repository
import com.example.korachat.models.Users

class ChatsFragmentVM :ViewModel() {
    var mapMLD=MutableLiveData<ArrayList<HashMap<String,Any>>>()
    fun showConnectedUsers(fragment:ChatsFragment){
        val repo=Repository()
        repo.showConnectedUsers()
        repo.mapLiveData.observe(fragment, Observer {
            mapMLD.postValue(it)
        })
    }
}