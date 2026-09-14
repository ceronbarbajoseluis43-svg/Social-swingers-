package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.entity.ChatMessageEntity
import com.example.data.entity.CommentEntity
import com.example.data.entity.EventEntity
import com.example.data.entity.GroupEntity
import com.example.data.entity.PostEntity
import com.example.data.entity.UserProfileEntity
import com.example.data.firebase.FirebaseRepository
import com.example.data.repository.SocialRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavigationTab {
    FEED,
    EVENTS,
    GROUPS,
    MESSAGES,
    PROFILE
}

data class StoryItem(
    val id: String,
    val authorName: String,
    val authorType: String,
    val avatarInitials: String,
    val avatarColor: Long,
    val caption: String,
    val location: String,
    val imageResName: String = "",
    val timeAgo: String = "2h"
)

@OptIn(ExperimentalCoroutinesApi::class)
class SocialViewModel(private val repository: SocialRepository, private val firebase: FirebaseRepository) : ViewModel() {

    init {
        viewModelScope.launch { repository.checkAndSeedInitialData() }
        viewModelScope.launch {
            firebase.loadCurrentProfile()?.let { repository.updateProfile(it) }
        }
    }

    val realProfiles: StateFlow<List<FirebaseRepository.RemoteProfile>> = firebase.profilesExceptCurrent()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun conversationIdFor(uid: String): String = firebase.conversationId(uid)

    fun uploadProfilePhoto(uri: android.net.Uri) {
        viewModelScope.launch {
            val url = firebase.uploadProfilePhoto(uri)
            val current = userProfile.value ?: return@launch
            repository.updateProfile(current.copy(photoUrl = url))
        }
    }

    fun logout() = firebase.logout()

    val posts: StateFlow<List<PostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val events: StateFlow<List<EventEntity>> = repository.allEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val groups: StateFlow<List<GroupEntity>> = repository.allGroups
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allMessages: StateFlow<List<ChatMessageEntity>> = repository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentTab = MutableStateFlow(NavigationTab.FEED)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _isDiscreetMode = MutableStateFlow(false)
    val isDiscreetMode: StateFlow<Boolean> = _isDiscreetMode.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFeedTag = MutableStateFlow("Todos")
    val selectedFeedTag: StateFlow<String> = _selectedFeedTag.asStateFlow()

    // Active Comments Sheet
    private val _selectedPostForComments = MutableStateFlow<PostEntity?>(null)
    val selectedPostForComments: StateFlow<PostEntity?> = _selectedPostForComments.asStateFlow()

