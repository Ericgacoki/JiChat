package com.ericg.jichat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericg.jichat.MainActivity.Companion.getTime
import com.ericg.jichat.data.repository.ChatRepository
import com.ericg.jichat.model.Chat
import com.ericg.jichat.model.SenderType
import com.ericg.jichat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repo: ChatRepository) : ViewModel() {
    var chats: Flow<List<Chat>> = emptyFlow()

    init {
        getChats()
    }

    fun getBotResponse(message: String) {
        viewModelScope.launch {
            when (val response = repo.getBotResponse(message)) {
                is Resource.Success -> {
                    val botChat = Chat(
                        sender = SenderType.BOT,
                        message = response.data!!.botResponse,
                        time = getTime()
                    )
                    saveChat(botChat)
                }
                else -> {
                    Timber.e("Error loading bot response")
                }
            }
        }
    }

    fun saveChat(chat: Chat) {
        viewModelScope.launch {
            repo.saveChat(chat)
        }
    }

    private fun getChats() {
        chats = repo.getChats()
    }

    fun deleteChats() {
        viewModelScope.launch {
            repo.deleteChats()
        }
    }
}
