package com.example.actividad2_ddam

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Actividad2DDAMTheme { SettingsScreen { finish() } } }
    }
}

@Composable
fun SettingsScreen(alCerrar: () -> Unit) {
    val ctx = LocalContext.current
    var modoOscuro by remember { mutableStateOf(false) }

    // Función auxiliar para no repetir el código del Toast
    fun mostrarProximamente(mensaje: String = "Funcionalidad próxima") {
        Toast.makeText(ctx, mensaje, Toast.LENGTH_SHORT).show()
    }

    val fondoGeneral = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))

    Box(Modifier.fillMaxSize().background(fondoGeneral)) {

        // Contenedor principal blanco/morado claro
        Card(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f).align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFFE))
        ) {
            Column(Modifier.padding(24.dp).fillMaxSize()) {

                // --- SECCIÓN 1: Tarjeta de Perfil y Botón de Google ---
                // Usamos un Box para poder superponer la foto de perfil sobre la tarjeta
                Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 24.dp)) {

                    // Tarjeta azul del usuario
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 25.dp).height(120.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(
                                Brush.verticalGradient(listOf(Color(0xFF7A9BBF), Color(0xFF5A75A7)))
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp).fillMaxSize(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("Guest", fontSize = 20.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(16.dp))

                                // Botón de conectar con Google
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    modifier = Modifier.clickable {
                                        mostrarProximamente("No es posible ingresar con Google. Funcionalidad próxima")
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Text("Conectar con Google", fontSize = 14.sp, color = Color(0xFF3F5A8A))
                                        Text(text = "G", color = Color(0xFF4285F4), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Círculo de foto de perfil superpuesto
                    Card(
                        modifier = Modifier
                            .size(90.dp)
                            .align(Alignment.TopEnd)
                            .padding(end = 24.dp), // Lo empujamos hacia adentro desde la derecha
                        shape = CircleShape,
                        border = BorderStroke(4.dp, Color(0xFFF1EFFE)), // Borde del mismo color que el fondo principal
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(
                                Brush.verticalGradient(listOf(Color(0xFF8BB5CE), Color(0xFF5A75A7)))
                            )
                        )
                    }
                }

                // --- SECCIÓN 2: Colores de Interfaz ---
                Text("Colores de Interfaz", fontSize = 16.sp, modifier = Modifier.padding(bottom = 12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val colores = listOf(Color(0xFF3B4874), Color(0xFF4A7D9D), Color(0xFF9AB4C9))
                    colores.forEach { color ->
                        Card(
                            modifier = Modifier
                                .size(80.dp)
                                .clickable { mostrarProximamente() },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = color),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {}
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- SECCIÓN 3: Modo Oscuro ---
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Modo oscuro", fontSize = 16.sp, modifier = Modifier.padding(end = 16.dp))
                    Switch(
                        checked = modoOscuro,
                        onCheckedChange = {
                            mostrarProximamente()
                            // No cambiamos la variable 'modoOscuro' para que el switch no se mueva visualmente,
                            // o puedes permitir que cambie si lo prefieres.
                        },
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = Color(0xFF5A75A7),
                            uncheckedTrackColor = Color.White,
                            uncheckedBorderColor = Color.LightGray
                        )
                    )
                    Text(" no", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        // Botón inferior para salir y regresar al menú
        IconButton(
            onClick = { alCerrar() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(50.dp)
                .background(Color(0xFF4A6DA7), CircleShape)
        ) {
            Icon(painterResource(id = android.R.drawable.ic_menu_revert), null, tint = Color.White)
        }
    }
}