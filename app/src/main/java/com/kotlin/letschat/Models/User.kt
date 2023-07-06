package com.kotlin.letschat.Models

import android.media.Image

class User {
    var username: String?= null
    var email:String?= null
    var uid: String?= null
    var quote: String?= null

    constructor()

    constructor(username: String?, email: String?, uid: String?, quote: String?) {
        this.username = username
        this.email = email
        this.uid = uid
        this.quote = quote
    }
}