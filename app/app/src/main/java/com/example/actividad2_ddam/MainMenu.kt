package com.example.actividad2_ddam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
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
import java.time.LocalDate
import java.time.DayOfWeek

class MainMenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Actividad2DDAMTheme(darkTheme = Repo.modoOscuro, dynamicColor = false) { MainMenuScreen() } }
    }
}

@Composable
fun MainMenuScreen() {
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
    var diaSeleccionado by remember { mutableStateOf(diaActualStr) }

    fun navegarSeguro(nombreClase: String) {
        try {
            val intent = Intent(ctx, Class.forName("com.example.actividad2_ddam.$nombreClase"))
            ctx.startActivity(intent)
            if (ctx is android.app.Activity) {
                ctx.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        } catch (e: Exception) {
            Toast.makeText(ctx, "Error al abrir pantalla", Toast.LENGTH_SHORT).show()
        }
    }

    val fondo = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))

    val tareasFiltradas = Repo.tareas.filter { it.dia.equals(diaSeleccionado, ignoreCase = true) }

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
                            .weight(1f).height(38.dp)
                            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                                diaSeleccionado = dia
                            },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = if (esActivo) Color(0xFF556DB5) else if (Repo.modoOscuro) Color(0xFF343434) else Color(0xFF71808A))
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = dia, fontSize = 12.sp, color = if (esActivo || Repo.modoOscuro) Color.White else Color.Black)
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
                    items(tareasFiltradas, key = { it.id }) { tarea ->
                        // Animación al agregar eventos
                        val isAdded by remember { mutableStateOf(true) }
                        val offset by animateDpAsState(
                            targetValue = if (isAdded) 0.dp else 100.dp,
                            animationSpec = tween(durationMillis = 400),
                            label = "addOffset"
                        )

                        Box(modifier = Modifier.offset(x = offset)) {
                            SwipeableEventCard(
                                event = tarea,
                                diaTexto = diaSeleccionado,
                                onDelete = {
                                    Repo.tareas.remove(tarea)
                                    Toast.makeText(ctx, "Actividad eliminada", Toast.LENGTH_SHORT).show()
                                },
                                onEditarClick = {
                                    val intent = Intent(ctx, EditarActividadActivity::class.java)
                                    intent.putExtra("TAREA_ID", tarea.id)
                                    ctx.startActivity(intent)
                                    if (ctx is android.app.Activity) {
                                        ctx.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                    }
                                },
                                onAlarmaClick = { Toast.makeText(ctx, "Recordatorio activado", Toast.LENGTH_SHORT).show() }
                            )
                        }
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

        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            BottomNavBar(currentScreen = "HOME")
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
                                    val intent = Intent(ctx, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    ctx.startActivity(intent)
                                    if (ctx is android.app.Activity) {
                                        ctx.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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

// Componente de Tarjeta Plegable (EventCard) solicitado en las instrucciones
@Composable
fun EventCard(event: Tarea, diaTexto: String, onExpand: (Boolean) -> Unit, onEditarClick: () -> Unit, onAlarmaClick: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF252525) else Color(0xFFF1EFFE))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (Repo.modoOscuro) Color.White else Color.Black
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = event.hora,
                        fontSize = 12.sp,
                        color = if (Repo.modoOscuro) Color(0xFF8FC7FF) else Color(0xFF3F5A8A)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = {
                            isExpanded = !isExpanded
                            onExpand(isExpanded)
                        }
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = if (isExpanded) "Colapsar detalles de la actividad" else "Expandir detalles de la actividad",
                            tint = if (Repo.modoOscuro) Color.White else Color.Black
                        )
                    }
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = event.desc ?: "Sin detalles adicionales",
                    fontSize = 13.sp,
                    color = if (Repo.modoOscuro) Color(0xFFD0D0D0) else Color.Gray
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Duracion aprox: 60 min",
                    fontSize = 12.sp,
                    color = if (Repo.modoOscuro) Color(0xFF8FC7FF) else Color(0xFF3F5A8A)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$diaTexto · Ago",
                        fontSize = 10.sp,
                        color = if (Repo.modoOscuro) Color.White else Color.Black
                    )
                    Image(
                        painter = painterResource(R.drawable.relojazul),
                        contentDescription = "Activar alarma",
                        modifier = Modifier.size(24.dp).clickable { onAlarmaClick() }
                    )
                    Image(
                        painter = painterResource(R.drawable.editarazul),
                        contentDescription = "Editar actividad",
                        modifier = Modifier.size(24.dp).clickable { onEditarClick() }
                    )
                }
            }
        }
    }
}

