package com.lakeheadu.uniconnect_auth.views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lakeheadu.uniconnect_auth.R
import com.lakeheadu.uniconnect_auth.utils.MessageItemUi
import com.lakeheadu.uniconnect_auth.utils.MessageItemUi.Companion.TYPE_FRIEND_MESSAGE
import com.lakeheadu.uniconnect_auth.utils.MessageItemUi.Companion.TYPE_MY_MESSAGE
import com.lakeheadu.uniconnect_auth.utils.MessageViewHolder

class ChatAdapter(var data: MutableList<MessageItemUi>) : RecyclerView.Adapter<MessageViewHolder<*>>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder<*> {
        val context = parent.context
        return when (viewType) {
            TYPE_MY_MESSAGE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.my_message_item, parent, false)
                MyMessageViewHolder(view)
            }
            TYPE_FRIEND_MESSAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_message_item, parent, false)
                FriendMessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder<*>, position: Int) {
        val item = data[position]
        Log.d("adapter View", position.toString() + item.content)
        when (holder) {
            is MyMessageViewHolder -> holder.bind(item)
            is FriendMessageViewHolder -> holder.bind(item)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = data[position].messageType

    class MyMessageViewHolder(val view: View) : MessageViewHolder<MessageItemUi>(view) {
        private val messageContent = view.findViewById<TextView>(R.id.message)

        override fun bind(item: MessageItemUi) {
            messageContent.text = item.content
            messageContent.setTextColor(item.textColor)
        }
    }
    class FriendMessageViewHolder(val view: View) : MessageViewHolder<MessageItemUi>(view) {
        private val messageContent = view.findViewById<TextView>(R.id.message)

        override fun bind(item: MessageItemUi) {
            messageContent.text = item.content
            messageContent.setTextColor(item.textColor)
        }


    }
}