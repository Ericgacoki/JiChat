package com.ericg.jichat.model

data class Chat(
    val sender: SenderType,
    val message: String,
    val time: String
)
