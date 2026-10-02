package com.example.actividad2_ddam.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.actividad2_ddam.FormularioCuentaDialog
import com.example.actividad2_ddam.R
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.navigation.Routes
import com.example.actividad2_ddam.ui.components.BottomNavBar
import com.example.actividad2_ddam.ui.components.SwipeableEventCard
import com.example.actividad2_ddam.viewmodel.EventViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.actividad2_ddam.ui.theme.scaledSp
import com.example.actividad2_ddam.ui.theme.scaledWeight

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
    var mostrarPanelAgregar by remember { mutableStateOf(false) }
    var eventoEditandoId by remember { mutableStateOf<Int?>(null) }
    var mostrarPanelEdicion by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val fondo = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))
    val diaSeleccionado by viewModel.diaSeleccionado.collectAsState()

    var diaAnteriorIndex by remember { mutableIntStateOf(0) }
    val diasSemana = listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom")
    val diaActualIndex = diasSemana.indexOf(diaSeleccionado)
    val slideDirection = if (diaActualIndex >= diaAnteriorIndex) 1 else -1

    LaunchedEffect(diaSeleccionado) { diaAnteriorIndex = diaActualIndex }

    val tareasFiltradas by viewModel.tareasFiltradas.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(fondo)) {
        val isWideScreen = maxWidth > 600.dp

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier.widthIn(max = 900.dp).fillMaxSize().padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(44.dp))

                val primeraLetra = usuario?.nombre?.trim()?.firstOrNull()?.uppercase() ?: "U"

                // HEADER
                HeaderSection(
                    userName = usuario?.nombre ?: "Usuario",
                    initial = primeraLetra,
                    onProfileClick = { mostrarPerfil = true }
                )

                // DAY SELECTOR
                DaySelectorRow(
                    dias = diasSemana,
                    diaSeleccionado = diaSeleccionado,
                    onDiaClick = { viewModel.actualizarDiaSeleccionado(it) }
                )

                // TASK LIST
                AnimatedContent(
                    targetState = diaSeleccionado,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(300)) { it * slideDirection } togetherWith
                                slideOutHorizontally(animationSpec = tween(300)) { -it * slideDirection }
                    },
                    label = "daySlideAnimation",
                    modifier = Modifier.weight(1f)
                ) { _ ->
                    if (tareasFiltradas.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = stringResource(R.string.no_activities), color = Color.White)
                        }
                    } else {
                        TaskListContent(
                            tareas = tareasFiltradas,
                            diaSeleccionado = diaSeleccionado,
                            isWideScreen = isWideScreen,
                            onDelete = { tarea ->
                                viewModel.removeEvent(tarea)
                                Toast.makeText(ctx, ctx.getString(R.string.activity_deleted), Toast.LENGTH_SHORT).show()
                            },
                            onAnclar = { tarea ->
                                viewModel.toggleAnclar(tarea)
                                val msj = if (!tarea.esAnclada) ctx.getString(R.string.task_pinned) else ctx.getString(R.string.task_unpinned)
                                Toast.makeText(ctx, msj, Toast.LENGTH_SHORT).show()
                            },
                            onEditar = { tarea ->
                                eventoEditandoId = tarea.id
                                mostrarPanelEdicion = true
                            },
                            onAlarma = {
                                Toast.makeText(ctx, ctx.getString(R.string.reminder_activated), Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(90.dp))
            }
        }

        // FAB
        FloatingActionButton(
            onClick = { mostrarPanelAgregar = true },
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 24.dp, bottom = 130.dp).size(80.dp),
            containerColor = Color(0xFF456B8C),
            shape = CircleShape
        ) {
            Text(text = "+", color = Color.White, fontSize = 42.sp.scaledSp)
        }

        // BOTTOM BAR
        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            BottomNavBar(navController = navController, currentScreen = Routes.EVENT_LIST)
        }

        // PROFILE DIALOG
        if (mostrarPerfil) {
            ProfileDialog(
                usuario = usuario,
                onDismiss = { mostrarPerfil = false },
                onEditar = { mostrarPerfil = false; mostrarEditar = true },
                onCerrarSesion = {
                    mostrarPerfil = false
                    navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        // EDIT ACCOUNT DIALOG
        if (mostrarEditar) {
            FormularioCuentaDialog(
                titulo = "Editar Cuenta",
                usuarioInicial = usuario,
                onDismiss = { mostrarEditar = false },
                onGuardar = { usuarioEditado ->
                    Repo.usuarioActual = usuarioEditado
                    usuario = usuarioEditado
                    Toast.makeText(ctx, ctx.getString(R.string.account_updated), Toast.LENGTH_SHORT).show()
                    mostrarEditar = false
                }
            )
        }

        // ADD PANEL
        AnimatedVisibility(
            visible = mostrarPanelAgregar,
            enter = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
            exit = slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f))) {
                EventFormScreen(
                    navController = navController,
                    viewModel = viewModel,
                    onCerrar = { mostrarPanelAgregar = false }
                )
            }
        }

        // EDIT PANEL
        AnimatedVisibility(
            visible = mostrarPanelEdicion,
            enter = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
            exit = slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
        ) {
            eventoEditandoId?.let { id ->
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f))) {
                    EventEditScreen(
                        eventId = id,
                        viewModel = viewModel,
                        modoOverlay = true,
                        onCerrar = {
                            mostrarPanelEdicion = false
                            scope.launch { delay(300); eventoEditandoId = null }
                        }
                    )
                }
            }
        }
    }
}

