package com.example.actividad2_ddam

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Actividad2DDAMTheme { LoginScreen() } }
    }
}

@Composable
fun LoginScreen() {
    val ctx = LocalContext.current
    val fondo = Brush.verticalGradient(listOf(Color(0xFF2E3B55), Color(0xFF6B93B5), Color(0xFF9ABED4)))
    
    // Variables para guardar lo que el usuario escribe
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize().background(fondo), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Image(painterResource(R.drawable.logo), "Logo", Modifier.size(130.dp))
            Text("TAREUM", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2A38))
            
            Spacer(Modifier.height(32.dp))

            // Campo para el usuario
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White.copy(0.2f),
                    focusedContainerColor = Color.White.copy(0.3f)
                )
            )

            Spacer(Modifier.height(16.dp))

            // Campo para la contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White.copy(0.2f),
                    focusedContainerColor = Color.White.copy(0.3f)
                )
            )

            Spacer(Modifier.height(32.dp))
            
            BotonRedondo("Ingresar como USER") { 
                if (usuario.isNotEmpty()) {
                    ctx.startActivity(Intent(ctx, MainMenuActivity::class.java)) 
                }
            }
            Spacer(Modifier.height(16.dp))
            BotonRedondo("Ingresar con cuenta de Google") { 
                ctx.startActivity(Intent(ctx, MainMenuActivity::class.java)) 
            }
            
            Spacer(Modifier.height(12.dp))
            Text("*Permite conectar tu calendario", fontSize = 11.sp, color = Color.White)
        }
    }
}

// Componente reutilizable para los botones blancos
@Composable
fun BotonRedondo(texto: String, alClick: () -> Unit) {
    Button(
        onClick = alClick,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
    ) {
        Text(texto, color = Color(0xFF3B608C), fontWeight = FontWeight.Bold)
    }
}
