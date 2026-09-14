package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entity.UserProfileEntity
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.PrimaryRose
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun EditProfileDialog(
    initialProfile: UserProfileEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, type: String, ages: String, city: String, bio: String, seeking: String, boundaries: String) -> Unit
) {
    var name by remember { mutableStateOf(initialProfile?.profileName ?: "Alex & Sofía") }
    var type by remember { mutableStateOf(initialProfile?.profileType ?: "Pareja Swinger") }
    var ages by remember { mutableStateOf(initialProfile?.ages ?: "Él 33, Ella 30") }
    var city by remember { mutableStateOf(initialProfile?.city ?: "Madrid, España") }
    var bio by remember { mutableStateOf(initialProfile?.bio ?: "") }
    var seeking by remember { mutableStateOf(initialProfile?.seeking ?: "") }
    var boundaries by remember { mutableStateOf(initialProfile?.boundaries ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkOutline),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("edit_profile_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Editar Perfil Lifestyle",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = DarkOutline)
                Spacer(modifier = Modifier.height(12.dp))

                ProfileEditField(label = "Nombres de la pareja / usuario", value = name) { name = it }
                Spacer(modifier = Modifier.height(8.dp))

                ProfileEditField(label = "Tipo de perfil (Pareja H+M, Single, etc.)", value = type) { type = it }
                Spacer(modifier = Modifier.height(8.dp))

                ProfileEditField(label = "Edades", value = ages) { ages = it }
                Spacer(modifier = Modifier.height(8.dp))

                ProfileEditField(label = "Ciudad / Ubicación", value = city) { city = it }
                Spacer(modifier = Modifier.height(8.dp))

                ProfileEditField(label = "Biografía", value = bio, maxLines = 3) { bio = it }
                Spacer(modifier = Modifier.height(8.dp))

                ProfileEditField(label = "¿Qué buscáis en el estilo de vida?", value = seeking, maxLines = 3) { seeking = it }
                Spacer(modifier = Modifier.height(8.dp))

                ProfileEditField(label = "Nuestros límites y acuerdos", value = boundaries, maxLines = 3) { boundaries = it }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onSave(name, type, ages, city, bio, seeking, boundaries)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryRose,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("save_profile_button")
                ) {
                    Text(
                        text = "Guardar Cambios",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileEditField(
    label: String,
    value: String,
    maxLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurfaceVariant,
                unfocusedContainerColor = DarkSurfaceVariant,
                focusedBorderColor = PrimaryRose,
                unfocusedBorderColor = DarkOutline,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLines
        )
    }
}
