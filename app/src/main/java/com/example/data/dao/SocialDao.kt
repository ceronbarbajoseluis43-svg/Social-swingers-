package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.ChatMessageEntity
import com.example.data.entity.CommentEntity
import com.example.data.entity.EventEntity
import com.example.data.entity.GroupEntity
import com.example.data.entity.PostEntity
import com.example.data.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SocialDao {

    // --- Posts ---
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("UPDATE posts SET userReaction = :reaction, likesCount = likesCount + :likesDelta WHERE id = :postId")
    suspend fun updatePostReaction(postId: Int, reaction: String, likesDelta: Int)

    @Query("UPDATE posts SET commentsCount = commentsCount + 1 WHERE id = :postId")
    suspend fun incrementCommentsCount(postId: Int)

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun getPostCount(): Int

    // --- Comments ---
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPost(postId: Int): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    // --- Chat Messages ---
    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessages(messages: List<ChatMessageEntity>)

    // --- Events ---
    @Query("SELECT * FROM events ORDER BY id ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("UPDATE events SET isAttending = :isAttending, attendeesCount = attendeesCount + :delta WHERE id = :eventId")
    suspend fun updateEventAttendance(eventId: Int, isAttending: Boolean, delta: Int)

    // --- Groups ---
    @Query("SELECT * FROM groups ORDER BY id ASC")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<GroupEntity>)

    @Query("UPDATE groups SET isJoined = :isJoined, membersCount = membersCount + :delta WHERE id = :groupId")
    suspend fun updateGroupMembership(groupId: Int, isJoined: Boolean, delta: Int)

    // --- Profile ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET isDiscreetMode = :isDiscreet WHERE id = 1")
    suspend fun updateDiscreetMode(isDiscreet: Boolean)
}
