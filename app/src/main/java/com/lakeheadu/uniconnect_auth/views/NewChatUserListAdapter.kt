package com.lakeheadu.uniconnect_auth.views

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lakeheadu.uniconnect_auth.R
import com.lakeheadu.uniconnect_auth.databinding.FragmentNewChatUserListBinding
import com.lakeheadu.uniconnect_auth.messaging.User

class NewChatUserListAdapter(
    private val values: List<User>
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
        val item = values[position]
        holder.useremailview.text = item.email

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentNewChatUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val useremailview: TextView = binding.userListUserEmail
    }

}