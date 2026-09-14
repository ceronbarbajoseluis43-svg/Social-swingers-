package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entity.UserProfileEntity
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
import com.example.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    isDiscreetMode: Boolean,
    onToggleDiscreetMode: () -> Unit,
    onOpenEditProfile: () -> Unit,
    onOpenEtiquette: () -> Unit,
    onUploadPhoto: (android.net.Uri) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let(onUploadPhoto)
    }
    val name = userProfile?.profileName ?: "Alex & Sofía"
    val type = userProfile?.profileType ?: "Pareja Hetero-curiosa"
    val ages = userProfile?.ages ?: "Él 33, Ella 30"
    val city = userProfile?.city ?: "Madrid, España"
    val bio = userProfile?.bio ?: "Amantes del buen vino, cenas con calma y gente auténtica. Buscamos compartir momentos distendidos con parejas educadas y respetuosas. La química y el buen rollo son lo primero."
    val seeking = userProfile?.seeking ?: "Parejas afines para salir de copas, charlar sin tabúes y lo que fluya con naturalidad."
    val boundaries = userProfile?.boundaries ?: "Respeto total, comunicación transparente, sin presiones y discreción mutua."

    Box(modifier = Modifier.fillMaxSize().background(DarkBackground)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("profile_lazy_column"),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Facebook Cover Photo + Avatar Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                ) {
                    // Cover photo
                    Image(
                        painter = painterResource(id = R.drawable.img_lifestyle_banner),
                        contentDescription = "Foto de portada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)
                    )

                    // Subtle gradient at bottom of cover
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, DarkBackground)
                                )
                            )
                    )

                    // Avatar overlapping bottom of cover
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.BottomCenter)
                            .clip(CircleShape)
                            .background(DarkBackground)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(PrimaryRose, PrimaryRoseVariant)
                                )
                            )
                            .border(2.dp, SecondaryGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!userProfile?.photoUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = userProfile?.photoUrl,
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().clip(CircleShape)
                            )
                        } else {
                            Text(
                                text = name.take(2).uppercase(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Names & Identity
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 22.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Perfil Verificado",
                            tint = BadgeVerified,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$type • $ages",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryRoseVariant
                    )

                    Text(
                        text = city,
                        fontSize = 12.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onOpenEditProfile,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryRose,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("edit_profile_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Editar Perfil", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onToggleDiscreetMode,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("profile_discreet_toggle")
                        ) {
                            Icon(
                                imageVector = if (isDiscreetMode) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = if (isDiscreetMode) PrimaryRose else TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isDiscreetMode) "Discreto ON" else "Discreto OFF",
                                fontSize = 12.sp,
                                color = if (isDiscreetMode) PrimaryRose else TextSecondary
                            )
                        }
                    }
                }
            }

            // Trust & Etiquette Banner
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SecondaryGold.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .clickable { onOpenEtiquette() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = SecondaryGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Miembro Verificado de Swingers FB",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Adherido al Código de Consentimiento y Respeto 100%",
                                fontSize = 11.sp,
                                color = SecondaryGold
                            )
                        }
                    }
                }
            }

            // Card: Biografía
            item {
                ProfileSectionCard(title = "Sobre Nosotros") {
                    Text(
                        text = bio,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 19.sp
                    )
                }
            }

            // Card: Qué buscamos
            item {
                ProfileSectionCard(title = "¿Qué Buscamos en el Estilo de Vida?") {
                    Text(
                        text = seeking,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 19.sp
                    )
                }
            }

            // Card: Límites y Acuerdos
            item {
                ProfileSectionCard(title = "Nuestros Límites y Acuerdos de Pareja") {
                    Text(
                        text = boundaries,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 19.sp
                    )
                }
            }

            // Card: Galería Privada y Bóveda
            item {
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp)
                ) { Text("Cerrar sesión") }
            }

            item {
                ProfileSectionCard(title = "Galería Lifestyle & Bóveda Privada") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Public photo thumbnail
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    Toast.makeText(context, "Foto de galería pública", Toast.LENGTH_SHORT).show()
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_lifestyle_banner),
                                contentDescription = "Foto pública",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                    .align(Alignment.BottomStart)
                            ) {
                                Text("Pública", fontSize = 9.sp, color = Color.White)
                            }
                        }

                        // Locked Private Vault thumbnail
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurfaceVariant)
                                .border(1.dp, PrimaryRose.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                .clickable {
                                    Toast.makeText(context, "Bóveda cifrada: acceso solo mediante solicitud de llave privada", Toast.LENGTH_LONG).show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Bóveda con candado",
                                    tint = PrimaryRose,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Bóveda Privada", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("4 fotos con llave", fontSize = 9.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(DarkOutline, DarkOutlineSubtle))),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = PrimaryRoseVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
