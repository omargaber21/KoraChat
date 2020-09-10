package com.example.korachat.data

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.korachat.models.ChatMessages
import com.example.korachat.models.Request
import com.example.korachat.models.UserChats
import com.example.korachat.models.Users
import com.example.korachat.ui.Authentication.Register.RegisterActivity
import com.example.korachat.ui.Home.SearchFragment.SearchFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.Exception
import java.net.URI
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.timerTask

class Repository {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUserUID = mAuth.currentUser?.uid
    var successLoginLD = MutableLiveData<Boolean>()
    var successRegisterLD = MutableLiveData<Boolean>()
    val storageReference = FirebaseStorage.getInstance().reference
    var userId: String? = null
    fun SignIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                successLoginLD.postValue(true)
                Log.w("myTag", "Signin Success", task.exception)

            } else {
                successLoginLD.value = false
                Log.w("myTag", "Signin Failed", task.exception)
            }
        }
    }

    fun Register(email: String, password: String, newUser: Users) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userId = mAuth.currentUser!!.uid
                newUser.uid = userId!!
                val connections = ArrayList<String>()
                connections.add("")
                newUser.sentConnections = connections
                newUser.receivedConnections = connections
                reference.child("Users").child(userId!!).setValue(newUser)
                    .addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            successRegisterLD.value = true

                        }
                    })
            } else {
                successRegisterLD.value = false
                Log.w("myTag", "Register Failed", task.exception)
            }
        }
    }

    val reference = FirebaseDatabase.getInstance().reference
    var mUsers: List<Users>? = null
    var usersMutableLiveData: MutableLiveData<List<Users>>? = null
    fun getUsers() {
        mUsers = ArrayList()
        usersMutableLiveData = MutableLiveData<List<Users>>()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("myTag", "Signed out")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                for (snapshot in snapshot.children) {
                    val user: Users? = snapshot.getValue(Users::class.java)
                    if (!(user!!.uid.equals(currentUser!!.uid)) && !(user.sentConnections!!.contains(
                            currentUser.uid
                        ))
                    ) {
                        if (!(user.receivedConnections!!.contains(currentUser.uid))) {
                            (mUsers as ArrayList<Users>).add(user)
                        }

                    }
                }
                usersMutableLiveData!!.postValue(mUsers)

            }
        }
        reference.child("Users").addValueEventListener(listener)
    }
    //add in db userChats & chats -> chatUID

    fun addChat(userChats: UserChats, userUID: String) {
        var chatUID: String = reference.push().key.toString()
        reference.child("Chats").child(chatUID).setValue(userChats)
        reference.child("userChats").child(mAuth.currentUser!!.uid).child(chatUID).setValue(chatUID)
        reference.child("userChats").child(userUID).child(chatUID).setValue(chatUID)

    }

    var checkConnectedMLD: Boolean = false
    fun checkUsersConnected(userUID: String, userChats: UserChats) {
        val members = ArrayList<String>()
        val listener = object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.w("myTag", "Signed out")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    checkConnectedMLD = false
                    Log.w("myTag", "clean data")
                }
                for (data in snapshot.children) {
                    members.clear()
                    val userChats: UserChats = data.getValue(UserChats::class.java)!!
                    for (member in userChats.members!!) {
                        members.add(member)
                    }
                    if (members.contains(userUID) && members.contains(mAuth.currentUser!!.uid.toString())) {
                        checkConnectedMLD = true
                    }
                }
                if (!checkConnectedMLD) {
                    addChat(userChats, userUID)
                }
            }
        }

        reference.child("Chats").addListenerForSingleValueEvent(listener)
    }

    //getting all chats for current user
    fun showConnectedUsers() {

        var chatUID: String? = null
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("myTag", "Signed out")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listOfHashMap.clear()
                for (data in snapshot.children) {
                    chatUID = data.key.toString()
                    getUserUID(chatUID!!)
                    Log.w("myxxTag", "chatUID$chatUID")
                }
            }
        }

        reference.child("userChats").child(mAuth.currentUser!!.uid).addValueEventListener(listener)
    }

    var listOfHashMap = ArrayList<HashMap<String, Any>>()
    val mapLiveData = MutableLiveData<ArrayList<HashMap<String, Any>>>()
    fun getConnectedUsersDetails(userUID: String, chatUID: String) {
        val usersListener = object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.w("myTag", "Signed out")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var map = HashMap<String, Any>()
                for (data in snapshot.children) {
                    if (data.key == userUID) {
                        val user = data.getValue(Users::class.java)
                        if (user != null) {
                            map["user"] = user
                            map["chatUID"] = chatUID
                            listOfHashMap.add(map)
                            Log.w("myxxTag", "map size${listOfHashMap.size}")
                        }
                    }
                }
                mapLiveData.postValue(listOfHashMap)
            }
        }
        reference.child("Users").addListenerForSingleValueEvent(usersListener)

    }


    fun getUserUID(chatUID: String) {
        val listener = object : ValueEventListener {
            var userUID: String? = null
            override fun onCancelled(error: DatabaseError) {
                Log.w("myTag", "Signed out")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if (!(data.value == mAuth.currentUser!!.uid)) {
                        userUID = data.value.toString()
                        getConnectedUsersDetails(userUID!!, chatUID)
                        Log.w("myxxTag", "userUID$userUID")
                    }
                }
            }
        }
        reference.child("Chats").child(chatUID).child("members").addValueEventListener(listener)
    }
    val currentUserMLD = MutableLiveData<Users>()
    fun getCurrentUserDetails(currentUserUID: String) {
        val usersListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("myTag", "Signed out")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                if (user != null) {
                    currentUserMLD.postValue(user)
                }

            }
        }
        reference.child("Users").child(currentUserUID).addValueEventListener(usersListener)

    }
    fun sendMessage(chatUID: String, chatMessages: ChatMessages) {
        val messageUID = reference.push().key.toString()
        reference.child("chatMessages").child(chatUID).child(messageUID).setValue(chatMessages)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    reference.child("Chats").child(chatUID).child("lastMsg").setValue(messageUID)
                    reference.child("Chats").child(chatUID).child("TIMESTAMP").setValue(ServerValue.TIMESTAMP)

                    Log.w("myXTag", "Success")
                } else {
                    throw it.exception!!
                }
            }
    }

    val loadMessages = ArrayList<ChatMessages>()
    val loadMessagesMLD = MutableLiveData<ArrayList<ChatMessages>>()
    fun showMessages(chatUID: String) {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("myTag", "Signed out")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                loadMessages.clear()
                for (data in snapshot.children) {
                    val chatMessage = data.getValue(ChatMessages::class.java)
                    if (chatMessage != null) {
                        loadMessages.add(chatMessage)
                    }

                }
                loadMessagesMLD.postValue(loadMessages)
            }

        }
        reference.child("chatMessages").child(chatUID).addValueEventListener(listener)
    }



    var listOfUsersSentConnection = ArrayList<String>()
    var listOfUsersReceivedConnection = ArrayList<String>()
    fun sendConnectRequest(currentUserUID: String, userUID: String) {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("myTag", "Signed out")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if (data.key == currentUserUID) {
                        listOfUsersSentConnection.clear()
                        val user = data.getValue(Users()::class.java)
                        if (user != null) {
                            listOfUsersSentConnection = user.sentConnections!!
                            if (!(listOfUsersSentConnection.contains(userUID))) {
                                listOfUsersSentConnection.add(userUID)
                                reference.child("Users").child(currentUserUID)
                                    .child("sentConnections").setValue(listOfUsersSentConnection)
                            }
                        }

                    }
                    if (data.key == userUID) {
                        listOfUsersReceivedConnection.clear()
                        val user = data.getValue(Users()::class.java)
                        if (user != null) {
                            listOfUsersReceivedConnection = user.receivedConnections!!
                            if (!(listOfUsersReceivedConnection.contains(currentUserUID))) {
                                listOfUsersReceivedConnection.add(currentUserUID)
                                reference.child("Users").child(userUID).child("receivedConnections")
                                    .setValue(listOfUsersReceivedConnection)
                            }
                        }

                    }

                }
            }
        }
        reference.child("Users").addListenerForSingleValueEvent(listener)
    }

    fun uploadProfileImage(imageURI: Uri) {
        val key = reference.push().key
        val ref = storageReference.child("profileImages").child(key + ".jpg")
        val uploadTask = ref.putFile(imageURI)
        val urlTask = uploadTask.continueWithTask {
            if (!it.isSuccessful) {
                it.exception?.let { throw it }
            }
            ref.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                val imageUrl = it.result.toString()
                reference.child("Users").child(mAuth.currentUser!!.uid).child("imageURL")
                    .setValue(imageUrl)
            }
        }

    }
    val imageUrlMLD=MutableLiveData<String>()
    fun uploadImage(imageURI: Uri,chatUID: String,chatMessages: ChatMessages) {
        val key = reference.push().key
        val ref = storageReference.child("profileImages").child(key + ".jpg")
        val uploadTask = ref.putFile(imageURI)
        val urlTask = uploadTask.continueWithTask {
            if (!it.isSuccessful) {
                it.exception?.let { throw it }
            }
            ref.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                val imageUrl = it.result.toString()
                chatMessages.message=""
                chatMessages.imageUrl=imageUrl
                sendMessage(chatUID,chatMessages)
                imageUrlMLD.postValue(imageUrl)
                Log.w("Tango", "asfasfs$imageUrl")

            }
        }
    }
    val lastMsgMLD=MutableLiveData<ChatMessages>()
    fun getLastMsg(chatUID: String,messageUID:String){
        val listener=object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
            val message=p0.getValue(ChatMessages::class.java)
                if(message!=null){
                    lastMsgMLD.postValue(message)
                }
            }
        }
        reference.child("chatMessages").child(chatUID).child(messageUID).addListenerForSingleValueEvent(listener)
    }

}