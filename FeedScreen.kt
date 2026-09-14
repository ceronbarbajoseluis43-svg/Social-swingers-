package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.PostEntity
import com.example.ui.components.PostCard
import com.example.ui.components.StoryTray
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkOutlineSubtle
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.PrimaryRose
import com.example.ui.theme.PrimaryRoseVariant
import com.example.ui.theme.SecondaryGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StoryItem

@Composable
fun FeedScreen(
    posts: List<PostEntity>,
    stories: List<StoryItem>,
    isDiscreetMode: Boolean,
    selectedTag: String,
    searchQuery: String,
    onTagSelected: (String) -> Unit,
    onStoryClick: (StoryItem) -> Unit,
    onOpenCreatePost: () -> Unit,
    onReactToPost: (PostEntity, String) -> Unit,
    onOpenComments: (PostEntity) -> Unit,
    onDirectMessage: (PostEntity) -> Unit
) {
    val context = LocalContext.current

    val tags = listOf(
        "Todos",
        "Buscamos Pareja",
        "Cena & Copas",
        "Fiesta / Evento",
        "Debate & Consejos",
        "Comunidad Lifestyle"
    )

    val filteredPosts = posts.filter { post ->
        val matchesTag = if (selectedTag == "Todos") true else post.lifestyleTag.contains(selectedTag, ignoreCase = true)
        val matchesSearch = if (searchQuery.isBlank()) true else {
            post.content.contains(searchQuery, ignoreCase = true) ||
            post.authorName.contains(searchQuery, ignoreCase = true) ||
            post.location.contains(searchQuery, ignoreCase = true)
        }
        matchesTag && matchesSearch
    }

    Box(modifier = Modifier.fillMaxSize().background(DarkBackground)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("feed_lazy_column"),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Facebook-style "What's on your mind?" Composer Box
            item {
                Surface(
                    color = DarkSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkOutline)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenCreatePost() }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryRose),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "AS",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(DarkSurfaceElevated)
                                    .border(1.dp, DarkOutline, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "¿Qué plan tenéis hoy? Publicar...",
                                    color = TextMuted,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = DarkOutlineSubtle)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Quick buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            QuickComposerAction(icon = Icons.Default.Image, label = "Foto / Flyer", tint = PrimaryRose) {
                                onOpenCreatePost()
                            }
                            QuickComposerAction(icon = Icons.Default.LocalBar, label = "Plan Pareja", tint = SecondaryGold) {
                                onOpenCreatePost()
                            }
                            QuickComposerAction(icon = Icons.Default.Lock, label = "Bóveda VIP", tint = PrimaryRoseVariant) {
                                onOpenCreatePost()
                            }
                        }
                    }
                }
            }

            // Stories Tray
            item {
                StoryTray(
                    stories = stories,
                    onStoryClick = onStoryClick,
                    onAddStoryClick = onOpenCreatePost
                )
            }

            // Category Filter Chips
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tags) { tag ->
                        val isSelected = selectedTag == tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) PrimaryRose else DarkSurfaceElevated)
                                .border(1.dp, if (isSelected) PrimaryRoseVariant else DarkOutline, RoundedCornerShape(16.dp))
                                .clickable { onTagSelected(tag) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("feed_tag_$tag")
                        ) {
                            Text(
                                text = tag,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Posts List
            if (filteredPosts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron publicaciones en esta categoría.",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(filteredPosts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        isDiscreetMode = isDiscreetMode,
                        onReact = { reaction -> onReactToPost(post, reaction) },
                        onOpenComments = { onOpenComments(post) },
                        onDirectMessage = { onDirectMessage(post) },
                        onShare = {
                            Toast.makeText(context, "Enlace privado copiado al portapapeles", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onOpenCreatePost,
            containerColor = PrimaryRose,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 76.dp, end = 20.dp)
                .testTag("fab_create_post")
        ) {
            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = "Crear publicación",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun QuickComposerAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}
