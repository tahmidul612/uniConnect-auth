package com.lakeheadu.uniconnect_auth.messaging

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


data class Message(
    val user : DocumentReference,
    val content : String,
    @ServerTimestamp
    val timestamp : Date? = null
)

