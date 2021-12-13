package com.lakeheadu.uniconnect_auth.views

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.lakeheadu.uniconnect_auth.R
import com.lakeheadu.uniconnect_auth.extensions.Extensions.toast
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils.register
import kotlinx.android.synthetic.main.activity_create_account.*

/**
 * this file implemented by Tahmidul
 *
 */

class CreateAccountActivity : AppCompatActivity() {
    lateinit var createAccountInputsArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        createAccountInputsArray = arrayOf(etEmail, etPassword, etConfirmPassword)
        btnCreateAccount2.setOnClickListener {
            signup()
        }
    }

    private fun signup() {
        val userEmail = etEmail.text.toString().trim()
        val userPassword = etPassword.text.toString().trim()
        if (identicalPassword()) {
            register(userEmail, userPassword)
                .addOnCompleteListener { signup ->
                    if (signup.isSuccessful) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        toast("registered successfully")
                        finish()
                    } else {
                        toast("registration failed")
                    }
                }
        }
    }

    private fun notEmpty(): Boolean = etEmail.text.toString().trim().isNotEmpty() &&
            etPassword.text.toString().trim().isNotEmpty() &&
            etConfirmPassword.text.toString().trim().isNotEmpty()

    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() &&
            etPassword.text.toString().trim() == etConfirmPassword.text.toString().trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        } else {
            toast("passwords are not matching !")
        }
        return identical
    }


    /* send verification email to the new user. This will only
    *  work if the firebase user is not null.
    */

    /*private fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("email sent to $userEmail")
                }
            }
        }
    }*/
}