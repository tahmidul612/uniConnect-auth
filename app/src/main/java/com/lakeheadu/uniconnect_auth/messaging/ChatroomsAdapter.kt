package com.lakeheadu.uniconnect_auth.messaging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
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


class ChatroomsAdapter(list: List<Chatroom>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    private val chatroomsList = mutableListOf<Chatroom>()

    init {
        for (chat in list) {
            chatroomsList.add(chat)
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
                    chatroomsList.clear()

                    for (result in list) {
                        val chatroom = result!!.toObject<Chatroom>()
                        chatroomsList.add(chatroom!!)
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
        val item = chatroomsList[position]
        var latestMsg : Message
        var sender : User
        item.getAllMessages().orderBy("timestamp", Query.Direction.ASCENDING).limitToLast(1).addSnapshotListener { value, error ->
            if (value != null && !value.isEmpty) {
                latestMsg = value.toObjects<Message>()[0]
                latestMsg.user!!.get().addOnSuccessListener { value ->
                    sender = value.toObject<User>()!!
                    cardView.chatRoomCardUser.text =  sender.email
                }
                cardView.chatRoomCardLatestMsg.text = latestMsg.content
            }
        }

    }

    override fun getItemCount(): Int {
        return chatroomsList.count()
    }
}

