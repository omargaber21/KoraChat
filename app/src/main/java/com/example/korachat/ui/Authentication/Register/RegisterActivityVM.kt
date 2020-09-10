package com.example.korachat.ui.Authentication.Register

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.korachat.data.Repository
import com.example.korachat.models.Users

class RegisterActivityVM : ViewModel() {
    val repo: Repository = Repository()
    var successfullRegister = MutableLiveData<Boolean>()
    fun Register(
        email: String,
        password: String,
        context: RegisterActivity,
        newUser:Users
    ) {
        repo.Register(email, password, newUser)
        repo.successRegisterLD.observe(context, Observer {
            successfullRegister.postValue(it)
        })
    }
    fun uploadImage(imageURI:Uri) {
        repo.uploadProfileImage(imageURI)
    }
}