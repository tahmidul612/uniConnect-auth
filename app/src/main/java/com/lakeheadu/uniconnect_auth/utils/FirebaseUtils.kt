package com.lakeheadu.uniconnect_auth.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.lakeheadu.uniconnect_auth.messaging.User


object FirebaseUtils {
    val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    var firebaseUser = firebaseAuth.currentUser

    fun db(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    // similar to getCurrentUser
    fun getUser(uid : String) : Task<DocumentSnapshot> {
        return db().collection("users").document(uid).get()
    }

    /*  example:
        getCurrentUser().addOnSuccessListener { snapshot ->
            val user = snapshot.toObject<User>()
        }
     */
    fun getCurrentUser() : Task<DocumentSnapshot> {
        return getUser(firebaseUser!!.uid)
    }

    /*  example:
        getAllUsers().addOnSuccessListener { snapshot ->
            val list = snapshot.toObjects<User>()
        }

     */
    fun getAllUsers() : Task<QuerySnapshot> {
        return db().collection("users").get()
    }

    fun login(email: String, password: String): Task<AuthResult> {
        val task = firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            // make sure to update firebaseUser
            firebaseUser = firebaseAuth.currentUser
        }
        return task
    }

    // on successful registration, it will also attempt to login
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
}