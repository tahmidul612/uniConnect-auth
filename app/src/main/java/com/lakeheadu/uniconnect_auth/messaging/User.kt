package com.lakeheadu.uniconnect_auth.messaging

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils
import java.util.*

/**
 * represents a request from user to another to join a chat
 *
 * @property docRef Self-referential document reference (type chatRequest)
 * @property requester User who requested (type User)
 * @property chatDoc reference to the chat (type Chatroom)
 */
data class chatRequest(
    var docRef: DocumentReference,
    var requester: DocumentReference,
    var chatDoc: DocumentReference
)

/**
 * represents a request for an appointment with another user
 *
 * @property docRef self referential document reference (type appointmentRequest)
 * @property requester User who requested (type User)
 * @property requestDate date for appointment
 */

data class appointmentRequest(
    var docRef: DocumentReference,
    var requester : DocumentReference,
    val requestDate: Date
)

/**
 * An object representing a user.
 *
 * @property docRef DocumentReference to this object in Firestore.
 * @property displayName chosen display name of this user
 * @property email email address of this user
 * @property uid user_id of this user (equivalent to FirebaseUser?.uid)
 * @property userType type of user e.g. student, professor, admin etc
 * @property department department this user belongs to, e.g. Computer Science, Physics, etc
 * @property displayName name chosen by user
 * @property chatrooms list of chatrooms which this user is a part of.
 */
data class User(
    var docRef: DocumentReference,
    var email: String,
    var uid: String,
    // ideally userType is an enum, but firebase does not play well with enums
    var userType: String = "",
    var department: String = "",
    var displayName: String = "",
    var chatrooms: MutableList<Chatroom> = mutableListOf()
) {

    fun updateName(new_name: String) {
        displayName = new_name
        update()
    }


    /**
     * create a new chatroom.
     * @param other another user
     */
    fun newChat(other: User) {
        val chat = FirebaseUtils.newChat()

        chat.addUser(this)
        chat.addUser(other)
        update()
    }

    /**
     * leaves a chatroom
     * @param chat the chatroom to leave
     */
    fun leaveChat(chat: Chatroom) {
        chatrooms.remove(chat)
        update()
    }

    fun getChatRequests(): CollectionReference {
        return this.docRef.collection("chat_requests")
    }

    fun getAppointmentRequests() : CollectionReference {
        return this.docRef.collection("appointment_requests")
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