package com.lakeheadu.uniconnect_auth.views

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.lakeheadu.uniconnect_auth.R
import com.lakeheadu.uniconnect_auth.extensions.Extensions.toast
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils.login
/** fix missing imports **/
import kotlinx.android.synthetic.main.activity_sign_in.*

/**
 * Verify the email and password input by the user with
 * data from the Firebase Database and login the user
 * @author tahmidul
 */

class SignInActivity : AppCompatActivity() {
    private lateinit var signInInputsArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        signInInputsArray = arrayOf(etSignInEmail, etSignInPassword)

        btnSignIn2.setOnClickListener {
            signInUser()
        }
    }

    private fun signInUser() {
        val signInEmail = etSignInEmail.text.toString().trim()
        val signInPassword = etSignInPassword.text.toString().trim()

        if (signInEmail.isNotEmpty() && signInPassword.isNotEmpty()) {
            login(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        toast("signed in successfully")
                        finish()
                    } else {
                        toast("sign in failed")
                    }
                }
        } else {
            signInInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }
    }
}