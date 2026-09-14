package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.PrimaryRose
import com.example.ui.theme.PrimaryRoseVariant
import com.example.ui.theme.SecondaryGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AgeGateDialog(
  onAccept: () -> Unit,
  onDecline: () -> Unit
) {
  Dialog(
    onDismissRequest = { /* Must explicitly accept */ },
    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("age_gate_dialog"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = DarkSurface),
      border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryRose.copy(alpha = 0.6f))
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Icon Shield / Masquerade
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(
              Brush.linearGradient(
                colors = listOf(PrimaryRose, PrimaryRoseVariant)
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Social Swingers",
          color = TextPrimary,
          fontSize = 22.sp,
          fontWeight = FontWeight.Black,
          textAlign = TextAlign.Center
        )

        Text(
          text = "Comunidad Exclusiva +18",
          color = SecondaryGold,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceVariant)
            .border(1.dp, DarkOutline, RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Column {
            RuleBullet(text = "1. Estrictamente para personas de 18 años o más.")
            Spacer(modifier = Modifier.height(6.dp))
            RuleBullet(text = "2. Consentimiento y respeto mutuo son obligatorios.")
            Spacer(modifier = Modifier.height(6.dp))
            RuleBullet(text = "3. Discreción y privacidad garantizadas.")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "¿Confirmas que eres mayor de 18 años y estás de acuerdo con las normas de convivencia de la comunidad?",
          color = TextSecondary,
          fontSize = 12.sp,
          textAlign = TextAlign.Center,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onAccept,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("accept_age_gate_button"),
          colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryRose,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.GppGood, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Soy mayor de 18 años — Entrar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
          onClick = onDecline,
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("decline_age_gate_button"),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkOutline),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("No cumplo con la edad (Salir)", fontSize = 13.sp)
        }
      }
    }
  }
}

@Composable
private fun RuleBullet(text: String) {
  Row(verticalAlignment = Alignment.Top) {
    Icon(
      imageVector = Icons.Default.GppGood,
      contentDescription = null,
      tint = SecondaryGold,
      modifier = Modifier
        .size(16.dp)
        .padding(top = 2.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = text,
      color = TextPrimary,
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium
    )
  }
}
