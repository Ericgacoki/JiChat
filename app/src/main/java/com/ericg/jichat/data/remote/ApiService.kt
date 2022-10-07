package com.ericg.jichat.data.remote

import com.ericg.jichat.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("?")
    suspend fun getBotResponse(
        @Query("message") message: String,
        @Query("name") name: String = "Aco",
    ): BotResponse
}
