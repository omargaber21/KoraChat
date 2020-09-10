package com.example.korachat.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.korachat.data.Repository
import com.example.korachat.models.Users
import com.example.korachat.ui.HomeActivity

class HomeActivityVM :ViewModel() {
    val repo=Repository()
    val currentUserMLD=MutableLiveData<Users>()
    fun getCurrentUserDetails(activity:HomeActivity,currentUserUID:String){
        repo.getCurrentUserDetails(currentUserUID)
        repo.currentUserMLD.observe(activity, Observer {
            currentUserMLD.postValue(it)
        })
    }
}