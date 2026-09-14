package com.example.data.firebase

import android.net.Uri
import com.example.data.entity.ChatMessageEntity
import com.example.data.entity.UserProfileEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    data class RemoteProfile(
        val uid: String,
        val name: String,
        val type: String,
        val ages: String,
        val city: String,
        val bio: String,
        val seeking: String,
        val boundaries: String,
        val photoUrl: String = ""
    )

    fun currentUid(): String? = auth.currentUser?.uid
    fun currentEmail(): String? = auth.currentUser?.email
    fun logout() = auth.signOut()

    suspend fun register(email: String, password: String, profile: UserProfileEntity) {
        val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
        val uid = result.user?.uid ?: error("No se pudo crear el usuario")
        saveProfile(uid, profile)
    }

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email.trim(), password).await()
    }

    fun addAuthStateListener(onChanged: (Boolean) -> Unit): FirebaseAuth.AuthStateListener {
        val listener = FirebaseAuth.AuthStateListener { onChanged(it.currentUser != null) }
        auth.addAuthStateListener(listener)
        return listener
    }

    fun removeAuthStateListener(listener: FirebaseAuth.AuthStateListener) = auth.removeAuthStateListener(listener)

    suspend fun saveProfile(uid: String, profile: UserProfileEntity, photoUrl: String? = null) {
        val data = hashMapOf<String, Any>(
            "uid" to uid,
            "name" to profile.profileName,
            "type" to profile.profileType,
            "ages" to profile.ages,
            "city" to profile.city,
            "bio" to profile.bio,
            "seeking" to profile.seeking,
            "boundaries" to profile.boundaries,
            "verified" to profile.isVerified,
            "updatedAt" to System.currentTimeMillis()
        )
        if (!photoUrl.isNullOrBlank()) data["photoUrl"] = photoUrl
        firestore.collection("profiles").document(uid).set(data).await()
    }

    suspend fun loadCurrentProfile(): UserProfileEntity? {
        val uid = currentUid() ?: return null
        val doc = firestore.collection("profiles").document(uid).get().await()
        if (!doc.exists()) return null
        return UserProfileEntity(
            id = 1,
            profileName = doc.getString("name") ?: "",
            profileType = doc.getString("type") ?: "",
            ages = doc.getString("ages") ?: "",
            city = doc.getString("city") ?: "",
            bio = doc.getString("bio") ?: "",
            seeking = doc.getString("seeking") ?: "",
            boundaries = doc.getString("boundaries") ?: "",
            isVerified = doc.getBoolean("verified") ?: false,
            photoUrl = doc.getString("photoUrl") ?: ""
        )
    }

    fun profilesExceptCurrent(): Flow<List<RemoteProfile>> = callbackFlow {
        val current = currentUid()
        var registration: ListenerRegistration? = null
        registration = firestore.collection("profiles").addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val users = snapshot?.documents.orEmpty().mapNotNull { d ->
                if (d.id == current) return@mapNotNull null
                RemoteProfile(
                    uid = d.id,
                    name = d.getString("name") ?: "Usuario",
                    type = d.getString("type") ?: "Miembro",
                    ages = d.getString("ages") ?: "",
                    city = d.getString("city") ?: "",
                    bio = d.getString("bio") ?: "",
                    seeking = d.getString("seeking") ?: "",
                    boundaries = d.getString("boundaries") ?: "",
                    photoUrl = d.getString("photoUrl") ?: ""
                )
            }
            trySend(users)
        }
        awaitClose { registration?.remove() }
    }

    suspend fun uploadProfilePhoto(uri: Uri): String {
        val uid = currentUid() ?: error("Debes iniciar sesión")
        val ref = storage.reference.child("profilePhotos/$uid/avatar.jpg")
        ref.putFile(uri).await()
        val url = ref.downloadUrl.await().toString()
        firestore.collection("profiles").document(uid).update("photoUrl", url).await()
        return url
    }

    fun conversationId(otherUid: String): String {
        val me = currentUid() ?: error("Debes iniciar sesión")
        return listOf(me, otherUid).sorted().joinToString("_")
    }

    fun messages(otherUid: String): Flow<List<ChatMessageEntity>> = callbackFlow {
        val me = currentUid() ?: run { trySend(emptyList()); close(); return@callbackFlow }
        val conversation = conversationId(otherUid)
        val registration = firestore.collection("conversations").document(conversation)
            .collection("messages").orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val result = snapshot?.documents.orEmpty().map { d ->
                    ChatMessageEntity(
                        conversationId = conversation,
                        senderName = d.getString("senderName") ?: "Usuario",
                        text = d.getString("text") ?: "",
                        timestamp = d.getLong("timestamp") ?: 0L,
                        isMine = d.getString("senderId") == me
                    )
                }
                trySend(result)
            }
        awaitClose { registration.remove() }
    }

    suspend fun sendMessage(otherUid: String, senderName: String, text: String) {
        val me = currentUid() ?: error("Debes iniciar sesión")
        val conversation = conversationId(otherUid)
        firestore.collection("conversations").document(conversation)
            .collection("messages").add(
                mapOf(
                    "senderId" to me,
                    "senderName" to senderName,
                    "text" to text,
                    "timestamp" to System.currentTimeMillis()
                )
            ).await()
    }
}
