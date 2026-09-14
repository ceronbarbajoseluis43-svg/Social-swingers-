package com.aistudio.swingersfb.kx7a9.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp DESC")
    suspend fun getConversationMessages(conversationId: String): List<Message>

    @Query("SELECT * FROM messages")
    suspend fun getAllMessages(): List<Message>

    @Delete
    suspend fun deleteMessage(message: Message)
}
