package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val authorName: String,
    val authorType: String,
    val authorAges: String,
    val location: String,
    val avatarColor: Long,
    val avatarInitials: String,
    val content: String,
    val lifestyleTag: String,
    val imageResName: String = "",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val userReaction: String = "",
    val isPrivateVault: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
