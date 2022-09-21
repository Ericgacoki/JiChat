package com.ericg.jichat.data.remote

import com.ericg.jichat.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("get?")
    suspend fun getBotResponse(
        @Query("bid") brainId: String = BuildConfig.BRAIN_ID,
        @Query("key") apiKey: String = BuildConfig.JICHAT_API_KEY,
        @Query("uid") uid: String = "[]",
        @Query("msg") message: String
    ): BotResponse
}
