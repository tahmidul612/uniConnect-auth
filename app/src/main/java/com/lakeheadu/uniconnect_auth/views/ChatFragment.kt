package com.lakeheadu.uniconnect_auth.views

import android.os.Bundle

class ChatFragment{

    private lateinit var viewModel: ChatViewModel

    private var chatAdapter = ChatAdapter(mutableListOf())

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        chatAdapter.data=viewModel.messages
        subscribeOnAddMessage()
    }

    private fun subscribeOnAddMessage() {
        viewModel.notifyNewMessageInsertedLiveData.observe(this, Observer {
            chatAdapter.notifyItemInserted(it)
        })
    }


    override fun initView() {
        super.initView()
        bot_conversation.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                .apply {
                    stackFromEnd = true
                    isSmoothScrollbarEnabled = true
                }
            adapter = chatAdapter
        }

        input_layout.onSendClicked = {
            viewModel.sendMessage(it)
            hideKeyboard(activity)
        }
    }
}