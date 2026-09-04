package com.example.actividad2_ddam

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

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.window.Dialog

class MainMenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Actividad2DDAMTheme { MainMenuScreen() } }
    }
}

@Composable
fun MainMenuScreen() {
    val ctx = LocalContext.current
    var usuario by remember { mutableStateOf(Repo.usuarioActual) }
    var mostrarPerfil by remember { mutableStateOf(false) }
    var mostrarEditar by remember { mutableStateOf(false) }

    var diaSeleccionado by remember { mutableStateOf("Vie") }
    var pestanaActual by remember { mutableStateOf("HOME") }

    fun navegarSeguro(nombreClase: String) {
        try {
            val intent = Intent(ctx, Class.forName("com.example.actividad2_ddam.$nombreClase"))
            ctx.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(ctx, "Error al abrir pantalla", Toast.LENGTH_SHORT).show()
        }
    }

    val fondo = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))

    val tareasFiltradas = if (diaSeleccionado == "Vie") Repo.tareas else emptyList()

    Box(modifier = Modifier.fillMaxSize().background(fondo)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(44.dp))

            val primeraLetra = usuario?.nombre?.trim()?.firstOrNull()?.uppercase() ?: "U"

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bienvenido, ${usuario?.nombre ?: "Usuario"}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { mostrarPerfil = true },
                    shape = CircleShape,
                    color = Color(0xFF385A79),
                    border = BorderStroke(2.dp, Color.White),
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = primeraLetra,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom").forEach { dia ->
                    val esActivo = dia == diaSeleccionado
                    Card(
                        modifier = Modifier
                            .size(44.dp, 38.dp)
                            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                                diaSeleccionado = dia
                            },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = if (esActivo) Color(0xFF4A6DA7) else Color(0x33000000))
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = dia, fontSize = 12.sp, color = if (esActivo) Color.White else Color(0xFF1E293B))
                        }
                    }
                }
            }

            if (tareasFiltradas.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "Sin actividades para hoy", color = Color.White)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f).padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(tareasFiltradas) { tarea ->
                        TareaCard(
                            t = tarea,
                            diaTexto = diaSeleccionado,
                            onEditarClick = {
                                val intent = Intent(ctx, EditarActividadActivity::class.java)
                                intent.putExtra("TAREA_ID", tarea.id)
                                ctx.startActivity(intent)
                            },
                            onAlarmaClick = { Toast.makeText(ctx, "Recordatorio activado", Toast.LENGTH_SHORT).show() }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(90.dp))
        }

        FloatingActionButton(
            onClick = { navegarSeguro("MainstreamActivity") },
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 24.dp, bottom = 100.dp).size(56.dp),
            containerColor = Color(0xFF3B5E8C),
            shape = CircleShape
        ) {
            Text(text = "+", color = Color.White, fontSize = 32.sp)
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
            val imagenBarra = if (pestanaActual == "CALENDARIO") R.drawable.barracalendar else R.drawable.barrahome
            Image(
                painter = painterResource(id = imagenBarra),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Row(modifier = Modifier.fillMaxWidth().height(75.dp).padding(horizontal = 24.dp), horizontalArrangement = Arrangement.End) {
                Box(modifier = Modifier.size(55.dp).clickable {
                    navegarSeguro("SettingsActivity")
                })
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.size(55.dp).clickable {
                    pestanaActual = "CALENDARIO"
                    navegarSeguro("CalendarActivity")
                })
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.size(65.dp).clickable { pestanaActual = "HOME" })
            }
        }

        if (mostrarPerfil) {
            Dialog(onDismissRequest = { mostrarPerfil = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFFE)),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94))
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Perfil de Usuario",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        val inicial = usuario?.nombre?.trim()?.firstOrNull()?.uppercase() ?: "U"
                        Surface(
                            modifier = Modifier.size(72.dp),
                            shape = CircleShape,
                            color = Color(0xFF2C3E6B),
                            border = BorderStroke(3.dp, Color.White),
                            shadowElevation = 6.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = inicial,
                                    color = Color.White,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = usuario?.nombre ?: "",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("Correo: ${usuario?.correo ?: ""}", fontSize = 14.sp, color = Color(0xFF1E293B), fontWeight = FontWeight.Medium)
                                Text("Teléfono: ${usuario?.telefono ?: ""}", fontSize = 14.sp, color = Color(0xFF1E293B), fontWeight = FontWeight.Medium)
                                Text("Edad: ${usuario?.edad ?: ""} años", fontSize = 14.sp, color = Color(0xFF1E293B), fontWeight = FontWeight.Medium)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Button(
                            onClick = {
                                mostrarPerfil = false
                                mostrarEditar = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E6B))
                        ) {
                            Text("Editar cuenta", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = {
                                    mostrarPerfil = false
                                    val intent = Intent(ctx, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    ctx.startActivity(intent)
                                }
                            ) {
                                Text("Cerrar sesión", color = Color(0xFFC62828), fontWeight = FontWeight.SemiBold)
                            }

                            TextButton(onClick = { mostrarPerfil = false }) {
                                Text("Volver al menú", color = Color(0xFF2C3E6B), fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        if (mostrarEditar) {
            FormularioCuentaDialog(
                titulo = "Editar Cuenta",
                usuarioInicial = usuario,
                onDismiss = { mostrarEditar = false },
                onGuardar = { usuarioEditado ->
                    Repo.usuarioActual = usuarioEditado
                    usuario = usuarioEditado
                    Toast.makeText(ctx, "Cuenta actualizada", Toast.LENGTH_SHORT).show()
                    mostrarEditar = false
                }
            )
        }
    }
}

@Composable
fun TareaCard(t: Tarea, diaTexto: String, onEditarClick: () -> Unit, onAlarmaClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFFE))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = t.titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = t.hora, fontSize = 12.sp, color = Color(0xFF3F5A8A))
            }
            Text(text = t.desc ?: "Sin detalles", fontSize = 12.sp, color = Color.Gray)
            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text(text = "Duracion aprox: 60 min", fontSize = 12.sp, color = Color(0xFF3F5A8A))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "$diaTexto · Ago", fontSize = 10.sp)
                    Image(painterResource(R.drawable.relojazul), null, Modifier.size(24.dp).clickable { onAlarmaClick() })
                    Image(painterResource(R.drawable.editarazul), null, Modifier.size(24.dp).clickable { onEditarClick() })
                }
            }
        }
    }
}




















