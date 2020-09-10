package com.example.korachat.models

import java.io.Serializable

class Users : Serializable {
    var email: String?=null
    var phone: String?=null
    var imageURL:String?=""
    var uid: String?=null
    var username: String?=null
    var favourite_team:String?=null
    var sentConnections:ArrayList<String>?=null
    var receivedConnections:ArrayList<String>?=null

}