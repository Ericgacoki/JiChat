package com.ericg.jichat.ui

import com.ericg.jichat.model.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ChatState(
    val chats: Flow<List<Chat>> = emptyFlow(),
    val error: String = "",
    val loading: Boolean = false
)
