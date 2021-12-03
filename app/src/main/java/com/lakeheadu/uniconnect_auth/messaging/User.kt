package com.lakeheadu.uniconnect_auth.messaging

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils

data class User(
    var self : DocumentReference,
    var email: String,
    var uid: String,
    var chatrooms : List<Chatroom> = emptyList()
) {
    fun newChat(other : User) {
        val chat = FirebaseUtils.db().collection("chatrooms").document()

        chat.set(Chatroom(chat))

        // add references in both users
        this.self.update("chatrooms", FieldValue.arrayUnion(chat))
        other.self.update("chatrooms", FieldValue.arrayUnion(chat))
    }

    // simply writes any changes in this object to the database
    fun update() {
        self.set(this)
    }
}