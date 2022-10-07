package com.ericg.jichat.data.remote

import com.google.gson.annotations.SerializedName

class BotResponse(
    @SerializedName("message")
    val botResponse: String?
)
