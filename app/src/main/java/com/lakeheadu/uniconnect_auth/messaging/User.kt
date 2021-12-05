package com.lakeheadu.uniconnect_auth.messaging

import com.google.firebase.firestore.DocumentReference
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils

/**
 * An object representing a user.
 *
 * @property docRef DocumentReference to this object in Firestore.
 * @property displayName chosen display name of this user
 * @property email email address of this user
 * @property uid user_id of this user (equivalent to FirebaseUser?.uid)
 * @property chatrooms list of chatrooms which this user is a part of.
 */
data class User(
    var docRef : DocumentReference,
    var email: String,
    var uid: String,
    var displayName : String = "",
    var chatrooms : MutableList<Chatroom> = mutableListOf()
) {

    fun updateName(new_name : String) {
        displayName = new_name
        update()
    }

    /**
     * create a new chatroom.
     * @param other another user
     */
    fun newChat(other : User) {
        val chat = FirebaseUtils.newChat()

        chat.addUser(this)
        chat.addUser(other)
        update()
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