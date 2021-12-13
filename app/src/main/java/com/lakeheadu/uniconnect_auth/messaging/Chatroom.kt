package com.lakeheadu.uniconnect_auth.messaging

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils

/**
 *  this file implemented by Craig
 */

/**
 * An object representing a chatroom.
 *
 * @property docRef Document Reference to this chatroom in Firestore.
 * @author craig
 */
data class Chatroom(val docRef: DocumentReference? = null) {

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
    fun getAllMessages(): CollectionReference {
        return docRef!!.collection("messages")
    }

    fun update(): Task<Void> {
        val map = hashMapOf(
            "docRef" to docRef
        )

        return this.docRef!!.set(map)
    }

}