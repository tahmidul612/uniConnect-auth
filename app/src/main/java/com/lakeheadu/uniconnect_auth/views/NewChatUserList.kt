package com.lakeheadu.uniconnect_auth.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.toObjects
import com.lakeheadu.uniconnect_auth.R
import com.lakeheadu.uniconnect_auth.messaging.ChatroomsAdapter
import com.lakeheadu.uniconnect_auth.messaging.User
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils
import kotlinx.android.synthetic.main.fragment_new_chat_user_list_list.*

/**
 * A fragment representing a list of Items.
 * @author tahmidul
 */
class NewChatUserList : Fragment() {

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: NewChatUserListAdapter
    private lateinit var users : List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseUtils.getAllUsers().get().addOnSuccessListener { value ->
            users = value.toObjects<User>()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_chat_user_list_list, container, false)
        layoutManager = LinearLayoutManager(context)
        userlist.layoutManager = layoutManager
        adapter = NewChatUserListAdapter(users)
        userlist.adapter = adapter
        return view
    }
}