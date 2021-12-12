package com.lakeheadu.uniconnect_auth.messaging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.lakeheadu.uniconnect_auth.R
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils
import kotlinx.android.synthetic.main.chatrooms.view.*

val list_of_chatrooms = mutableListOf<Chatroom>()

class ChatroomsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // fill list of chatrooms on creation
        FirebaseUtils.getCurrentUserDoc().get().addOnSuccessListener {
            val tasks = mutableListOf<Task<DocumentSnapshot>>()

            val user = it?.toObject<User>()

            for (ref in user!!.chatrooms) {
                val refTask = ref.get()
                tasks.add(refTask)
            }

            Tasks.whenAllSuccess<DocumentSnapshot>(tasks).addOnSuccessListener { list ->

                for (result in list) {
                    val chatroom = result!!.toObject<Chatroom>()
                    list_of_chatrooms.add(chatroom!!)
                }
            }
        }

        // listen for changes e.g. new chats, removed chats etc
        FirebaseUtils.getCurrentUserDoc().addSnapshotListener { value, error ->
            if (value != null && value.exists()) {
                val user = value.toObject<User>()
                val tasks = mutableListOf<Task<DocumentSnapshot>>()

                for (ref in user?.chatrooms!!) {
                    val refTask = ref.get()
                    tasks.add(refTask)
                }
                Tasks.whenAllSuccess<DocumentSnapshot>(tasks).addOnSuccessListener { list ->
                    // clear out old list
                    list_of_chatrooms.clear()

                    for (result in list) {
                        val chatroom = result!!.toObject<Chatroom>()
                        list_of_chatrooms.add(chatroom!!)
                    }
                }
            }
        }
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.chatrooms, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val cardView = viewHolder.itemView
        val item = list_of_chatrooms[position]
        cardView.textView1.text = item.toString()
    }

    override fun getItemCount(): Int {
        return list_of_chatrooms.count()
    }
}

