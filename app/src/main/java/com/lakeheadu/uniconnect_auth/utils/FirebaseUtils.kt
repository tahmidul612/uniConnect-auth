package com.lakeheadu.uniconnect_auth.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.lakeheadu.uniconnect_auth.messaging.Chatroom
import com.lakeheadu.uniconnect_auth.messaging.Message
import com.lakeheadu.uniconnect_auth.messaging.User

/** HOW TO USE THESE METHODS
 *
 *  In firebase, we have two main ideas: documents and collections. A collection is a collection
 *  of documents. For example, the collection "users" contains documents which contains each user's
 *  info. Each collection has a path, for example "/users" is the path for the users collection.
 *
 *  A document is essentially an object that is storable in firestore. Documents also have paths, e.g.
 *  "/users/my_uid" is the path to a document called "my_uid" inside of the users collection.
 *
 *  With that in mind, many of these methods return DocumentReferences or CollectionReferences.
 *  Note that these _ARE NOT_ pieces of actual data, but literal references to what is in firestore.
 *
 *  Noteworthy reference methods
 *      addSnapshotListener
 *      delete
 *      parent
 *
 *  https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/CollectionReference
 *  https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/DocumentReference
 *
 *  While you are able to "listen" to a reference, in order to actually get the value of a reference,
 *  you must call .get() on a reference. Note that this is all asynchronous, so you must add a
 *  callback in order to do stuff with the data.
 *
 *  Noteworthy Task<T> methods
 *      addOnSuccessListener
 *      addOnCompleteListener
 *      addOnFailureListener
 *      result
 *
 *  https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/DocumentSnapshot
 *  https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/QuerySnapshot
 *
 *  Examples:
 *
 *      // get the current user
 *      getCurrentUser().get().addOnSuccessListener { snapshot ->
 *          val user = snapshot.toObject<User>()
 *      }
 *
 *      // get all users into a list
 *      getAllUsers().get().addOnSuccessListener { snapshot ->
 *          val list = snapshot.toObjects<User>()
 *      }
 *
 */

object FirebaseUtils {
    val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    var firebaseUser = firebaseAuth.currentUser

    private fun db(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    /**
     * get a user by their uid. Check top of FirebaseUtils.kt for detail on how to
     * use this return type
     *
     * @param uid a user's uid
     * @return a DocumentReference
     */
    fun getUser(uid : String) : DocumentReference {
        return db().collection("users").document(uid)
    }

    /**
     * get the current user's document reference. Check top of FirebaseUtils.kt for details
     * on how to use this return type
     *
     * @return a DocumentReference to the current user
     */

    fun getCurrentUser() : DocumentReference {
        return getUser(firebaseUser!!.uid)
    }


    /**
     *  get a list of all users. note that you must call ".get()" on this object
     *  in order to actually do anything. Check top of FirebaseUtils.kt for details
     *  on how to use this return type
     * @return an asynchronous CollectionReference, which can be turned into a list
     */
    fun getAllUsers() : CollectionReference {
        return db().collection("users")
    }

    /**
     * send a message to a chat.
     *
     * @param chat chatroom instance
     * @param msg message contents, in a string
     */
    fun sendMessage(chat : Chatroom, msg : String) {
        getCurrentUser().get().addOnSuccessListener {
            val user = it?.toObject<User>()

            // create our message
            val m = Message(user!!.self, msg)

            chat.self.collection("messages").document().set(m)
        }
    }

    /**
     * create a new Chatroom object.
     *
     * @return a new chatroom
     */
    fun newChat() : Chatroom {
        val doc = db().collection("chatrooms").document()

        val ret = Chatroom(doc)
        doc.set(ret)

        return ret
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
            // make sure to update firebaseUser
            firebaseUser = firebaseAuth.currentUser
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
        task.addOnCompleteListener {
            login(
                email,
                password
            ).addOnCompleteListener {
                // we could use this to initialize more info in db e.g. name, major, etc
                firebaseUser?.let {
                    // do this kinda manually, so we can add reference after
                    val doc = db().collection("users").document(it.uid)

                    val u = User(doc, email, it.uid)

                    u.update()
                }
            }
        }
        return task
    }

    /**
     * checks if current user is already logged in.
     *
     * @return
     */
    fun alreadyLoggedIn() : Boolean {
        firebaseUser?.let{
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
    }
}