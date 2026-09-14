package com.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.firebase.FirebaseRepository
import com.example.ui.auth.AuthScreen
import com.example.ui.components.BottomNavBar
import com.example.ui.components.CommentsSheet
import com.example.ui.components.CreatePostDialog
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.EtiquetteDialog
import com.example.ui.components.StoryViewerDialog
import com.example.ui.components.TopSocialBar
import com.example.ui.dialogs.AgeGateDialog
import com.example.ui.screens.EventsScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.GroupsScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.NavigationTab
import com.example.ui.viewmodel.SocialViewModel
import com.example.ui.viewmodel.SocialViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val firebase = remember { FirebaseRepository() }
                var authenticated by remember { mutableStateOf(firebase.currentUid() != null) }
                DisposableEffect(firebase) {
                    val listener = firebase.addAuthStateListener { authenticated = it }
                    onDispose { firebase.removeAuthStateListener(listener) }
                }
                if (!authenticated) {
                    AuthScreen(
                        onLogin = { email, password -> firebase.login(email, password) },
                        onRegister = { email, password, profile -> firebase.register(email, password, profile) },
                        onGuestLogin = { firebase.loginAsGuest() }
                    )
                } else {
                    val context = LocalContext.current
                    val factory = remember(context) { SocialViewModelFactory(context) }
                    val viewModel: SocialViewModel = viewModel(factory = factory)
                    SwingersSocialApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SwingersSocialApp(
    viewModel: SocialViewModel
) {
    val context = LocalContext.current
    var isAgeConfirmed by rememberSaveable { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val currentTab by viewModel.currentTab.collectAsState()
    val isDiscreetMode by viewModel.isDiscreetMode.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFeedTag by viewModel.selectedFeedTag.collectAsState()

    val posts by viewModel.posts.collectAsState()
    val events by viewModel.events.collectAsState()
    val groups by viewModel.groups.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val realProfiles by viewModel.realProfiles.collectAsState()

    val selectedPostForComments by viewModel.selectedPostForComments.collectAsState()
    val currentPostComments by viewModel.currentPostComments.collectAsState()

    val activeConversationId by viewModel.activeConversationId.collectAsState()
    val activeConversationName by viewModel.activeConversationName.collectAsState()
    val activeConversationMessages by viewModel.activeConversationMessages.collectAsState()

    val activeStory by viewModel.activeStory.collectAsState()
    val isCreatePostOpen by viewModel.isCreatePostOpen.collectAsState()
    val isEtiquetteOpen by viewModel.isEtiquetteOpen.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        containerColor = DarkBackground,
        topBar = {
            TopSocialBar(
                isDiscreetMode = isDiscreetMode,
                onToggleDiscreetMode = { viewModel.toggleDiscreetMode() },
                onOpenChatTab = { viewModel.selectTab(NavigationTab.MESSAGES) },
                onOpenEtiquette = { viewModel.setEtiquetteOpen(true) },
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.setSearchQuery(it) }
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                NavigationTab.FEED -> {
                    FeedScreen(
                        posts = posts,
                        stories = viewModel.stories,
                        isDiscreetMode = isDiscreetMode,
                        selectedTag = selectedFeedTag,
                        searchQuery = searchQuery,
                        onTagSelected = { viewModel.setSelectedFeedTag(it) },
                        onStoryClick = { viewModel.openStory(it) },
                        onOpenCreatePost = { viewModel.setCreatePostOpen(true) },
                        onReactToPost = { post, reaction -> viewModel.onReactToPost(post, reaction) },
                        onOpenComments = { post -> viewModel.openCommentsForPost(post) },
                        onDirectMessage = { post ->
                            viewModel.openChat(
                                conversationId = "chat_${post.authorName.lowercase().replace(" ", "_").take(15)}",
                                contactName = post.authorName
                            )
                            viewModel.selectTab(NavigationTab.MESSAGES)
                        }
                    )
                }

                NavigationTab.EVENTS -> {
                    EventsScreen(
                        events = events,
                        onToggleAttendance = { event -> viewModel.toggleEventAttendance(event) },
                        onCreateEventClick = { viewModel.setCreatePostOpen(true) }
                    )
                }

                NavigationTab.GROUPS -> {
                    GroupsScreen(
                        groups = groups,
                        onToggleMembership = { group -> viewModel.toggleGroupMembership(group) },
                        onOpenEtiquette = { viewModel.setEtiquetteOpen(true) }
                    )
                }

                NavigationTab.MESSAGES -> {
                    MessagesScreen(
                        activeConversationId = activeConversationId,
                        activeConversationName = activeConversationName,
                        activeMessages = activeConversationMessages,
                        realProfiles = realProfiles,
                        onSelectConversation = { convId, name ->
                            viewModel.openChat(convId, name)
                        },
                        onCloseActiveChat = { viewModel.closeChat() },
                        onSendMessage = { messageText ->
                            viewModel.sendChatMessage(messageText)
                        }
                    )
                }

                NavigationTab.PROFILE -> {
                    ProfileScreen(
                        userProfile = userProfile,
                        isDiscreetMode = isDiscreetMode,
                        onToggleDiscreetMode = { viewModel.toggleDiscreetMode() },
                        onOpenEditProfile = { showEditProfileDialog = true },
                        onOpenEtiquette = { viewModel.setEtiquetteOpen(true) },
                        onUploadPhoto = { viewModel.uploadProfilePhoto(it) },
                        onLogout = { viewModel.logout() }
                    )
                }
            }
        }
    }

    // Strict 18+ Age Gate Dialog for Consent & Play Policy Compliance
    if (!isAgeConfirmed) {
        AgeGateDialog(
            onAccept = { isAgeConfirmed = true },
            onDecline = { (context as? Activity)?.finish() }
        )
    }

    // Modal Sheet: Comments
    selectedPostForComments?.let { post ->
        CommentsSheet(
            post = post,
            comments = currentPostComments,
            onDismiss = { viewModel.closeComments() },
            onSubmitComment = { text -> viewModel.submitComment(text) }
        )
    }

    // Modal Dialog: Fullscreen Story Viewer
    activeStory?.let { story ->
        StoryViewerDialog(
            story = story,
            onDismiss = { viewModel.closeStory() },
            onSendReply = { reply ->
                viewModel.sendChatMessage(reply)
                viewModel.closeStory()
            }
        )
    }

    // Modal Dialog: Create Post Composer
    if (isCreatePostOpen) {
        CreatePostDialog(
            authorName = userProfile?.profileName ?: "Alex & Sofía",
            authorType = userProfile?.profileType ?: "Pareja Swinger",
            onDismiss = { viewModel.setCreatePostOpen(false) },
            onPublish = { content, tag, imageResName, isPrivateVault ->
                viewModel.publishNewPost(
                    content = content,
                    lifestyleTag = tag,
                    imageResName = imageResName,
                    isPrivateVault = isPrivateVault
                )
            }
        )
    }

    // Modal Dialog: Edit Profile
    if (showEditProfileDialog) {
        EditProfileDialog(
            initialProfile = userProfile,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, type, ages, city, bio, seeking, boundaries ->
                viewModel.updateProfile(
                    name = name,
                    type = type,
                    ages = ages,
                    city = city,
                    bio = bio,
                    seeking = seeking,
                    boundaries = boundaries
                )
                showEditProfileDialog = false
            }
        )
    }

    // Modal Dialog: Community Etiquette & Guidelines
    if (isEtiquetteOpen) {
        EtiquetteDialog(
            onDismiss = { viewModel.setEtiquetteOpen(false) }
        )
    }
}

// Kept for screenshot test and preview compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}
