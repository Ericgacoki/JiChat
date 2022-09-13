package com.ericg.jichat.data.repository

import com.ericg.jichat.data.local.ChatDatabase
import com.ericg.jichat.data.remote.ApiService
import com.ericg.jichat.data.remote.BotResponse
import com.ericg.jichat.model.Chat
import com.ericg.jichat.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val apiService: ApiService,
    private val chatsDatabase: ChatDatabase
) {
    suspend fun getBotResponse(message: String): Resource<BotResponse> {
        return try {
            val response = apiService.getBotResponse(message = message)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(message = "Oh, uh!\nIt looks like you're offline \uD83D\uDCA9")
        }
    }

    suspend fun saveChat(chat: Chat) {
        chatsDatabase.chatsDao.saveChat(chat)
    }

    fun getChats(): Flow<List<Chat>> {
        return chatsDatabase.chatsDao.getChats()
    }

    suspend fun deleteChats() {
        chatsDatabase.chatsDao.deleteChats()
    }
}
