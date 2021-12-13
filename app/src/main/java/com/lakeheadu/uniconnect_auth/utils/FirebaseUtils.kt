package com.lakeheadu.uniconnect_auth.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.lakeheadu.uniconnect_auth.messaging.*
import java.util.*

/**
 *  this file implemented by Craig
 */

/** HOW TO USE THESE METHODS
 *
 *  In firebase, we have two main ideas: documents and collections. A collection is a collection
 *  of """documents""". For example, the collection "users" contains documents which contains each user's
 *  info. Each collection has a path, for example "/users" is the path for the users collection.
 *
 *  A document is essentially an object that is storable in firestore. Documents also have paths, e.g.
 *  "/users/my_uid" is the path to a document called "my_uid" inside of the users collection.
 *
 *  With that in mind, many of these methods return DocumentReferences or CollectionReferences.
 *  Note that these _ARE NOT_ pieces of actual data, but literal references to what is in firestore.
 *  DocumentReference and CollectionReference inherit from Query, which allows for things such as ordering,
 *  only returning objects which match certain criteria, etc.
 *
 *  Noteworthy reference methods
 *      addSnapshotListener
 *      delete
 *      parent
 *
 *  https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/CollectionReference
 *  https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/DocumentReference
 *
 *  Noteworthy Query methods
 *      limit
 *      limitToLast
 *      orderBy
 *      whereEqualTo
 *      whereGreaterThan
 *      whereIn
 *      ...
 *
 *  https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/Query
 *--------------------------------------------------------------------------------------------------
 *  While you are able to "listen" to a reference, in order to actually get the value of a reference,
 *  you must call .get() on a reference. Note that this is all asynchronous, so you must add a
 *  callback in order to use the data. get() method on references returns an asynchronus Task<T>, where
 *  T depends on what exactly you're trying to get. As a simple example, to register via Firebase, we use
 *  createUserWithEmailAndPassword() which returns a Task<AuthResult>.
 *
 *  Noteworthy Task<T> methods
 *      addOnSuccessListener
 *      addOnCompleteListener
 *      addOnFailureListener
 *      result
 *
 *  https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/DocumentSnapshot
 *  https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/QuerySnapshot
 *  https://firebase.google.com/docs/firestore/query-data/order-limit-data
 *--------------------------------------------------------------------------------------------------
 *  Examples
 *--------------------------------------------------------------------------------------------------
 *      // get the DocumentReference for this user
 *      val doc = getCurrentUser()
 *
 *      // add a listener for when get() actually returns (asynchronously)
 *      // NOTE: addOnSuccessListener is a method of Task<T>
 *      doc.get().addOnSuccessListener { value ->
 *        // convert the value returned by get() into User type
 *          val user = value?.toObject<User>()
 *      }
 *
 *      // add a snapshot listener, this lambda will be called everytime Firestore
 *      // tells us this document has been updated.
 *      // NOTE: addSnapShotListener is a method of DocumentReference
 *      doc.addSnapshotListener { value, error ->
 *          if (value != null && value.exists()) {
 *              val user = value.toObject<User>()
 *          }
 *      }
 *--------------------------------------------------------------------------------------------------
 *      // get all users into a list
 *      // NOTE: getAllUsers() returns a CollectionReference
 *      getAllUsers().get().addOnSuccessListener { value ->
 *          val list = value.toObjects<User>()
 *      }
 *--------------------------------------------------------------------------------------------------
 *      // listen for changes to a CollectionReference
 *      getAllUsers().addSnapshotListener { value, error ->
 *          if (value != null && value.exists()) {
 *              val users = value.toObjects<User>()
 *          }
 *      }
 *--------------------------------------------------------------------------------------------------
 *      // a more involved example, listen for new messages in a chatroom
 *      val chat : Chatroom
 *
 *      // getAllMessages returns a CollectionReference
 *      val messages = chat.getAllMessages()
 *
 *      // listen for updates to the 10 most recent messages, ordering by timestamp
 *      messages.orderBy("timestamp", Query.Direction.ASCENDING).limitToLast(10).addOnSnapShotListener { value, error ->
 *          ...
 *      }
 *--------------------------------------------------------------------------------------------------
 *      // search for a user with display name "John Smith"
 *      getAllUsers().whereEqualTo("displayName", "John Smith").get().addOnSuccessListener {
 *          ...
 *      }
 *--------------------------------------------------------------------------------------------------
 *      // get list of all chatrooms for a user
 *
 *      val user
 *
 *      val tasks = mutableListOf<Task<DocumentSnapshot>>()
 *
 *       for (ref in user.chatrooms) {
 *           val refTask = ref.get()
 *           tasks.add(refTask)
 *       }
 *       Tasks.whenAllSuccess<DocumentSnapshot>(tasks).addOnSuccessListener {
 *           val list = mutableListOf<Chatroom>()
 *
 *           for (result in it) {
 *               result?.let { chat ->
 *                   chat.toObject<Chatroom>()?.let { obj ->
 *                       list.add(obj)
 *                   }
 *               }
 *           }
 *       }
 *--------------------------------------------------------------------------------------------------
 *
 *      // listen for changes to chatrooms list
 *      val list_of_chatrooms = mutableListOf<Chatrooms>()
 *
 *      FirebaseUtils.getCurrentUserDoc().addOnSnapshotListener {
 *          if (value != null && value.exists()) {
 *              val user = value.toObject<User>()
 *              val tasks = mutableListOf<Task<DocumentSnapshot>>()
 *
 *              for (ref in user.chatrooms) {
 *                  val refTask = ref.get()
 *                  tasks.add(refTask)
 *              }
 *              Tasks.whenAllSuccess<DocumentSnapshot>(tasks).addOnSuccessListener {
 *                  for (result in it) {
 *                      result?.let { chat ->
 *                          chat.toObject<Chatroom>()?.let { obj ->
 *                              list_of_chatrooms.add(obj)
 *                          }
 *                      }
 *                  }
 *              }
 *          }
 *      }
 *--------------------------------------------------------------------------------------------------
 *      // log all messages in every chat
 *      FirebaseUtils.getCurrentUserDoc().get().addOnSuccessListener {
            val tasks = mutableListOf<Task<DocumentSnapshot>>()

            val user = it?.toObject<User>()

            for (ref in user!!.chatrooms) {
                val refTask = ref.get()
                tasks.add(refTask)
            }

            Tasks.whenAllSuccess<DocumentSnapshot>(tasks).addOnSuccessListener { chatroomList ->

                for (result in chatroomList) {
                    val chatroom = result!!.toObject<Chatroom>()
                    chatroom!!.getAllMessages().get().addOnSuccessListener { it ->
                        it?.let {
                            val msgs = it.toObjects<Message>()

                            for (m in msgs) {
                                // do the logging now
                                Log.d("test", m.content)
                            }

                        }
                    }

                }
            }
        }
 */

