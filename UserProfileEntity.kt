package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val profileName: String,
    val profileType: String,
    val ages: String,
    val city: String,
    val bio: String,
    val seeking: String,
    val boundaries: String,
    val isVerified: Boolean = true,
    val isDiscreetMode: Boolean = false,
    val photoUrl: String = ""
)
