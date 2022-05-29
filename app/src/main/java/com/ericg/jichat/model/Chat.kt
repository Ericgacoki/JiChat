package com.ericg.jichat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sender: SenderType,
    val message: String,
    val time: String
)
