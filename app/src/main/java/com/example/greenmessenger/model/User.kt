package com.example.greenmessenger.model

class User {
    var name:String? = null
    var profileImage:String? = null
    constructor(
        name:String?,
        profileImage:String?
    ){

        this.name = name
        this.profileImage = profileImage
    }
}