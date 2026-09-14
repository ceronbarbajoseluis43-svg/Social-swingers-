package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val dateStr: String,
    val timeStr: String,
    val city: String,
    val venue: String,
    val dressCode: String,
    val description: String,
    val attendeesCount: Int = 0,
    val isAttending: Boolean = false,
    val imageResName: String = ""
)