// Componente con gesto de deslizamiento para eliminar (Swipe to Dismiss)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableEventCard(
    event: Tarea,
    diaTexto: String,
    onDelete: () -> Unit,
    onEditarClick: () -> Unit,
    onAlarmaClick: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart || it == SwipeToDismissBoxValue.StartToEnd) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color = Color(0xFFC62828)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, RoundedCornerShape(20.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text("Eliminar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    ) {
        EventCard(
            event = event,
            diaTexto = diaTexto,
            onExpand = {},
            onEditarClick = onEditarClick,
            onAlarmaClick = onAlarmaClick
        )
    }
}

private fun navegarConTransicionSuave(ctx: android.content.Context, targetClass: Class<*>) {
    if (ctx.javaClass != targetClass) {
        val intent = Intent(ctx, targetClass)
        ctx.startActivity(intent)
        if (ctx is android.app.Activity) {
            ctx.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}

@Composable
private fun BottomNavBar(currentScreen: String) {
    val ctx = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            color = if (Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF1EFFE),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            shadowElevation = 12.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(130.dp)
                        .height(4.dp)
                        .background(
                            color = if (Repo.modoOscuro) Color.LightGray else Color.Black,
                            shape = CircleShape
                        )
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Home
            MainMenuNavItemButton(
                icon = Icons.Default.Home,
                label = "HOME",
                isActive = currentScreen == "HOME",
                onClick = { navegarConTransicionSuave(ctx, MainMenuActivity::class.java) }
            )

            // 2. Calendar
            MainMenuNavItemButton(
                icon = Icons.Default.DateRange,
                label = "CALEND",
                isActive = currentScreen == "CALENDARIO",
                onClick = { navegarConTransicionSuave(ctx, CalendarActivity::class.java) }
            )

            // 3. Settings / Config
            MainMenuNavItemButton(
                icon = Icons.Default.Settings,
                label = "CONFIG",
                isActive = currentScreen == "SETTINGS",
                onClick = { navegarConTransicionSuave(ctx, SettingsActivity::class.java) }
            )
        }
    }
}

@Composable
private fun MainMenuNavItemButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val size by animateDpAsState(
        targetValue = if (isActive) 68.dp else 52.dp,
        animationSpec = tween(durationMillis = 300),
        label = "sizeAnimation"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFF8BB5CE) else Color(0xFF3B5E8C),
        animationSpec = tween(durationMillis = 300),
        label = "colorAnimation"
    )
    val iconTint by animateColorAsState(
        targetValue = if (isActive) Color(0xFF1E3A5F) else Color.White,
        animationSpec = tween(durationMillis = 300),
        label = "tintAnimation"
    )
    val offsetY by animateDpAsState(
        targetValue = if (isActive) (-12).dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "offsetAnimation"
    )

    Surface(
        modifier = Modifier
            .offset(y = offsetY)
            .size(size)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = CircleShape,
        color = backgroundColor,
        shadowElevation = if (isActive) 8.dp else 3.dp,
        border = if (isActive) BorderStroke(2.dp, Color.White) else null
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(if (isActive) 22.dp else 24.dp)
                )
                if (isActive) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = label,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A5F)
                    )
                }
            }
        }
    }
}
