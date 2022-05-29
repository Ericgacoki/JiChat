package com.ericg.jichat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ericg.jichat.model.Chat

@Database(entities = [Chat::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract val chatsDao: ChatsDao
}
