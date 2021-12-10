package com.lakeheadu.uniconnect_auth.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lakeheadu.uniconnect_auth.R
import com.lakeheadu.uniconnect_auth.extensions.Extensions.toast
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (!FirebaseUtils.alreadyLoggedIn()) {
            startActivity(Intent(this, CreateAccountActivity::class.java))
            finish()
        }
        else {
            // sign out a user
            btnSignOut.setOnClickListener {
                FirebaseUtils.signOut()
                startActivity(Intent(this, CreateAccountActivity::class.java))
                toast("signed out")
                finish()
            }
            newChat.setOnClickListener {
                val newChatRoom = FirebaseUtils.newChat()
                toast("send new message")
            }
        }
    }
}