// ========================================
// EXTRACTED COMPOSABLES
// ========================================

@Composable
private fun HeaderSection(userName: String, initial: String, onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.welcome_user, userName),
            color = Color.White, fontSize = 18.sp.scaledSp, fontWeight = FontWeight.Bold.scaledWeight
        )
        Surface(
            modifier = Modifier.size(48.dp).clickable { onProfileClick() },
            shape = CircleShape, color = Color(0xFF385A79),
            border = BorderStroke(2.dp, Color.White), shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = initial, color = Color.White, fontSize = 24.sp.scaledSp, fontWeight = FontWeight.Bold.scaledWeight)
            }
        }
    }
}

@Composable
private fun DaySelectorRow(dias: List<String>, diaSeleccionado: String, onDiaClick: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        dias.forEach { dia ->
            val esActivo = dia == diaSeleccionado
            Card(
                modifier = Modifier.weight(1f).height(38.dp).clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDiaClick(dia) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (esActivo) Color(0xFF50629A) else if (Repo.modoOscuro) Color(0xFF343434) else Color(0xFF91A5B7)
                )
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = dia, fontSize = 12.sp.scaledSp,
                        color = if (esActivo || Repo.modoOscuro) Color.White else Color(0xFF2D3E5B)
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskListContent(
    tareas: List<com.example.actividad2_ddam.model.Tarea>,
    diaSeleccionado: String,
    isWideScreen: Boolean,
    onDelete: (com.example.actividad2_ddam.model.Tarea) -> Unit,
    onAnclar: (com.example.actividad2_ddam.model.Tarea) -> Unit,
    onEditar: (com.example.actividad2_ddam.model.Tarea) -> Unit,
    onAlarma: () -> Unit
) {
    if (isWideScreen) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tareas, key = { it.id }) { tarea ->
                SwipeableEventCard(
                    event = tarea, diaTexto = diaSeleccionado,
                    onDelete = { onDelete(tarea) }, onAnclar = { onAnclar(tarea) },
                    onEditarClick = { onEditar(tarea) }, onAlarmaClick = { onAlarma() }
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tareas, key = { it.id }) { tarea ->
                SwipeableEventCard(
                    event = tarea, diaTexto = diaSeleccionado,
                    onDelete = { onDelete(tarea) }, onAnclar = { onAnclar(tarea) },
                    onEditarClick = { onEditar(tarea) }, onAlarmaClick = { onAlarma() }
                )
            }
        }
    }
}

@Composable
private fun ProfileDialog(
    usuario: com.example.actividad2_ddam.model.Usuario?,
    onDismiss: () -> Unit,
    onEditar: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (Repo.modoOscuro) Color(0xFF252525) else Color(0xFFF1EFFE)
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header gradient
                Box(
                    modifier = Modifier.fillMaxWidth().background(
                        Brush.horizontalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94))),
                        shape = RoundedCornerShape(16.dp)
                    ).padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.profile_title), color = Color.White, fontSize = 20.sp.scaledSp, fontWeight = FontWeight.Bold.scaledWeight)
                }

                val inicial = usuario?.nombre?.trim()?.firstOrNull()?.uppercase() ?: "U"
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape, color = Color(0xFF2C3E6B),
                    border = BorderStroke(3.dp, Color.White)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = inicial, color = Color.White, fontSize = 36.sp.scaledSp, fontWeight = FontWeight.Bold.scaledWeight)
                    }
                }

                Text(
                    text = usuario?.nombre ?: "", fontSize = 20.sp.scaledSp, fontWeight = FontWeight.Bold.scaledWeight,
                    color = if (Repo.modoOscuro) Color.White else Color(0xFF1E293B)
                )

                // Info card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (Repo.modoOscuro) Color(0xFF343434) else Color(0xFFEDE2FF)
                    )
                ) {
                    val textColor = if (Repo.modoOscuro) Color.White else Color(0xFF2D3E5B)
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(stringResource(R.string.profile_email, usuario?.correo ?: ""), color = textColor)
                        Text(stringResource(R.string.profile_phone, usuario?.telefono ?: ""), color = textColor)
                        Text(stringResource(R.string.profile_age, usuario?.edad?.toString() ?: ""), color = textColor)
                    }
                }

                Button(
                    onClick = onEditar,
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E6B))
                ) {
                    Text(stringResource(R.string.profile_edit), color = Color.White, fontWeight = FontWeight.Bold.scaledWeight)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = onCerrarSesion) {
                        Text(stringResource(R.string.profile_logout), color = Color(0xFFC62828))
                    }
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.profile_back), color = Color(0xFF2C3E6B))
                    }
                }
            }
        }
    }
}