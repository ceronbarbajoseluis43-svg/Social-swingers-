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
    private val authProvider: () -> FirebaseAuth? = {
        try { FirebaseAuth.getInstance() } catch (_: Throwable) { null }
    },
    private val firestoreProvider: () -> FirebaseFirestore? = {
        try { FirebaseFirestore.getInstance() } catch (_: Throwable) { null }
    },
    private val storageProvider: () -> FirebaseStorage? = {
        try { FirebaseStorage.getInstance() } catch (_: Throwable) { null }
    }
) {
    private val auth: FirebaseAuth? by lazy { authProvider() }
    private val firestore: FirebaseFirestore? by lazy { firestoreProvider() }
    private val storage: FirebaseStorage? by lazy { storageProvider() }

    private var localGuestUid: String? = null
    private var localGuestEmail: String? = null
    private val localListeners = mutableListOf<(Boolean) -> Unit>()

    val isFirebaseAvailable: Boolean
        get() = auth != null && firestore != null

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

    fun currentUid(): String? = auth?.currentUser?.uid ?: localGuestUid
    fun currentEmail(): String? = auth?.currentUser?.email ?: localGuestEmail
    fun logout() {
        auth?.signOut()
        localGuestUid = null
        localGuestEmail = null
        notifyAuthState(false)
    }

    private fun notifyAuthState(isAuth: Boolean) {
        localListeners.forEach { it(isAuth) }
    }

    fun loginAsGuest() {
        localGuestUid = "local_demo_user"
        localGuestEmail = "demo@swingersfb.com"
        notifyAuthState(true)
    }

    suspend fun register(email: String, password: String, profile: UserProfileEntity) {
        val fbAuth = auth
        if (fbAuth != null) {
            val result = fbAuth.createUserWithEmailAndPassword(email.trim(), password).await()
            val uid = result.user?.uid ?: error("No se pudo crear el usuario")
            saveProfile(uid, profile)
        } else {
            localGuestUid = "local_user_${System.currentTimeMillis()}"
            localGuestEmail = email.trim()
            notifyAuthState(true)
        }
    }

    suspend fun login(email: String, password: String) {
        val fbAuth = auth
        if (fbAuth != null) {
            fbAuth.signInWithEmailAndPassword(email.trim(), password).await()
        } else {
            localGuestUid = "local_user_${System.currentTimeMillis()}"
            localGuestEmail = email.trim()
            notifyAuthState(true)
        }
    }

    fun addAuthStateListener(onChanged: (Boolean) -> Unit): Any {
        val fbAuth = auth
        if (fbAuth != null) {
            val listener = FirebaseAuth.AuthStateListener { onChanged(it.currentUser != null || localGuestUid != null) }
            fbAuth.addAuthStateListener(listener)
            return listener
        } else {
            localListeners.add(onChanged)
            onChanged(localGuestUid != null)
            return onChanged
        }
    }

    fun removeAuthStateListener(listener: Any) {
        val fbAuth = auth
        if (fbAuth != null && listener is FirebaseAuth.AuthStateListener) {
            fbAuth.removeAuthStateListener(listener)
        } else if (listener is Function1<*, *>) {
            @Suppress("UNCHECKED_CAST")
            localListeners.remove(listener as (Boolean) -> Unit)
        }
    }

    suspend fun saveProfile(uid: String, profile: UserProfileEntity, photoUrl: String? = null) {
        val fs = firestore ?: return
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
        try {
            fs.collection("profiles").document(uid).set(data).await()
        } catch (_: Throwable) {
            // Handled gracefully
        }
    }

    suspend fun loadCurrentProfile(): UserProfileEntity? {
        val fs = firestore ?: return null
        val uid = currentUid() ?: return null
        return try {
            val doc = fs.collection("profiles").document(uid).get().await()
            if (!doc.exists()) return null
            UserProfileEntity(
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
        } catch (_: Throwable) {
            null
        }
    }

    fun profilesExceptCurrent(): Flow<List<RemoteProfile>> = callbackFlow {
        val fs = firestore
        if (fs == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val current = currentUid()
        var registration: ListenerRegistration? = null
        try {
            registration = fs.collection("profiles").addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
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
        } catch (_: Throwable) {
            trySend(emptyList())
        }
        awaitClose { registration?.remove() }
    }

    suspend fun uploadProfilePhoto(uri: Uri): String {
        val st = storage
        val fs = firestore
        val uid = currentUid() ?: "local_user"
        if (st == null || fs == null) {
            return uri.toString()
        }
        return try {
            val ref = st.reference.child("profilePhotos/$uid/avatar.jpg")
            ref.putFile(uri).await()
            val url = ref.downloadUrl.await().toString()
            fs.collection("profiles").document(uid).update("photoUrl", url).await()
            url
        } catch (_: Throwable) {
            uri.toString()
        }
    }

    fun conversationId(otherUid: String): String {
        val me = currentUid() ?: "guest_user"
        return listOf(me, otherUid).sorted().joinToString("_")
    }

    fun messages(otherUid: String): Flow<List<ChatMessageEntity>> = callbackFlow {
        val fs = firestore
        val me = currentUid()
        if (fs == null || me == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val conversation = conversationId(otherUid)
        var registration: ListenerRegistration? = null
        try {
            registration = fs.collection("conversations").document(conversation)
                .collection("messages").orderBy("timestamp")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
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
        } catch (_: Throwable) {
            trySend(emptyList())
        }
        awaitClose { registration?.remove() }
    }

    suspend fun sendMessage(otherUid: String, senderName: String, text: String) {
        val fs = firestore ?: return
        val me = currentUid() ?: return
        val conversation = conversationId(otherUid)
        try {
            fs.collection("conversations").document(conversation)
                .collection("messages").add(
                    mapOf(
                        "senderId" to me,
                        "senderName" to senderName,
                        "text" to text,
                        "timestamp" to System.currentTimeMillis()
                    )
                ).await()
        } catch (_: Throwable) {
            // Ignored if offline
        }
    }
}
