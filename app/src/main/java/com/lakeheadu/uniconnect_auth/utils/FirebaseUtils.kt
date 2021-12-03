package com.lakeheadu.uniconnect_auth.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtils {
    val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    var firebaseUser = firebaseAuth.currentUser

    private fun db(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    fun login(email: String, password: String): Task<AuthResult> {
        val task = firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            // make sure to update firebaseUser
            firebaseUser = firebaseAuth.currentUser
        }
        return task
    }

    fun register(email: String, password: String): Task<AuthResult> {
        val task = firebaseAuth.createUserWithEmailAndPassword(email, password)

        task.addOnCompleteListener {
            login(
                email,
                password
            ).addOnCompleteListener {
                // we could use this to initialize more info in db e.g. name, major, etc
                val test = hashMapOf("name" to "test")
                firebaseUser?.let {
                    db().collection("users").document(it.uid).set(test)
                }
            }
        }
        return task
    }
}