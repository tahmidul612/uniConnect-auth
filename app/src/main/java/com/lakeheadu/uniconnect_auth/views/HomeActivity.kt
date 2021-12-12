package com.lakeheadu.uniconnect_auth.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lakeheadu.uniconnect_auth.R
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
        } else {
            layoutManager = LinearLayoutManager(this)
            recycler1.layoutManager = layoutManager
            adapter = ChatroomsAdapter()
            recycler1.adapter = adapter
        }
    }

}