package com.example.actividad2_ddam.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.actividad2_ddam.FormularioCuentaDialog
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.navigation.Routes
import com.example.actividad2_ddam.ui.components.BottomNavBar
import com.example.actividad2_ddam.ui.components.SwipeableEventCard
import com.example.actividad2_ddam.viewmodel.EventViewModel
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun EventListScreen(
    navController: NavController,
    viewModel: EventViewModel = hiltViewModel(),
    onAddEventClick: () -> Unit
) {
    val ctx = LocalContext.current
    var usuario by remember { mutableStateOf(Repo.usuarioActual) }
    var mostrarPerfil by remember { mutableStateOf(false) }
    var mostrarEditar by remember { mutableStateOf(false) }

    val diasMap = mapOf(
        DayOfWeek.MONDAY to "Lun",
        DayOfWeek.TUESDAY to "Mar",
        DayOfWeek.WEDNESDAY to "Mie",
        DayOfWeek.THURSDAY to "Jue",
        DayOfWeek.FRIDAY to "Vie",
        DayOfWeek.SATURDAY to "Sab",
        DayOfWeek.SUNDAY to "Dom"
    )
    val diaActualStr = diasMap[LocalDate.now().dayOfWeek] ?: "Lun"

    val fondo = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))

    // El ViewModel ahora guarda el estado del día y expone la lista ya procesada
    val diaSeleccionado by viewModel.diaSeleccionado.collectAsState()
    val tareasFiltradas by viewModel.tareasFiltradas.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(fondo)) {
        val isWideScreen = maxWidth > 600.dp

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 900.dp)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom").forEach { dia ->
                        val esActivo = dia == diaSeleccionado
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    viewModel.actualizarDiaSeleccionado(dia)
                                },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (esActivo) Color(0xFF556DB5)
                                else if (Repo.modoOscuro) Color(0xFF343434)
                                else Color(0xFF71808A)
                            )
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = dia,
                                    fontSize = 12.sp,
                                    color = if (esActivo || Repo.modoOscuro) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }

                if (tareasFiltradas.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Sin actividades para hoy", color = Color.White)
                    }
                } else {
                    if (isWideScreen) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(tareasFiltradas, key = { it.id }) { tarea ->
                                SwipeableEventCard(
                                    event = tarea,
                                    diaTexto = diaSeleccionado,
                                    onDelete = {
                                        viewModel.removeEvent(tarea)
                                        Toast.makeText(ctx, "Actividad eliminada", Toast.LENGTH_SHORT).show()
                                    },
                                    onEditarClick = {
                                        navController.navigate(Routes.eventEdit(tarea.id))
                                    },
                                    onAlarmaClick = {
                                        Toast.makeText(ctx, "Recordatorio activado", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(tareasFiltradas, key = { it.id }) { tarea ->
                                SwipeableEventCard(
                                    event = tarea,
                                    diaTexto = diaSeleccionado,
                                    onDelete = {
                                        viewModel.removeEvent(tarea)
                                        Toast.makeText(ctx, "Actividad eliminada", Toast.LENGTH_SHORT).show()
                                    },
                                    onEditarClick = {
                                        navController.navigate(Routes.eventEdit(tarea.id))
                                    },
                                    onAlarmaClick = {
                                        Toast.makeText(ctx, "Recordatorio activado", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(90.dp))
            }
        }

        FloatingActionButton(
            onClick = { onAddEventClick() },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 100.dp)
                .size(56.dp),
            containerColor = Color(0xFF3B5E8C),
            shape = CircleShape
        ) {
            Text(text = "+", color = Color.White, fontSize = 32.sp)
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            BottomNavBar(navController = navController, currentScreen = Routes.EVENT_LIST)
        }

        if (mostrarPerfil) {
            Dialog(onDismissRequest = { mostrarPerfil = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF252525) else Color(0xFFF1EFFE)),
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
                            color = if (Repo.modoOscuro) Color.White else Color(0xFF1E293B)
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF343434) else Color(0xFFEDE2FF))
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("Correo: ${usuario?.correo ?: ""}", fontSize = 14.sp, color = if (Repo.modoOscuro) Color.White else Color(0xFF1E293B), fontWeight = FontWeight.Medium)
                                Text("Teléfono: ${usuario?.telefono ?: ""}", fontSize = 14.sp, color = if (Repo.modoOscuro) Color.White else Color(0xFF1E293B), fontWeight = FontWeight.Medium)
                                Text("Edad: ${usuario?.edad ?: ""} años", fontSize = 14.sp, color = if (Repo.modoOscuro) Color.White else Color(0xFF1E293B), fontWeight = FontWeight.Medium)
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
                                    navController.navigate(Routes.LOGIN) {
                                        popUpTo(0) { inclusive = true }
                                    }
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
