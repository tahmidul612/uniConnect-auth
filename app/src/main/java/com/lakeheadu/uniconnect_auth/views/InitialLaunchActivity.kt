package com.lakeheadu.uniconnect_auth.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lakeheadu.uniconnect_auth.R
import kotlinx.android.synthetic.main.activity_initial_launch.*

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