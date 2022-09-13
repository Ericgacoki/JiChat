package com.ericg.jichat.data.local

import androidx.room.*
import com.ericg.jichat.model.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChat(chat: Chat)

    @Query("SELECT * FROM chats ORDER BY id ASC")
    fun getChats(): Flow<List<Chat>>

    @Query("DELETE FROM chats")
    suspend fun deleteChats()
}
