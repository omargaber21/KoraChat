package com.example.korachat.ui.ChattingRoom

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.korachat.data.Repository
import com.example.korachat.models.ChatMessages
import com.example.korachat.ui.ChattingRoom.ChatRoom

class ChatRoomVM :ViewModel() {
    val repo=Repository()
    fun sendMessage(chatUID:String,chatMessages:ChatMessages ){
        repo.sendMessage(chatUID,chatMessages)
    }
    val messagesMLD=MutableLiveData<ArrayList<ChatMessages>>()
    fun loadMessages(chatUID:String,context: ChatRoom){
        repo.showMessages(chatUID)
        repo.loadMessagesMLD.observe(context, Observer {
            messagesMLD.postValue(it)
        })

    }
    val imageUrlMLD=MutableLiveData<String>()
    fun uploadImage(imageUri: Uri,context:ChatRoom,chatUID: String,chatMessages: ChatMessages){
        repo.uploadImage(imageUri,chatUID, chatMessages)
        repo.imageUrlMLD.observe(context, Observer {
            imageUrlMLD.postValue(it)
        })
    }
}