package com.lakeheadu.uniconnect_auth.messaging

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


/**
 * An object representing a message in a chatroom.
 *
 * @property user DocumentReference to the author
 * @property content content of message
 * @property timestamp timestamp of the message
 */
data class Message(
    val user : DocumentReference,
    val content : String,
    @ServerTimestamp
    val timestamp : Date? = null
)

