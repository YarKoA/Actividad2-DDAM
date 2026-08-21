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
import androidx.compose.runtime.Composable
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

// --- Punto 1: Funciones principales ---
// Por: Ricardo

// Función botón login
fun autenticarUsuario(usuario: String, esGoogle: Boolean): Boolean {
    return usuario.isNotBlank() && (esGoogle || usuario.length >= 4)
}

// Función crear actividad
fun crearActividad(titulo: String, hora: String, dia: String): String {
    return "Actividad '$titulo' creada para el $dia a las $hora."
}


// --- Espacio para el equipo ---
// Punto 2: Colección de datos
// Punto 3: Manejo de excepciones y null safety
// Punto 4: Clase, Objeto e Interfaz

interface GestionTarea {
    fun mostrarInformacion(): String
}

class Tarea(
    val titulo: String,
    val descripcion: String,
    val hora: String,
    val dia: String,
    var completada: Boolean = false
) : GestionTarea {

    override fun mostrarInformacion(): String {
        return "$titulo - $dia a las $hora"
    }
}

object ConfiguracionApp {
    const val NOMBRE_APP = "TAREUM"
}
// Punto 5: Función lambda / orden superior


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actividad2DDAMTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2E3B55), Color(0xFF6B93B5), Color(0xFF9ABED4))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo importado desde drawable
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo TAREUM",
                modifier = Modifier.size(130.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = ConfiguracionApp.NOMBRE_APP,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E2A38)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón 1: Ingresar como USER
            Button(
                onClick = {
                    if (autenticarUsuario("Ricardo", false)) {
                        val intent = Intent(context, MainstreamActivity::class.java)
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Ingresar como USER", color = Color(0xFF3B608C), fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón 2: Ingresar con Google
            Button(
                onClick = {
                    if (autenticarUsuario("GoogleUser", true)) {
                        val intent = Intent(context, MainstreamActivity::class.java)
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Ingresar con cuenta de Google", color = Color(0xFF3B608C), fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "*Permite conectar tu calendario con la app",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}