    val currentPostComments: StateFlow<List<CommentEntity>> = _selectedPostForComments
        .flatMapLatest { post ->
            if (post == null) flowOf(emptyList())
            else repository.getCommentsForPost(post.id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Chat
    private val _activeConversationId = MutableStateFlow<String?>(null)
    val activeConversationId: StateFlow<String?> = _activeConversationId.asStateFlow()

    private val _activeConversationName = MutableStateFlow("")
    val activeConversationName: StateFlow<String> = _activeConversationName.asStateFlow()

    val activeConversationMessages: StateFlow<List<ChatMessageEntity>> = _activeConversationId
        .flatMapLatest { convId ->
            if (convId == null) flowOf(emptyList())
            else {
                val otherUid = convId.substringAfter("firebase_")
                if (otherUid == convId) repository.getMessagesForConversation(convId)
                else firebase.messages(otherUid)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Story Viewer
    private val _activeStory = MutableStateFlow<StoryItem?>(null)
    val activeStory: StateFlow<StoryItem?> = _activeStory.asStateFlow()

    // Modals
    private val _isCreatePostOpen = MutableStateFlow(false)
    val isCreatePostOpen: StateFlow<Boolean> = _isCreatePostOpen.asStateFlow()

    private val _isCreateEventOpen = MutableStateFlow(false)
    val isCreateEventOpen: StateFlow<Boolean> = _isCreateEventOpen.asStateFlow()

    private val _isEtiquetteOpen = MutableStateFlow(false)
    val isEtiquetteOpen: StateFlow<Boolean> = _isEtiquetteOpen.asStateFlow()

    // Hardcoded curated stories
    val stories = listOf(
        StoryItem(
            id = "story_1",
            authorName = "Carlos & Valeria",
            authorType = "Pareja Swinger",
            avatarInitials = "CV",
            avatarColor = 0xFFE11D62,
            caption = "Brindis previo al fin de semana 🥂 ¡Buena vibra para toda la comunidad!",
            location = "Madrid Rooftop",
            imageResName = "img_lifestyle_banner",
            timeAgo = "1h"
        ),
        StoryItem(
            id = "story_2",
            authorName = "Club Velvet",
            authorType = "Club VIP",
            avatarInitials = "VV",
            avatarColor = 0xFF8B5CF6,
            caption = "Preparando los antifaces para este sábado. Luces tenues y privacidad absoluta 🎭",
            location = "Barcelona",
            imageResName = "img_event_banner",
            timeAgo = "3h"
        ),
        StoryItem(
            id = "story_3",
            authorName = "Elena Moon",
            authorType = "Single Chica",
            avatarInitials = "EM",
            avatarColor = 0xFFEC4899,
            caption = "Tarde soleada en la playa. La libertad y el respeto son el mejor estilo de vida ✨",
            location = "Marbella",
            imageResName = "img_lifestyle_banner",
            timeAgo = "5h"
        ),
        StoryItem(
            id = "story_4",
            authorName = "David & Marta",
            authorType = "Pareja",
            avatarInitials = "DM",
            avatarColor = 0xFF10B981,
            caption = "Cata de vinos en pareja. ¡Nos encanta descubrir nuevos lugares con encanto!",
            location = "Sevilla",
            imageResName = "img_event_banner",
            timeAgo = "7h"
        )
    )

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun toggleDiscreetMode() {
        val next = !_isDiscreetMode.value
        _isDiscreetMode.value = next
        viewModelScope.launch {
            repository.updateDiscreetMode(next)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedFeedTag(tag: String) {
        _selectedFeedTag.value = tag
    }

    fun openCommentsForPost(post: PostEntity) {
        _selectedPostForComments.value = post
    }

    fun closeComments() {
        _selectedPostForComments.value = null
    }

    fun openChat(conversationId: String, contactName: String) {
        _activeConversationId.value = conversationId
        _activeConversationName.value = contactName
    }

    fun closeChat() {
        _activeConversationId.value = null
        _activeConversationName.value = ""
    }

    fun openStory(story: StoryItem) {
        _activeStory.value = story
    }

    fun closeStory() {
        _activeStory.value = null
    }

    fun setCreatePostOpen(open: Boolean) {
        _isCreatePostOpen.value = open
    }

    fun setCreateEventOpen(open: Boolean) {
        _isCreateEventOpen.value = open
    }

    fun setEtiquetteOpen(open: Boolean) {
        _isEtiquetteOpen.value = open
    }

    fun onReactToPost(post: PostEntity, reactionType: String) {
        val currentReaction = post.userReaction
        val newReaction = if (currentReaction == reactionType) "" else reactionType
        viewModelScope.launch {
            repository.reactToPost(post.id, newReaction, currentReaction)
        }
    }

    fun submitComment(text: String) {
        val post = _selectedPostForComments.value ?: return
        if (text.isBlank()) return
        val currentProfile = userProfile.value
        val authorName = currentProfile?.profileName ?: "Alex & Sofía"
        val authorType = currentProfile?.profileType ?: "Pareja"
        viewModelScope.launch {
            repository.addComment(
                postId = post.id,
                authorName = authorName,
                authorType = authorType,
                avatarInitials = "AS",
                avatarColor = 0xFFE11D62,
                text = text.trim()
            )
        }
    }

    fun sendChatMessage(text: String) {
        val convId = _activeConversationId.value ?: return
        if (text.isBlank()) return
        val currentProfile = userProfile.value
        val myName = currentProfile?.profileName ?: "Alex & Sofía"
        viewModelScope.launch {
            if (convId.startsWith("firebase_")) {
                firebase.sendMessage(convId.removePrefix("firebase_"), myName, text.trim())
            } else {
                repository.sendMessage(conversationId = convId, senderName = myName, text = text.trim(), isMine = true)
            }
        }
    }

    fun publishNewPost(
        content: String,
        lifestyleTag: String,
        imageResName: String,
        isPrivateVault: Boolean
    ) {
        if (content.isBlank()) return
        val currentProfile = userProfile.value
        val name = currentProfile?.profileName ?: "Alex & Sofía"
        val type = currentProfile?.profileType ?: "Pareja Swinger"
        val ages = currentProfile?.ages ?: "33 & 30"
        val city = currentProfile?.city ?: "Madrid, España"
        viewModelScope.launch {
            repository.createPost(
                authorName = name,
                authorType = type,
                authorAges = ages,
                location = city,
                avatarColor = 0xFFE11D62,
                avatarInitials = "AS",
                content = content.trim(),
                lifestyleTag = lifestyleTag,
                imageResName = imageResName,
                isPrivateVault = isPrivateVault
            )
            _isCreatePostOpen.value = false
        }
    }

    fun toggleEventAttendance(event: EventEntity) {
        viewModelScope.launch {
            repository.toggleEventAttendance(event.id, event.isAttending)
        }
    }

    fun toggleGroupMembership(group: GroupEntity) {
        viewModelScope.launch {
            repository.toggleGroupMembership(group.id, group.isJoined)
        }
    }

    fun updateProfile(
        name: String,
        type: String,
        ages: String,
        city: String,
        bio: String,
        seeking: String,
        boundaries: String
    ) {
        viewModelScope.launch {
            val updated = UserProfileEntity(
                id = 1,
                profileName = name,
                profileType = type,
                ages = ages,
                city = city,
                bio = bio,
                seeking = seeking,
                boundaries = boundaries,
                isVerified = true,
                isDiscreetMode = _isDiscreetMode.value
            )
            repository.updateProfile(updated)
            firebase.currentUid()?.let { firebase.saveProfile(it, updated, updated.photoUrl) }
        }
    }
}

class SocialViewModelFactory(private val context: android.content.Context) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = com.example.data.database.AppDatabase.getInstance(context.applicationContext)
        val repo = com.example.data.repository.SocialRepository(db.socialDao())
        val firebase = com.example.data.firebase.FirebaseRepository()
        @Suppress("UNCHECKED_CAST")
        return SocialViewModel(repo, firebase) as T
    }
}
