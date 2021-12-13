package com.lakeheadu.uniconnect_auth.views

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.lakeheadu.uniconnect_auth.databinding.FragmentNewChatUserListBinding
import com.lakeheadu.uniconnect_auth.messaging.User

/**
 * Adapter class to populate user list
 * @author tahmidul
 */

class NewChatUserListAdapter(
    private val list: List<User>
) : RecyclerView.Adapter<NewChatUserListAdapter.ViewHolder>() {

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentNewChatUserListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.useremailview.text = item.email

    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(binding: FragmentNewChatUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val useremailview: TextView = binding.userListUserEmail
    }

}