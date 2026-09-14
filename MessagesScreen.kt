package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.ChatMessageEntity
import com.example.data.firebase.FirebaseRepository
import com.example.ui.theme.BadgeVerified
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkOutlineSubtle
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.PrimaryRose
import com.example.ui.theme.PrimaryRoseVariant
import com.example.ui.theme.SecondaryGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ConversationSummary(
    val id: String,
    val name: String,
    val type: String,
    val avatarInitials: String,
    val avatarColor: Long,
    val unreadCount: Int = 0
)

@Composable
fun MessagesScreen(
    activeConversationId: String?,
    activeConversationName: String,
    activeMessages: List<ChatMessageEntity>,
    realProfiles: List<FirebaseRepository.RemoteProfile>,
    onSelectConversation: (String, String) -> Unit,
    onCloseActiveChat: () -> Unit,
    onSendMessage: (String) -> Unit
) {
    val predefinedConversations = listOf(
        ConversationSummary(
            id = "chat_carlos_valeria",
            name = "Carlos & Valeria",
            type = "Pareja Swinger • Madrid",
            avatarInitials = "CV",
            avatarColor = 0xFFE11D62,
            unreadCount = 1
        ),
        ConversationSummary(
            id = "chat_club_velvet",
            name = "Club Velvet VIP",
            type = "Club Oficial • Barcelona",
            avatarInitials = "VV",
            avatarColor = 0xFF8B5CF6,
            unreadCount = 1
        ),
        ConversationSummary(
            id = "chat_elena_moon",
            name = "Elena Moon",
            type = "Single Chica • Marbella",
            avatarInitials = "EM",
            avatarColor = 0xFFEC4899,
            unreadCount = 0
        ),
        ConversationSummary(
            id = "chat_david_marta",
            name = "David & Marta",
            type = "Pareja • Sevilla",
            avatarInitials = "DM",
            avatarColor = 0xFF10B981,
            unreadCount = 0
        )
    )

    if (activeConversationId != null) {
        ActiveChatView(
            conversationName = activeConversationName,
            messages = activeMessages,
            onBack = onCloseActiveChat,
            onSendMessage = onSendMessage
        )
    } else {
        Box(modifier = Modifier.fillMaxSize().background(DarkBackground)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("messages_lazy_column"),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Mensajería Privada & Discreta",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 22.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = SecondaryGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Conversaciones protegidas bajo estricta discreción",
                                fontSize = 12.sp,
                                color = SecondaryGold
                            )
                        }
                    }
                    HorizontalDivider(color = DarkOutlineSubtle)
                }

                if (realProfiles.isNotEmpty()) {
                    item {
                        Text(
                            "Personas y parejas de la comunidad",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                        )
                    }
                    items(realProfiles, key = { it.uid }) { profile ->
                        Surface(
                            color = DarkSurface,
                            modifier = Modifier.fillMaxWidth().clickable {
                                onSelectConversation("firebase_${profile.uid}", profile.name)
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(50.dp).clip(CircleShape).background(PrimaryRose),
                                    contentAlignment = Alignment.Center
                                ) { Text(profile.name.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold) }
                                Spacer(Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(profile.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                    Text("${profile.type} • ${profile.city}", fontSize = 12.sp, color = PrimaryRoseVariant)
                                    Text("Abrir chat privado en tiempo real", fontSize = 12.sp, color = TextMuted)
                                }
                            }
                        }
                    }
                    item { HorizontalDivider(color = DarkOutlineSubtle, modifier = Modifier.padding(top = 8.dp)) }
                }

                items(predefinedConversations) { conv ->
                    Surface(
                        color = DarkSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectConversation(conv.id, conv.name) }
                            .testTag("conversation_item_${conv.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color(conv.avatarColor)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = conv.avatarInitials,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = conv.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = BadgeVerified,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = conv.type,
                                    fontSize = 12.sp,
                                    color = PrimaryRoseVariant
                                )
                                Text(
                                    text = "Toca para abrir la conversación privada...",
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }

                            if (conv.unreadCount > 0) {
                                Badge(
                                    containerColor = PrimaryRose,
                                    contentColor = Color.White
                                ) {
                                    Text("${conv.unreadCount}", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                    HorizontalDivider(color = DarkOutlineSubtle, modifier = Modifier.padding(start = 78.dp))
                }
            }
        }
    }
}

@Composable
private fun ActiveChatView(
    conversationName: String,
    messages: List<ChatMessageEntity>,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit
) {
    var messageText by remember { mutableStateOf("") }

    val icebreakers = listOf(
        "🍷 ¿Os apetece tomar unas copas primero?",
        "✨ ¡Nos gustó vuestro perfil! ¿Qué plan tenéis?",
        "📍 ¿Por qué zona de la ciudad soléis moveros?",
        "🎭 ¿Iréis a la fiesta de este fin de semana?"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .testTag("active_chat_view")
    ) {
        Surface(
            color = DarkSurface,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PrimaryRose),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversationName.take(2).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = conversationName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = BadgeVerified,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Text(
                        text = "🔒 Chat Privado Cifrado Lifestyle",
                        fontSize = 11.sp,
                        color = SecondaryGold
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages) { msg ->
                val isMine = msg.isMine
                val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(msg.timestamp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
                ) {
                    Column(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isMine) 16.dp else 2.dp,
                                    bottomEnd = if (isMine) 2.dp else 16.dp
                                )
                            )
                            .background(if (isMine) PrimaryRose else DarkSurfaceElevated)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = msg.text,
                            fontSize = 14.sp,
                            color = Color.White,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = timeStr,
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(icebreakers) { ice ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkSurfaceElevated)
                        .clickable { onSendMessage(ice) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = ice,
                        fontSize = 11.sp,
                        color = SecondaryGold,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Surface(
            color = DarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Escribe un mensaje privado...", color = TextMuted, fontSize = 13.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant,
                        focusedBorderColor = PrimaryRose,
                        unfocusedBorderColor = DarkOutline,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            onSendMessage(messageText)
                            messageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PrimaryRose)
                        .testTag("send_chat_message_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
