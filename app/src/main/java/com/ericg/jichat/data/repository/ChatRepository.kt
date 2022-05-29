package com.ericg.jichat.data.repository

import com.ericg.jichat.data.local.ChatDatabase
import com.ericg.jichat.data.remote.ApiService
import com.ericg.jichat.data.remote.BotResponse
import com.ericg.jichat.model.Chat
import com.ericg.jichat.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepository @Inject constructor(private val apiService: ApiService, private val chatsDatabase: ChatDatabase) {
    suspend fun getBotResponse(message: String): Resource<BotResponse>{
        val response = try {
            apiService.getBotResponse(message = message)
        } catch (e: Exception){
            return Resource.Error(message = "Failed to load bot response")
        }
        return Resource.Success(response)
    }

    suspend fun saveChat(chat: Chat){
        chatsDatabase.chatsDao.saveChat(chat)
    }

    fun getChats(): Flow<List<Chat>> {
        return chatsDatabase.chatsDao.getChats()
    }

    suspend fun deleteChats(){
        chatsDatabase.chatsDao.deleteChats()
    }
}
