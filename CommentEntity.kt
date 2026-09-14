package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: Int,
    val authorName: String,
    val authorType: String,
    val avatarInitials: String,
    val avatarColor: Long,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
