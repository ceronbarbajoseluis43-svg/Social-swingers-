package com.aistudio.swingersfb.kx7a9.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.aistudio.swingersfb.kx7a9.data.User
import kotlinx.coroutines.tasks.await

class FirestoreManager {
    private val db = FirebaseFirestore.getInstance()

    suspend fun saveUserProfile(user: User): Result<Unit> {
        return try {
            db.collection("profiles").document(user.uid)
                .set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(uid: String): Result<User?> {
        return try {
            val document = db.collection("profiles").document(uid).get().await()
            val user = document.toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllProfiles(): Result<List<User>> {
        return try {
            val snapshot = db.collection("profiles").get().await()
            val users = snapshot.toObjects(User::class.java)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveMessage(conversationId: String, messageData: Map<String, Any>): Result<Unit> {
        return try {
            db.collection("conversations").document(conversationId)
                .collection("messages").add(messageData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
