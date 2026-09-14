package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val description: String,
    val membersCount: Int = 0,
    val isJoined: Boolean = false,
    val badgeEmoji: String = "✨"
)
