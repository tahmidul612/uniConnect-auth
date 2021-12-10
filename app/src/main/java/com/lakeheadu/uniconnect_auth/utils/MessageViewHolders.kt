package com.lakeheadu.uniconnect_auth.utils

import android.view.View
import android.widget.TextView
import com.lakeheadu.uniconnect_auth.R


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