object FirebaseUtils {
    val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    var firebaseUser = firebaseAuth.currentUser
    var user: User? = null

    private fun db(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    private fun update_user(): Task<DocumentSnapshot> {
        firebaseUser = firebaseAuth.currentUser
        val doc = getCurrentUserDoc()
        val task = doc.get()

        task.addOnSuccessListener {
            it?.let {
                user = it.toObject<User>()
            }
        }
        doc.addSnapshotListener { value, error ->

            if (value != null && value.exists()) {
                user = value.toObject<User>()
            }
        }
        return task
    }

    /**
     * get a user by their uid. Check top of FirebaseUtils.kt for detail on how to
     * use this return type
     *
     * @param uid a user's uid
     * @return a DocumentReference
     */
    fun getUserDoc(uid: String): DocumentReference {
        return db().collection("users").document(uid)
    }

    /**
     * get the current user's document reference. Check top of FirebaseUtils.kt for details
     * on how to use this return type
     *
     * @return a DocumentReference to the current user
     */

    fun getCurrentUserDoc(): DocumentReference {
        return getUserDoc(firebaseUser!!.uid)
    }

    /**
     *  get a list of all users. note that you must call ".get()" on this object
     *  in order to actually do anything. Check top of FirebaseUtils.kt for details
     *  on how to use this return type
     * @return an asynchronous CollectionReference, which can be turned into a list
     */
    fun getAllUsers(): CollectionReference {
        return db().collection("users")
    }

    /**
     * send a message to a chat.
     *
     * @param chat chatroom instance
     * @param msg message contents, in a string
     */
    fun sendMessage(chat: Chatroom, msg: String) {
        user?.let {
            val m = Message(it.docRef!!, msg)
            chat.getAllMessages().document().set(m)
        }
    }

    fun inviteUserToChat(chat: Chatroom, u: User) {
        user?.let {
            val doc = u.docRef!!.collection("chat_requests").document()

            val invite = chatRequest(doc, it.docRef!!, chat.docRef!!)

            // write it to doc now
            doc.set(invite)
        }

    }


    fun requestAppointment(u: User, time: Date) {
        user?.let {
            val doc = u.docRef!!.collection("appointment_requests").document()

            val r = appointmentRequest(doc, u.docRef!!, time)

            doc.set(r)
        }
    }

    fun answerAppointmentRequest(req: appointmentRequest, accept: Boolean) {
        user?.let {
            if (accept) {
                /*  notify the sender somehow. ideas:
                        sender has collection "appointments", which is passed in appointmentRequest
                        that contains result
                        some more global collection of all appointments
                        writing to collection of sender "appointmentAnswers" or something similar
                 */

            }
        }
    }

    /**
     * create a new Chatroom object.
     *
     * @return a new chatroom
     */
    fun newChat(other: User) {
        user?.let {
            val doc = db().collection("chatrooms").document()

            val chatroom = Chatroom(doc)

            chatroom.update().addOnSuccessListener { void ->
                it.chatrooms.add(doc)

                inviteUserToChat(chatroom, other)

                it.update()
            }

        }
    }

    fun answerChatRequest(req: chatRequest, accept: Boolean) {
        user?.let {
            if (accept) {
                it.chatrooms.add(req.chatDoc)
                it.update()
            }
            // delete the request now
            req.docRef.delete()
        }
    }

    /**
     * login feature
     *
     * @param email user's email
     * @param password user's password
     * @return an asynchronous task that will contain the result of authentication.
     */
    fun login(email: String, password: String): Task<AuthResult> {
        val task = firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            // update FirebaseUtil's data for current user
            update_user()
        }
        return task
    }

    /**
     * register feature
     *
     * @param email user's email
     * @param password user's password
     * @return an asynchronous task that will contain the result of registration
     */
    fun register(email: String, password: String): Task<AuthResult> {
        val task = firebaseAuth.createUserWithEmailAndPassword(email, password)
        task.addOnSuccessListener {


            firebaseUser = firebaseAuth.currentUser

            val doc = db().collection("users").document(firebaseUser!!.uid)

            val u = User(doc, email, firebaseUser!!.uid)
            //doc.set("test")
            u.update()


        }
        return task
    }

    /** checks if current user is already logged in.
     *
     * @return
     */
    fun alreadyLoggedIn(): Boolean {
        firebaseUser?.let {
            update_user()
            return true
        }
        return false
    }

    /**
     * signs out the currently logged in user.
     *
     */
    fun signOut() {
        firebaseAuth.signOut()
        firebaseUser = null
        user = null
    }

}