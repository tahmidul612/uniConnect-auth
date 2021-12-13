package com.lakeheadu.uniconnect_auth.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lakeheadu.uniconnect_auth.R
import kotlinx.android.synthetic.main.activity_initial_launch.*

/**
 * this file implemented by Tahmidul
 *
 */

/**
 * Activity launched for new or logged out users
 * User can choose to sign up or sign in
 * @author tahmidul
 */

class InitialLaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_launch)
        btnSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
    }
}