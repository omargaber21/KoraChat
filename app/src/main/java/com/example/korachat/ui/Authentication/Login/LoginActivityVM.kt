package com.example.korachat.ui.Authentication.Login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.korachat.data.Repository
import com.example.korachat.ui.Authentication.Login.LoginActivity

class LoginActivityVM : ViewModel() {
    val repo:Repository= Repository()
    var successfullLogin = MutableLiveData<Boolean>()
    fun Login(email: String, password: String, activity: LoginActivity) {
        repo.SignIn(email, password)!!
        repo.successLoginLD.observe(activity, Observer {
            Log.w("myTag","observe Succ")
            successfullLogin.postValue(it)
        })
    }

}