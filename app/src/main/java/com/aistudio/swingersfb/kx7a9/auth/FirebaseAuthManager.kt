package com.aistudio.swingersfb.kx7a9.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager {
    private val auth = FirebaseAuth.getInstance()

    suspend fun registerUser(email: String, password: String, displayName: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                user.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()
                ).await()
            }
            Result.success(user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logoutUser() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser

    fun isUserLoggedIn(): Boolean = auth.currentUser != null
}
