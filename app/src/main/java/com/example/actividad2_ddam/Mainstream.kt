package com.example.actividad2_ddam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme

class MainstreamActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actividad2DDAMTheme {
                MainstreamScreen(onBack = { finish() })
            }
        }
    }
}

@Composable
fun MainstreamScreen(onBack: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var mensajeConfirmacion by remember { mutableStateOf("") }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2E3B55), Color(0xFF6B93B5), Color(0xFF9ABED4))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFDCE7F0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Añadir actividad",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C567A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Ingresa un título *", fontSize = 13.sp, color = Color(0xFF333333))
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    placeholder = { Text("Ingresar", fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFB4C8D8),
                        unfocusedContainerColor = Color(0xFFB4C8D8),
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Ingresa una descripción", fontSize = 13.sp, color = Color(0xFF333333))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = { Text("Ingresar", fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(top = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFB4C8D8),
                        unfocusedContainerColor = Color(0xFFB4C8D8),
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Define una hora:", fontSize = 13.sp, color = Color(0xFF333333))
                    SuggestionChip(
                        onClick = { },
                        label = { Text("9:30 am", color = Color.White) },
                        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFF4A7C9F)),
                        border = null
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Crear Actividad (usa la clase Tarea del Punto 4)
                Button(
                    onClick = {
                
                        val tarea = Tarea(
                            titulo = if (titulo.isBlank()) "Sin título" else titulo,
                            descripcion = descripcion,
                            hora = "9:30 am",
                            dia = "Vie"
                        )
                
                        mensajeConfirmacion = tarea.mostrarInformacion()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5A93BE)
                    )
                ) {
                    Text(
                        "Crear Actividad",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (mensajeConfirmacion.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = mensajeConfirmacion,
                        fontSize = 12.sp,
                        color = Color(0xFF1E415F),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
