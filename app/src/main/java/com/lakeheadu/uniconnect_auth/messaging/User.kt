package com.lakeheadu.uniconnect_auth.messaging


import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils
import java.util.*

/**
 *  this file implemented by Craig
 */

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
{
    /**
     * Answers a request to be added to a chat
     *
     * @param accept whether the current user accepts it or not
     */
    fun answerChatRequest(accept: Boolean) {
        FirebaseUtils.answerChatRequest(this, accept)
    }
}

/**
 * represents a request for an appointment with another user
 *
 * @property docRef self referential document reference (type appointmentRequest)
 * @property requester User who requested (type User)
 * @property requestDate date for appointment
 */

data class appointmentRequest(
    var docRef: DocumentReference,
    var requester: DocumentReference,
    val requestDate: Date
)
{
    /**
     * answers a request for an appointment
     *
     * @param accept whether the current user accepts it or not
     */
    fun answerAppointmentRequest(accept : Boolean) {
        FirebaseUtils.answerAppointmentRequest(this, accept)
    }
}
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
    var docRef: DocumentReference? = null,
    var email: String = "",
    var uid: String = "",
    // ideally userType is an enum, but firebase does not play well with enums
    var userType: String = "",
    var department: String = "",
    var displayName: String = "",
    var chatrooms: MutableList<DocumentReference> = mutableListOf(),
    var firstName: String = "",
    var lastName: String = "",
) {




    /**
     * updates the name of this user and writes to Firestore
     *
     * @param new_name new displayName of this user
     */
    fun updateDisplayName(new_name: String) {
        displayName = new_name
        update()
    }

    /**
     * updates the department of this user and writes to Firestore
     *
     * @param new_dept new department of this user
     */
    fun updateDepartment(new_dept: String) {
        department = new_dept
        update()
    }

    /**
     * updates the userType of this user and writes it to Firestore
     *
     * @param new_type the new type
     */
    fun updateUserType(new_type: String) {
        userType = new_type
        update()
    }

    /**
     * create a new chatroom with this user.
     *
     */
    fun newChat() {
        FirebaseUtils.newChat(this)

    }

    /**
     * request this user for an appointment
     *
     * @param date the date of this appointment
     */
    fun requestAppointment(date: Date) {
        FirebaseUtils.requestAppointment(this, date)
    }

    /**
     * leaves a chatroom
     * @param chat the chatroom to leave
     */
    fun leaveChat(chat: Chatroom) {
        chatrooms.remove(chat.docRef)
        update()
    }


    /**
     * get all new chat requests.
     *
     * @return a CollectionReference (type list of chatRequests)
     */
    fun getChatRequests(): CollectionReference {
        return this.docRef!!.collection("chat_requests")
    }

    /**
     * get all appointment requests
     *
     * @return a CollectionReference (type list of appointmentRequests)
     */
    fun getAppointmentRequests(): CollectionReference {
        return this.docRef!!.collection("appointment_requests")
    }

    /**
     * deletes this user account. also signs out
     *
     */
    fun deleteAccount() {
        docRef!!.delete()
        FirebaseUtils.firebaseUser?.delete()
        FirebaseUtils.signOut()
    }

    /**
     * updates this object in firestore
     *
     */
    fun update() {
        val map = hashMapOf(
            "docRef" to docRef,
            "email" to email,
            "uid" to uid,
            "userType" to userType,
            "department" to department,
            "displayName" to displayName,
            "chatrooms" to chatrooms,
            "firstName" to firstName,
            "lastName" to lastName
        )
        docRef!!.set(map)
    }

}