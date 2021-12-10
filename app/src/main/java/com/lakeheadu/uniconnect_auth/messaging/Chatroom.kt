package com.lakeheadu.uniconnect_auth.messaging

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils

/**
 * An object representing a chatroom.
 *
 * @property docRef Document Reference to this chatroom in Firestore.
 */
data class Chatroom(val docRef : DocumentReference) {

    /**
     * send a message to this chat
     *
     * @param str contents of message
     */
    fun sendMessage(str : String) {
        FirebaseUtils.sendMessage(this, str)
    }

    fun inviteUser(sender : User, u : User) {
        // create doc in user's request collection
        val doc = u.docRef.collection("chat_requests").document()

        val invite = chatRequest(doc, sender.docRef, this.docRef)

        // write it to doc now
        doc.set(invite)
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
    fun getAllMessages() : CollectionReference {
        return docRef.collection("messages")
    }

}