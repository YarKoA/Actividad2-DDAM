// Desarrollado por Ricardo
package com.example.actividad2_ddam

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme

class MainMenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actividad2DDAMTheme {
                MainMenuScreen()
            }
        }
    }
}

@Composable
fun MainMenuScreen() {
    val ctx = LocalContext.current

    // Estados para interactividad de días y pestañas inferiores
    var diaSeleccionado by remember { mutableStateOf("Vie") }
    var pestanaActual by remember { mutableStateOf("HOME") }

    // Función segura para redirigir a pantallas pendientes sin crasheos
    fun navegarSeguro(nombreClase: String) {
        try {
            val intent = Intent(ctx, Class.forName("com.example.actividad2_ddam.$nombreClase"))
            ctx.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(ctx, "⚠️ Pantalla no añadida todavía ($nombreClase)", Toast.LENGTH_SHORT).show()
        }
    }

    // Fondo degradado azul vertical
    val fondo = Brush.verticalGradient(
        listOf(
            Color(0xFF2C3E6B),
            Color(0xFF4B6B94),
            Color(0xFF8BB5CE)
        )
    )

    // Filtro de tareas: si es Viernes muestra las tareas existentes; otros días queda vacío
    val tareasFiltradas = if (diaSeleccionado == "Vie") Repo.tareas else emptyList()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Selector interactivo de días de la semana
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom").forEach { dia ->
                    val esActivo = dia == diaSeleccionado
                    Card(
                        modifier = Modifier
                            .size(44.dp, 38.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                diaSeleccionado = dia
                            },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (esActivo) Color(0xFF4A6DA7) else Color(0x33000000)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dia,
                                fontSize = 12.sp,
                                fontWeight = if (esActivo) FontWeight.Bold else FontWeight.Normal,
                                color = if (esActivo) Color.White else Color(0xFF1E293B)
                            )
                        }
                    }
                }
            }

            // Contenedor dinámico de tareas según el día seleccionado
            if (tareasFiltradas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Sin actividades para este día",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Toca el botón '+' para agregar una tarea para $diaSeleccionado",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(tareasFiltradas) { tarea ->
                        TareaCard(
                            t = tarea,
                            diaTexto = diaSeleccionado,
                            onEditarClick = {
                                navegarSeguro("EditarActividadActivity")
                            },
                            onAlarmaClick = {
                                Toast.makeText(ctx, "Recordatorio activo para ${tarea.hora}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }

        // Botón Flotante para Añadir Tarea
        FloatingActionButton(
            onClick = {
                try {
                    val intent = Intent(ctx, MainstreamActivity::class.java)
                    ctx.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(ctx, "⚠️ Pantalla MainstreamActivity no añadida", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 100.dp)
                .size(56.dp),
            containerColor = Color(0xFF3B5E8C),
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
        ) {
            Text(
                text = "+",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Barra inferior con cambio dinámico de imágenes
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            val imagenBarra = when (pestanaActual) {
                "CONFIG" -> R.drawable.barraconfig
                "CALENDARIO" -> R.drawable.barracalendar
                else -> R.drawable.barrahome
            }

            Image(
                painter = painterResource(id = imagenBarra),
                contentDescription = "Barra de Navegación",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            // Áreas táctiles sin recuadros negros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {
                // Botón Configuración
                Box(
                    modifier = Modifier
                        .size(55.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            pestanaActual = "CONFIG"
                            navegarSeguro("ConfigActivity")
                        }
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Botón Calendario
                Box(
                    modifier = Modifier
                        .size(55.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            pestanaActual = "CALENDARIO"
                            navegarSeguro("CalendarActivity")
                        }
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Botón Home
                Box(
                    modifier = Modifier
                        .size(65.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            pestanaActual = "HOME"
                        }
                )
            }
        }
    }
}

@Composable
fun TareaCard(t: Tarea, diaTexto: String, onEditarClick: () -> Unit, onAlarmaClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFFE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = t.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Text(
                    text = "A las: ${t.hora}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3F5A8A)
                )
            }

            Text(
                text = t.desc ?: "Sin descripción adicional",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "En: 60 min.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3F5A8A)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "$diaTexto · 28 · Ago",
                        fontSize = 10.sp,
                        color = Color(0xFF3F5A8A)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.relojazul),
                        contentDescription = "Alarma",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onAlarmaClick() }
                    )

                    Image(
                        painter = painterResource(id = R.drawable.editarazul),
                        contentDescription = "Editar",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onEditarClick() }
                    )
                }
            }
        }
    }
}