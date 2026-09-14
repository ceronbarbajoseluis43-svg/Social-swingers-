package com.example.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.data.entity.UserProfileEntity
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.PrimaryRose
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    onLogin: suspend (String, String) -> Unit,
    onRegister: suspend (String, String, UserProfileEntity) -> Unit
) {
    var register by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Pareja") }
    var city by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().background(DarkBackground).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Social Swingers", style = MaterialTheme.typography.headlineLarge, color = PrimaryRose)
        Spacer(Modifier.height(8.dp))
        Text(if (register) "Crea tu perfil" else "Inicia sesión", color = TextPrimary)
        Spacer(Modifier.height(24.dp))
        if (register) {
            OutlinedTextField(name, { name = it }, label = { Text("Nombre del perfil") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(type, { type = it }, label = { Text("Tipo de perfil") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(city, { city = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
        }
        OutlinedTextField(email, { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(password, { password = it }, label = { Text("Contraseña (mín. 6 caracteres)") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        if (error.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = Color(0xFFFF6B6B))
        }
        Spacer(Modifier.height(18.dp))
        Button(
            enabled = !busy,
            onClick = {
                error = ""
                if (email.isBlank() || password.length < 6 || (register && name.isBlank())) {
                    error = "Completa los campos correctamente."
                    return@Button
                }
                busy = true
                scope.launch {
                    try {
                        if (register) {
                            onRegister(email, password, UserProfileEntity(profileName = name.trim(), profileType = type.trim(), ages = "", city = city.trim(), bio = "", seeking = "", boundaries = "", isVerified = false))
                        } else {
                            onLogin(email, password)
                        }
                    } catch (e: Exception) {
                        error = e.message ?: "No se pudo completar la operación."
                        busy = false
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRose),
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (register) "Crear cuenta" else "Entrar", color = Color.White) }
        TextButton(onClick = { register = !register; error = "" }) {
            Text(if (register) "Ya tengo una cuenta" else "Crear una cuenta nueva", color = TextMuted)
        }
        Text("Solo para mayores de 18 años. Respeto, consentimiento y privacidad.", color = TextMuted, style = MaterialTheme.typography.bodySmall)
    }
}
