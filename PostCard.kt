package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entity.PostEntity
import com.example.ui.theme.BadgeCouple
import com.example.ui.theme.BadgeSingle
import com.example.ui.theme.BadgeVerified
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkOutlineSubtle
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.PrimaryRose
import com.example.ui.theme.PrimaryRoseVariant
import com.example.ui.theme.ReactionCheers
import com.example.ui.theme.ReactionFire
import com.example.ui.theme.ReactionLike
import com.example.ui.theme.ReactionLove
import com.example.ui.theme.SecondaryGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PostCard(
    post: PostEntity,
    isDiscreetMode: Boolean,
    onReact: (String) -> Unit,
    onOpenComments: () -> Unit,
    onDirectMessage: () -> Unit,
    onShare: () -> Unit
) {
    var showReactionPicker by remember { mutableStateOf(false) }
    var userRevealedDiscreetMedia by remember { mutableStateOf(false) }

    val formattedTime = remember(post.timestamp) {
        val diff = System.currentTimeMillis() - post.timestamp
        when {
            diff < 60 * 1000 -> "Ahora mismo"
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} min"
            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} h"
            else -> SimpleDateFormat("d MMM", Locale.getDefault()).format(Date(post.timestamp))
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(DarkOutline, DarkOutlineSubtle))),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("post_card_${post.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(post.avatarColor))
                            .border(1.5.dp, PrimaryRose.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.avatarInitials,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.authorName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Perfil Verificado",
                                tint = BadgeVerified,
                                modifier = Modifier.size(15.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${post.authorType} • ${post.authorAges}",
                                fontSize = 11.sp,
                                color = if (post.authorType.contains("Pareja")) BadgeCouple else BadgeSingle,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "•",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                            Text(
                                text = post.location,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                            Text(
                                text = "•",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                            Text(
                                text = formattedTime,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                // Lifestyle tag pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryRose.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = post.lifestyleTag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryRoseVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Text Content
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            )

            // Media or Vault Image
            val imageResId = when (post.imageResName) {
                "img_lifestyle_banner" -> R.drawable.img_lifestyle_banner
                "img_event_banner" -> R.drawable.img_event_banner
                else -> 0
            }

            if (imageResId != 0 || post.isPrivateVault) {
                Spacer(modifier = Modifier.height(12.dp))

                if (post.isPrivateVault) {
                    // Locked Private Vault Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(DarkSurfaceElevated, Color(0xFF1F1527))
                                )
                            )
                            .border(1.dp, PrimaryRose.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .clickable { onDirectMessage() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryRose.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Bóveda Privada",
                                    tint = PrimaryRose,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Bóveda de Fotos Privadas",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Envía un mensaje para solicitar la llave de acceso",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                } else if (imageResId != 0) {
                    val shouldBlur = isDiscreetMode && !userRevealedDiscreetMedia

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                if (shouldBlur) userRevealedDiscreetMedia = true
                            }
                    ) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = "Imagen de la publicación",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                                .then(if (shouldBlur) Modifier.blur(22.dp) else Modifier)
                        )

                        if (shouldBlur) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(190.dp)
                                    .background(Color.Black.copy(alpha = 0.6f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(DarkSurfaceElevated.copy(alpha = 0.9f))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = "Revelar",
                                        tint = SecondaryGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Modo Discreto • Toca para ver",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SecondaryGold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Stats counter row (Facebook style)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(PrimaryRose.copy(alpha = 0.2f))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = "Fuego",
                            tint = ReactionFire,
                            modifier = Modifier.size(14.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Amor",
                            tint = ReactionLove,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${post.likesCount} reacciones",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${post.commentsCount} comentarios",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.clickable { onOpenComments() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = DarkOutlineSubtle, thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(4.dp))

            // Reaction bar popup if active
            AnimatedVisibility(
                visible = showReactionPicker,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    color = DarkSurfaceElevated,
                    shape = RoundedCornerShape(24.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(PrimaryRose, SecondaryGold))),
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .testTag("reaction_picker_bar")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ReactionItem(icon = Icons.Default.Whatshot, label = "Fuego", color = ReactionFire) {
                            onReact("FIRE")
                            showReactionPicker = false
                        }
                        ReactionItem(icon = Icons.Default.Favorite, label = "Amor", color = ReactionLove) {
                            onReact("LOVE")
                            showReactionPicker = false
                        }
                        ReactionItem(icon = Icons.Default.LocalBar, label = "Brindis", color = ReactionCheers) {
                            onReact("CHEERS")
                            showReactionPicker = false
                        }
                        ReactionItem(icon = Icons.Default.ThumbUp, label = "Like", color = ReactionLike) {
                            onReact("LIKE")
                            showReactionPicker = false
                        }
                    }
                }
            }

            // Facebook Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like / React
                val hasReacted = post.userReaction.isNotEmpty()
                val currentIcon = when (post.userReaction) {
                    "FIRE" -> Icons.Default.Whatshot
                    "LOVE" -> Icons.Default.Favorite
                    "CHEERS" -> Icons.Default.LocalBar
                    else -> Icons.Default.ThumbUp
                }
                val currentTint = when (post.userReaction) {
                    "FIRE" -> ReactionFire
                    "LOVE" -> ReactionLove
                    "CHEERS" -> ReactionCheers
                    "LIKE" -> ReactionLike
                    else -> TextSecondary
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            if (!hasReacted) {
                                onReact("FIRE")
                            } else {
                                showReactionPicker = !showReactionPicker
                            }
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("react_button_${post.id}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = currentIcon,
                        contentDescription = "Reaccionar",
                        tint = currentTint,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (hasReacted) post.userReaction else "Reaccionar",
                        fontSize = 12.sp,
                        fontWeight = if (hasReacted) FontWeight.Bold else FontWeight.Normal,
                        color = currentTint
                    )
                }

                // Comment
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onOpenComments() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("comment_button_${post.id}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Comentar",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Comentar",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                // Direct Message
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onDirectMessage() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("direct_message_button_${post.id}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Mensaje Privado",
                        tint = PrimaryRoseVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Mensaje",
                        fontSize = 12.sp,
                        color = PrimaryRoseVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Share
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onShare() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("share_button_${post.id}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReactionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 9.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Bold
        )
    }
}
