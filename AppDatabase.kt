package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.SocialDao
import com.example.data.entity.ChatMessageEntity
import com.example.data.entity.CommentEntity
import com.example.data.entity.EventEntity
import com.example.data.entity.GroupEntity
import com.example.data.entity.PostEntity
import com.example.data.entity.UserProfileEntity

@Database(
    entities = [
        PostEntity::class,
        CommentEntity::class,
        ChatMessageEntity::class,
        EventEntity::class,
        GroupEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun socialDao(): SocialDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "swingers_fb_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun getInstance(context: Context): AppDatabase = getDatabase(context)
    }
}
