package com.kotlin.letschat.Models

import android.media.Image

class UserChat {

    var message: String?=null
    var senderId: String? = null

    constructor()
    constructor(message: String?, senderId: String?) {
        this.message = message
        this.senderId = senderId
    }

}