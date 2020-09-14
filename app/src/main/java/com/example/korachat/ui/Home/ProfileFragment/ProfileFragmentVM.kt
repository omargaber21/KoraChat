package com.example.korachat.ui.Home.ProfileFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.korachat.data.Repository
import com.example.korachat.models.Users

class ProfileFragmentVM :ViewModel() {
    val repo=Repository()
    val currentUserMLD=MutableLiveData<Users>()
    fun getCurrentUserDetails(fragment: ProfileFragment,currentUser:String){
        repo.getCurrentUserDetails(currentUser)
        repo.currentUserMLD.observe(fragment, Observer {
            currentUserMLD.postValue(it)
        })
    }
}