package com.ericg.jichat.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericg.jichat.data.repository.ChatRepository
import com.ericg.jichat.model.Chat
import com.ericg.jichat.model.SenderType
import com.ericg.jichat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repo: ChatRepository) : ViewModel() {
    val chats: SnapshotStateList<Chat> = mutableStateListOf<Chat>()

    fun getBotResponse(message: String) {
        viewModelScope.launch {
            when (val response = repo.getBotResponse(message)) {
                is Resource.Success -> {
                    chats.add(
                        Chat(
                            sender = SenderType.BOT,
                            message = response.data!!.botResponse,
                            time = getTime()
                        )
                    )
                }
                else -> {
                    Timber.e("Error loading bot response")
                }
            }
        }
    }
}

fun getTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    return current.format(formatter)
}
