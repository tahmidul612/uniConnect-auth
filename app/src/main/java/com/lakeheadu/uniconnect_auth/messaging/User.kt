package com.lakeheadu.uniconnect_auth.messaging

import com.google.firebase.firestore.DocumentReference
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils

data class User(
    var docRef : DocumentReference,
    var email: String,
    var uid: String,
    var chatrooms : MutableList<Chatroom> = mutableListOf()
) {
    /**
     * create a new chatroom.
     * @param other another user
     */
    fun newChat(other : User) {
        val chat = FirebaseUtils.newChat()

        chat.addUser(this)
        chat.addUser(other)

    }

    /**
     * leaves a chatroom
     * @param chat the chatroom to leave
     */
    fun leaveChat(chat : Chatroom) {
        chatrooms.remove(chat)
        update()
    }

    /**
     * deletes this user account. also signs out
     *
     */
    fun deleteAccount() {
        docRef.delete()
        FirebaseUtils.firebaseUser?.delete()
        FirebaseUtils.signOut()
    }

    /**
     * updates this object in firestore
     *
     */
    fun update() {
        docRef.set(this)
    }
}