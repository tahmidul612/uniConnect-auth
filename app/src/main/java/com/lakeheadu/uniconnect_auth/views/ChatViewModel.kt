package com.lakeheadu.uniconnect_auth.views

import androidx.lifecycle.MutableLiveData
import com.lakeheadu.uniconnect_auth.utils.MessageItemUi

class ChatViewModel (private val sendChatMessageUseCase: CompletableUseCase<SendMessageParams>,
                     private val openChatChannelUseCase: ObservableUseCase<String>){



    var messages= mutableListOf<MessageItemUi>()
    var notifyNewMessageInsertedLiveData = MutableLiveData<Int>()

    fun openChatChannel() {
        openChatChannelUseCase.getObservable().subscribeBy(onNext = {
            messagesData.add(MessageItemUi(it,MessageItemUi.TYPE_FRIEND_MESSAGE))
            notifyNewMessageInsertedLiveData.value = messagesData.size

        }
        )
    }


    fun sendMessage(message: String) {
        messagesData.add(MessageItemUi(message, MessageItemUi.TYPE_MY_MESSAGE))
        notifyNewMessageInsertedLiveData.value = messagesData.size
        sendChatMessageUseCase.getCompletable(SendMessageParams(message)).subscribeBy(onComplete = {
        })
    }

}