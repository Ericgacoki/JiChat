package com.ericg.jichat.data.remote

import com.ericg.jichat.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    // http://api.brainshop.ai/get?bid=156451&key=RdjOJWhMeR2JDdOW&uid=%5Buid%5D&msg=hey
    @GET("get?")
    suspend fun getBotResponse(
        @Query("bid") brainId: String = BuildConfig.BRAIN_ID,
        @Query("key") apiKey: String = BuildConfig.JICHAT_API_KEY,
        @Query("uid") uid: String = "[]",
        @Query("msg") message: String
    ): BotResponse
}
