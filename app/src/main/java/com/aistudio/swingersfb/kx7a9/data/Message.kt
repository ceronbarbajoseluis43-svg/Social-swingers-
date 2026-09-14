package com.aistudio.swingersfb.kx7a9.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: Long,
    val isRead: Boolean = false
)
