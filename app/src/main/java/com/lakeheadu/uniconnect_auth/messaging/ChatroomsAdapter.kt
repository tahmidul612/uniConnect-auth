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

/**
 *  this file implemented by Tahmidul, which some help from Craig
 */

/**
 * Adapter to populate RecyclerView list with information from
 * all open chatrooms
 * @author tahmidul, craig
 */

val list_of_chatrooms = mutableListOf<Chatroom>()

class ChatroomsAdapter(list: List<Chatroom>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    init {
        for (chat in list) {
            list_of_chatrooms.add(chat)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

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

