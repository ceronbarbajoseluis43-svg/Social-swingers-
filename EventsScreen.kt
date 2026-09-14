package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.entity.EventEntity
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

@Composable
fun EventsScreen(
    events: List<EventEntity>,
    onToggleAttendance: (EventEntity) -> Unit,
    onCreateEventClick: () -> Unit
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("Todos") }

    val categories = listOf("Todos", "Club VIP", "Fiesta Privada", "Cena", "Weekend")

    val filteredEvents = events.filter { event ->
        if (selectedCategory == "Todos") true else event.category.contains(selectedCategory, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize().background(DarkBackground)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("events_lazy_column"),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Fiestas, Clubs & Quedadas VIP",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 22.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Eventos privados verificados con aforo limitado y máxima discreción",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }

            // Categories Filter
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) PrimaryRose else DarkSurfaceElevated)
                                .border(1.dp, if (isSelected) PrimaryRoseVariant else DarkOutline, RoundedCornerShape(16.dp))
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("event_category_$cat")
                        ) {
                            Text(
                                text = cat,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Event Cards
            items(filteredEvents, key = { it.id }) { event ->
                val imageRes = when (event.imageResName) {
                    "img_event_banner" -> R.drawable.img_event_banner
                    "img_lifestyle_banner" -> R.drawable.img_lifestyle_banner
                    else -> R.drawable.img_event_banner
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(DarkOutline, DarkOutlineSubtle))),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("event_card_${event.id}")
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Event Flyer image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = event.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                        )
                                    )
                            )

                            // Category badge top-left
                            Box(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PrimaryRose)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .align(Alignment.TopStart)
                            ) {
                                Text(
                                    text = event.category,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            // Attendees count bottom-right
                            Box(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkSurface.copy(alpha = 0.85f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .align(Alignment.BottomEnd)
                            ) {
                                Text(
                                    text = "👥 ${event.attendeesCount} asistentes",
                                    fontSize = 11.sp,
                                    color = SecondaryGold,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Event details body
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 17.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = event.description,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Date & Time Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = SecondaryGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = event.dateStr, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = SecondaryGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = event.timeStr, fontSize = 12.sp, color = TextPrimary)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Location with discretion
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = PrimaryRose,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${event.city} • ${event.venue}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Dress code tag
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = SecondaryGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Dress code: ${event.dressCode}",
                                    fontSize = 11.sp,
                                    color = SecondaryGold,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // RSVP Actions
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onToggleAttendance(event) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (event.isAttending) PrimaryRose.copy(alpha = 0.2f) else PrimaryRose,
                                        contentColor = if (event.isAttending) PrimaryRoseVariant else Color.White
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp)
                                        .testTag("rsvp_button_${event.id}")
                                ) {
                                    if (event.isAttending) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("¡Asistiré!", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    } else {
                                        Text("Asistir a la Fiesta", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        Toast.makeText(context, "Guardado en tus eventos de interés", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.height(40.dp)
                                ) {
                                    Text("Me Interesa", fontSize = 12.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }

        // FAB to propose event
        FloatingActionButton(
            onClick = onCreateEventClick,
            containerColor = SecondaryGold,
            contentColor = Color.Black,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 76.dp, end = 20.dp)
                .testTag("fab_propose_event")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Proponer Quedada o Fiesta",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
