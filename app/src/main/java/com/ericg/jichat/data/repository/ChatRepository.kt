package com.ericg.jichat.data.repository

import com.ericg.jichat.data.remote.APIService
import com.ericg.jichat.data.remote.BotResponse
import com.ericg.jichat.util.Resource
import javax.inject.Inject

class ChatRepository @Inject constructor(private val apiService: APIService) {
    suspend fun getBotResponse(message: String): Resource<BotResponse>{
        val response = try {
            apiService.getBotResponse(message = message)
        } catch (e: Exception){
            return Resource.Error(message = "Failed to load bot response")
        }
        return Resource.Success(response)
    }
}
