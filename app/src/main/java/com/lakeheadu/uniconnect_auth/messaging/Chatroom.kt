package com.lakeheadu.uniconnect_auth.messaging

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils.getCurrentUser

data class Chatroom(val self : DocumentReference) {

    fun sendMessage(str : String) {
        getCurrentUser().addOnSuccessListener {
            val user = it?.toObject<User>()

            // create our message
            val m = Message(user!!.self, str)

            self.collection("messages").document().set(m)
        }
    }

    /*
        note that CollectionReference has methods orderBy(), limit()
        see here for details:
        https://firebase.google.com/docs/firestore/query-data/order-limit-data
     */
    fun messages() : CollectionReference {
        return self.collection("messages")
    }
}