package com.lakeheadu.uniconnect_auth.messaging

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils

data class User(
    var self : DocumentReference,
    var email: String,
    var uid: String,
    var chatrooms : MutableList<Chatroom> = mutableListOf()
) {
    /**
     * create a new chatroom.
     *
     * @param other another user
     */
    fun newChat(other : User) {
        val chat = FirebaseUtils.newChat()

        chat.addUser(this)
        chat.addUser(other)

    }

    /**
     * leaves a chatroom
     *
     * @param chat the chatroom to leave
     */
    fun leaveChat(chat : Chatroom) {
        chatrooms.remove(chat)
        update()
    }

    /**
     * updates this object in firestore
     *
     */
    fun update() {
        self.set(this)
    }
}