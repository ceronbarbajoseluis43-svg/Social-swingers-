package com.aistudio.swingersfb.kx7a9.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: String?,
    val age: Int?,
    val bio: String?,
    val createdAt: Long
)