/*
package com.example.actividad2_ddam

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

// Actividad principal para visualizar y gestionar la lista de tareas
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

// Componente principal de la pantalla de tareas
@Composable
fun MainMenuScreen() {
    val ctx = LocalContext.current

    // Estados para controlar el dia filtrado y la navegacion inferior
    var diaSeleccionado by remember { mutableStateOf("Vie") }
    var pestanaActual by remember { mutableStateOf("HOME") }

    // Gestion de navegacion segura entre actividades
    fun navegarSeguro(nombreClase: String) {
        try {
            val intent = Intent(ctx, Class.forName("com.example.actividad2_ddam.$nombreClase"))
            ctx.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(ctx, "Error al abrir pantalla", Toast.LENGTH_SHORT).show()
        }
    }

    val fondo = Brush.verticalGradient(
        listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE))
    )

    // Logica de filtrado basico para demostracion
    val tareasFiltradas = if (diaSeleccionado == "Vie") Repo.tareas else emptyList()

    Box(modifier = Modifier.fillMaxSize().background(fondo)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(48.dp))

            // Selector interactivo de dias de la semana
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom").forEach { dia ->
                    val esActivo = dia == diaSeleccionado
                    Card(
                        modifier = Modifier
                            .size(44.dp, 38.dp)
                            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                                diaSeleccionado = dia
                            },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (esActivo) Color(0xFF4A6DA7) else Color(0x33000000)
                        )
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = dia, fontSize = 12.sp, color = if (esActivo) Color.White else Color(0xFF1E293B))
                        }
                    }
                }
            }

            // Visualizacion de la lista de actividades o mensaje vacio
            if (tareasFiltradas.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "Sin actividades para hoy", color = Color.White)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f).padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(tareasFiltradas) { tarea ->
                        TareaCard(
                            t = tarea,
                            diaTexto = diaSeleccionado,
                            onEditarClick = { navegarSeguro("EditarActividadActivity") },
                            onAlarmaClick = { Toast.makeText(ctx, "Recordatorio activado", Toast.LENGTH_SHORT).show() }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(90.dp))
        }

        // Boton para añadir nueva tarea
        FloatingActionButton(
            onClick = { navegarSeguro("MainstreamActivity") },
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 24.dp, bottom = 100.dp).size(56.dp),
            containerColor = Color(0xFF3B5E8C),
            shape = CircleShape
        ) {
            Text(text = "+", color = Color.White, fontSize = 32.sp)
        }

        // Barra de navegacion inferior con iconos dinmicos
        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
            val imagenBarra = if (pestanaActual == "CALENDARIO") R.drawable.barracalendar else R.drawable.barrahome

            Image(
                painter = painterResource(id = imagenBarra),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Row(modifier = Modifier.fillMaxWidth().height(75.dp).padding(horizontal = 24.dp), horizontalArrangement = Arrangement.End) {
                Box(modifier = Modifier.size(55.dp).clickable { /* Ajustes */ })
                Spacer(modifier = Modifier.width(10.dp))
                // Acceso a la vista de calendario
                Box(modifier = Modifier.size(55.dp).clickable { 
                    pestanaActual = "CALENDARIO"
                    navegarSeguro("CalendarActivity")
                })
                Spacer(modifier = Modifier.width(10.dp))
                // Regreso a la vista principal
                Box(modifier = Modifier.size(65.dp).clickable { pestanaActual = "HOME" })
            }
        }
    }
}

// Tarjeta individual para mostrar detalles de una tarea
@Composable
fun TareaCard(t: Tarea, diaTexto: String, onEditarClick: () -> Unit, onAlarmaClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFFE))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = t.titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = t.hora, fontSize = 12.sp, color = Color(0xFF3F5A8A))
            }
            Text(text = t.desc ?: "Sin detalles", fontSize = 12.sp, color = Color.Gray)
            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text(text = "Duracion aprox: 60 min", fontSize = 12.sp, color = Color(0xFF3F5A8A))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "$diaTexto · Ago", fontSize = 10.sp)
                    // Iconos para acciones rapidas
                    Image(painterResource(R.drawable.relojazul), null, Modifier.size(24.dp).clickable { onAlarmaClick() })
                    Image(painterResource(R.drawable.editarazul), null, Modifier.size(24.dp).clickable { onEditarClick() })
                }
            }
        }
    }
}

 */
