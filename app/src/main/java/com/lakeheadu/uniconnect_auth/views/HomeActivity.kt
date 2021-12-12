package com.lakeheadu.uniconnect_auth.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.toObject
import com.lakeheadu.uniconnect_auth.R
import com.lakeheadu.uniconnect_auth.extensions.Extensions.toast
import com.lakeheadu.uniconnect_auth.messaging.Chatroom
import com.lakeheadu.uniconnect_auth.messaging.ChatroomsAdapter
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils
import kotlinx.android.synthetic.main.activity_home.*

private lateinit var layoutManager: RecyclerView.LayoutManager
private lateinit var adapter: ChatroomsAdapter

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (!FirebaseUtils.alreadyLoggedIn()) {
            startActivity(Intent(this, InitialLaunchActivity::class.java))
            finish()
        }
        else {
            layoutManager = LinearLayoutManager(this)
            recycler1.layoutManager = layoutManager
            adapter = ChatroomsAdapter()
            recycler1.adapter = adapter
            }
        }

    }