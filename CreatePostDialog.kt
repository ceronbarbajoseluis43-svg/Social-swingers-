package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.PrimaryRose
import com.example.ui.theme.PrimaryRoseVariant
import com.example.ui.theme.SecondaryGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatePostDialog(
    authorName: String,
    authorType: String,
    onDismiss: () -> Unit,
    onPublish: (content: String, tag: String, imageResName: String, isPrivateVault: Boolean) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("Buscamos Pareja") }
    var selectedMedia by remember { mutableStateOf("none") } // "none", "img_lifestyle_banner", "img_event_banner", "vault"

    val lifestyleTags = listOf(
        "Buscamos Pareja",
        "Cena & Copas",
        "Fiesta / Evento",
        "Debate & Consejos",
        "Experiencia & Relato"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkOutline),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("create_post_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Crear Publicación",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Author Info Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(DarkSurfaceElevated)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(PrimaryRose),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = authorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "($authorType)",
                        fontSize = 11.sp,
                        color = SecondaryGold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Text field
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = {
                        Text(
                            "¿Qué tenéis en mente? Comparte vuestro plan, preferencias o abre un debate con la comunidad...",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant,
                        focusedBorderColor = PrimaryRose,
                        unfocusedBorderColor = DarkOutline,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(14.dp),
                    minLines = 4,
                    maxLines = 6,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_content_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Tags selection
                Text(
                    text = "Etiqueta Lifestyle:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    lifestyleTags.forEach { tag ->
                        val isSelected = selectedTag == tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) PrimaryRose else DarkSurfaceElevated)
                                .border(1.dp, if (isSelected) PrimaryRoseVariant else DarkOutline, RoundedCornerShape(16.dp))
                                .clickable { selectedTag = tag }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Attachments
                Text(
                    text = "Adjuntar Foto o Bóveda:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MediaOptionChip(
                        icon = Icons.Default.Image,
                        label = "Terraza Cocktail",
                        selected = selectedMedia == "img_lifestyle_banner"
                    ) {
                        selectedMedia = if (selectedMedia == "img_lifestyle_banner") "none" else "img_lifestyle_banner"
                    }

                    MediaOptionChip(
                        icon = Icons.Default.Image,
                        label = "Club VIP",
                        selected = selectedMedia == "img_event_banner"
                    ) {
                        selectedMedia = if (selectedMedia == "img_event_banner") "none" else "img_event_banner"
                    }

                    MediaOptionChip(
                        icon = Icons.Default.Lock,
                        label = "Bóveda 🔒",
                        selected = selectedMedia == "vault"
                    ) {
                        selectedMedia = if (selectedMedia == "vault") "none" else "vault"
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Publish Button
                Button(
                    onClick = {
                        val imgRes = when (selectedMedia) {
                            "img_lifestyle_banner" -> "img_lifestyle_banner"
                            "img_event_banner" -> "img_event_banner"
                            else -> ""
                        }
                        val isVault = selectedMedia == "vault"
                        onPublish(content, selectedTag, imgRes, isVault)
                    },
                    enabled = content.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryRose,
                        contentColor = Color.White,
                        disabledContainerColor = DarkSurfaceElevated,
                        disabledContentColor = TextMuted
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("publish_post_submit_button")
                ) {
                    Text(
                        text = "Publicar en Swingers FB",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaOptionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) PrimaryRose.copy(alpha = 0.2f) else DarkSurfaceElevated)
            .border(1.dp, if (selected) PrimaryRose else DarkOutline, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) PrimaryRose else TextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) PrimaryRose else TextSecondary
            )
        }
    }
}
