package com.lakeheadu.uniconnect_auth.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.lakeheadu.uniconnect_auth.R
import com.lakeheadu.uniconnect_auth.messaging.Chatroom
import com.lakeheadu.uniconnect_auth.messaging.ChatroomsAdapter
import com.lakeheadu.uniconnect_auth.messaging.User
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils
import kotlinx.android.synthetic.main.activity_home.*

private lateinit var layoutManager: RecyclerView.LayoutManager
private lateinit var adapter: ChatroomsAdapter

/**
 * The home screen of the app
 * Displays a RecyclerView list of the email address of
 * the sender and the latest text in each chatroom
 *
 * this file implemented by Tahmidul, with help from Craig
 * @author tahmidul, craig
 */

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (!FirebaseUtils.alreadyLoggedIn()) {
            startActivity(Intent(this, InitialLaunchActivity::class.java))
            finish()
        } else {

            /*  test requesting for new chat, might not work since this gets called fast and
                FirebaseUtil.user must be updated in time
            val doc = FirebaseUtils.getUserDoc("m6maUKuk19M5NlddsCkZQAdzMek2")

            doc.get().addOnSuccessListener { u ->
                val other = u.toObject<User>()

                other?.newChat()
            }*/


            // get all the chatrooms to pass to recycler view
            FirebaseUtils.getCurrentUserDoc().get().addOnSuccessListener {
                it?.let {
                    val user = it.toObject<User>()
                    val tasks = mutableListOf<Task<DocumentSnapshot>>()

                    for (ref in user!!.chatrooms) {
                        val refTask = ref.get()
                        tasks.add(refTask)
                    }
                    Tasks.whenAllSuccess<DocumentSnapshot>(tasks).addOnSuccessListener { list ->
                        val chatrooms = mutableListOf<Chatroom>()
                        for (result in list) {
                            val chat = result!!.toObject<Chatroom>()
                            chatrooms.add(chat!!)
                        }

                        layoutManager = LinearLayoutManager(this)
                        recycler1.layoutManager = layoutManager
                        adapter = ChatroomsAdapter(chatrooms)
                        recycler1.adapter = adapter
                    }
                }

            }
            newChat.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .add(R.id.userlist, NewChatUserList()).commit()
            }
        }
    }

}