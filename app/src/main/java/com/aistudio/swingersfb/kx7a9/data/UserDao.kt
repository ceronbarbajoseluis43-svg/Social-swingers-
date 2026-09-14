package com.aistudio.swingersfb.kx7a9.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE uid = :uid")
    suspend fun getUserById(uid: String): User?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Delete
    suspend fun deleteUser(user: User)
}
