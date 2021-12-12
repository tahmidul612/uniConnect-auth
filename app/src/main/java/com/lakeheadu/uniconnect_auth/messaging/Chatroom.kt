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

    /**
     * invite a user to this chatroom.
     *
     * @param u User to be invited.
     */
    fun inviteUser(u : User) {
        FirebaseUtils.inviteUserToChat(this, u)
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