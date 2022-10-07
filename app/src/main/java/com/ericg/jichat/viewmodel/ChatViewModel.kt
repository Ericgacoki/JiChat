package com.ericg.jichat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericg.jichat.data.repository.ChatRepository
import com.ericg.jichat.model.Chat
import com.ericg.jichat.model.SenderType
import com.ericg.jichat.ui.ChatState
import com.ericg.jichat.ui.MainActivity.Companion.getTime
import com.ericg.jichat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repo: ChatRepository) : ViewModel() {
    var uiState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
        private set

    init {
        getChats()
    }

    private fun upDateStateLoading() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(loading = true, error = "")
        }
    }

    private fun updateStateError(error: String) {
        uiState.value = uiState.value.copy(error = error, loading = false)
    }

    private fun updateStateSuccess() {
        uiState.value = uiState.value.copy(error = "", loading = false)
    }

    fun getBotResponse(message: String) {
        viewModelScope.launch {
            when (val response = repo.getBotResponse(message)) {
                is Resource.Success -> {
                    val botChat = Chat(
                        sender = SenderType.BOT,
                        message = response.data!!.botResponse ?: "Bot is unreachable a the moment!",
                        time = getTime()
                    )
                    delay(2000)
                    saveChat(botChat)
                    updateStateSuccess()
                }
                is Resource.Error -> {
                    updateStateError(response.statusMessage ?: "")
                }
                else -> {
                    upDateStateLoading()
                }
            }
        }
    }

    fun saveChat(chat: Chat) {
        if (chat.sender == SenderType.HUMAN) {
            upDateStateLoading()
        }
        viewModelScope.launch {
            repo.saveChat(chat)
        }
    }

    private fun getChats() {
        uiState.value = uiState.value.copy(chats = repo.getChats(), error = "")
    }

    fun deleteChats() {
        viewModelScope.launch {
            repo.deleteChats()
        }
    }
}
