package com.lakeheadu.uniconnect_auth.messaging

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils

data class Chatroom(val self : DocumentReference) {

    /**
     * send a message to this chat
     *
     * @param str contents of message
     */
    fun sendMessage(str : String) {
        FirebaseUtils.sendMessage(this, str)
    }

    /**
     * kinda hackish, since a user should not be able to modify another user's object.
     * will think about better way to do this
     *
     * @param u the user to add
     */
    fun addUser(u : User) {
        u.chatrooms.add(this)
        u.update()
    }

    /**
     * get all the messages for this chatroom. See FirebaseUtils.kt on how to use
     * this return type
     *
     * @return a CollectionReference to all the messages for this chatroom
     */
    fun getMessages() : CollectionReference {
        return self.collection("messages")
    }

}