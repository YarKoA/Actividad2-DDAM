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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.actividad2_ddam.FormularioCuentaDialog
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.navigation.Routes
import com.example.actividad2_ddam.ui.components.BottomNavBar
import com.example.actividad2_ddam.ui.components.SwipeableEventCard
import com.example.actividad2_ddam.viewmodel.EventViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EventListScreen(
    navController: NavController,
    viewModel: EventViewModel = hiltViewModel(),
    onAddEventClick: () -> Unit
) {

    val ctx =
        LocalContext.current

    var usuario by remember {
        mutableStateOf(
            Repo.usuarioActual
        )
    }

    var mostrarPerfil by remember {
        mutableStateOf(false)
    }

    var mostrarEditar by remember {
        mutableStateOf(false)
    }

    // -------------------------
    // PANEL AÑADIR
    // -------------------------

    var mostrarPanelAgregar by remember {
        mutableStateOf(false)
    }

    // -------------------------
    // PANEL EDITAR
    // -------------------------

    var eventoEditandoId by remember {
        mutableStateOf<Int?>(null)
    }

    var mostrarPanelEdicion by remember {
        mutableStateOf(false)
    }

    val scope =
        rememberCoroutineScope()

    val fondo =
        Brush.verticalGradient(
            listOf(
                Color(0xFF2C3E6B),
                Color(0xFF4B6B94),
                Color(0xFF8BB5CE)
            )
        )

    val diaSeleccionado by
    viewModel
        .diaSeleccionado
        .collectAsState()

    // Para la animación de deslizamiento
    var diaAnteriorIndex by remember {
        mutableIntStateOf(0)
    }

    val diasSemana = listOf(
        "Lun", "Mar", "Mie",
        "Jue", "Vie", "Sab",
        "Dom"
    )

    val diaActualIndex =
        diasSemana.indexOf(
            diaSeleccionado
        )

    val slideDirection =
        if (diaActualIndex >=
            diaAnteriorIndex
        ) 1 else -1

    LaunchedEffect(
        diaSeleccionado
    ) {
        diaAnteriorIndex =
            diaActualIndex
    }

    val tareasFiltradas by
    viewModel
        .tareasFiltradas
        .collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
    ) {

        val isWideScreen =
            maxWidth > 600.dp

        Box(
            modifier =
                Modifier.fillMaxSize(),

            contentAlignment =
                Alignment.TopCenter
        ) {

            Column(
                modifier = Modifier
                    .widthIn(
                        max = 900.dp
                    )
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp
                    )
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            44.dp
                        )
                )

                val primeraLetra =
                    usuario
                        ?.nombre
                        ?.trim()
                        ?.firstOrNull()
                        ?.uppercase()
                        ?: "U"

                // -------------------------
                // ENCABEZADO
                // -------------------------

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 12.dp
                        ),

                    horizontalArrangement =
                        Arrangement
                            .SpaceBetween,

                    verticalAlignment =
                        Alignment
                            .CenterVertically
                ) {

                    Text(
                        text =
                            "Bienvenido, ${usuario?.nombre ?: "Usuario"}",

                        color =
                            Color.White,

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Surface(
                        modifier =
                            Modifier
                                .size(48.dp)
                                .clickable {

                                    mostrarPerfil =
                                        true
                                },

                        shape =
                            CircleShape,

                        color =
                            Color(
                                0xFF385A79
                            ),

                        border =
                            BorderStroke(
                                2.dp,
                                Color.White
                            ),

                        shadowElevation =
                            4.dp
                    ) {

                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text =
                                    primeraLetra,

                                color =
                                    Color.White,

                                fontSize =
                                    24.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }

                // -------------------------
                // DÍAS
                // -------------------------

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement
                            .SpaceBetween
                ) {

                    listOf(
                        "Lun",
                        "Mar",
                        "Mie",
                        "Jue",
                        "Vie",
                        "Sab",
                        "Dom"
                    ).forEach { dia ->

                        val esActivo =
                            dia ==
                                    diaSeleccionado

                        Card(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .clickable(
                                        interactionSource =
                                            remember {
                                                MutableInteractionSource()
                                            },

                                        indication =
                                            null
                                    ) {

                                        viewModel
                                            .actualizarDiaSeleccionado(
                                                dia
                                            )
                                    },

                            shape =
                                RoundedCornerShape(
                                    10.dp
                                ),

                            colors =
                                CardDefaults
                                    .cardColors(
                                        containerColor =
                                            if (
                                                esActivo
                                            ) {

                                                Color(
                                                    0xFF556DB5
                                                )

                                            } else if (
                                                Repo.modoOscuro
                                            ) {

                                                Color(
                                                    0xFF343434
                                                )

                                            } else {

                                                Color(
                                                    0xFF71808A
                                                )
                                            }
                                    )
                        ) {

                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize(),

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Text(
                                    text = dia,

                                    fontSize =
                                        12.sp,

                                    color =
                                        if (
                                            esActivo ||
                                            Repo.modoOscuro
                                        ) {
                                            Color.White
                                        } else {
                                            Color.Black
                                        }
                                )
                            }
                        }
                    }
                }

                // -------------------------
                // LISTA
                // -------------------------

                AnimatedContent(
                    targetState =
                        diaSeleccionado,

                    transitionSpec = {
                        slideInHorizontally(
                            animationSpec =
                                tween(300)
                        ) {
                            it * slideDirection
                        } togetherWith
                        slideOutHorizontally(
                            animationSpec =
                                tween(300)
                        ) {
                            -it * slideDirection
                        }
                    },

                    label =
                        "daySlideAnimation",

                    modifier = Modifier
                        .weight(1f)
                ) { dia ->

                    if (
                        tareasFiltradas
                            .isEmpty()
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize(),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text =
                                    "Sin actividades para hoy",

                                color =
                                    Color.White
                            )
                        }

                    } else {

                        if (isWideScreen) {

                            LazyVerticalGrid(
                                columns =
                                    GridCells.Fixed(2),

                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        top = 20.dp
                                    ),

                                verticalArrangement =
                                    Arrangement
                                        .spacedBy(
                                            16.dp
                                        ),

                                horizontalArrangement =
                                    Arrangement
                                        .spacedBy(
                                            16.dp
                                        )
                            ) {

                                items(
                                    tareasFiltradas,
                                    key = {
                                        it.id
                                    }
                                ) { tarea ->

                                    SwipeableEventCard(
                                        event =
                                            tarea,

                                        diaTexto =
                                            diaSeleccionado,

                                        onDelete = {

                                            viewModel
                                                .removeEvent(
                                                    tarea
                                                )

                                            Toast
                                                .makeText(
                                                    ctx,
                                                    "Actividad eliminada",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        },

                                        onAnclar = {

                                            viewModel
                                                .toggleAnclar(
                                                    tarea
                                                )

                                            val msj =
                                                if (
                                                    !tarea.esAnclada
                                                ) {
                                                    "\uD83D\uDCCC Tarea marcada como importante"
                                                } else {
                                                    "Tarea desmarcada"
                                                }

                                            Toast
                                                .makeText(
                                                    ctx,
                                                    msj,
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        },

                                        onEditarClick = {

                                            eventoEditandoId =
                                                tarea.id

                                            mostrarPanelEdicion =
                                                true
                                        },

                                        onAlarmaClick = {

                                            Toast
                                                .makeText(
                                                    ctx,
                                                    "Recordatorio activado",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    )
                                }
                            }

                        } else {

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        top = 20.dp
                                    ),

                                verticalArrangement =
                                    Arrangement
                                        .spacedBy(
                                            16.dp
                                        )
                            ) {

                                items(
                                    tareasFiltradas,
                                    key = {
                                        it.id
                                    }
                                ) { tarea ->

                                    SwipeableEventCard(
                                        event =
                                            tarea,

                                        diaTexto =
                                            diaSeleccionado,

                                        onDelete = {

                                            viewModel
                                                .removeEvent(
                                                    tarea
                                                )

                                            Toast
                                                .makeText(
                                                    ctx,
                                                    "Actividad eliminada",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        },

                                        onAnclar = {

                                            viewModel
                                                .toggleAnclar(
                                                    tarea
                                                )

                                            val msj =
                                                if (
                                                    !tarea.esAnclada
                                                ) {
                                                    "\uD83D\uDCCC Tarea marcada como importante"
                                                } else {
                                                    "Tarea desmarcada"
                                                }

                                            Toast
                                                .makeText(
                                                    ctx,
                                                    msj,
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        },

                                        onEditarClick = {

                                            eventoEditandoId =
                                                tarea.id

                                            mostrarPanelEdicion =
                                                true
                                        },

                                        onAlarmaClick = {

                                            Toast
                                                .makeText(
                                                    ctx,
                                                    "Recordatorio activado",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            90.dp
                        )
                )
            }
        }

        // -------------------------
        // BOTÓN +
        // -------------------------

        FloatingActionButton(
            onClick = {

                mostrarPanelAgregar =
                    true
            },

            modifier =
                Modifier
                    .align(
                        Alignment
                            .BottomStart
                    )
                    .padding(
                        start = 24.dp,
                        bottom = 100.dp
                    )
                    .size(
                        56.dp
                    ),

            containerColor =
                Color(
                    0xFF3B5E8C
                ),

            shape =
                CircleShape
        ) {

            Text(
                text = "+",

                color =
                    Color.White,

                fontSize =
                    32.sp
            )
        }

        // -------------------------
        // BARRA INFERIOR
        // -------------------------

        Box(
            modifier =
                Modifier
                    .align(
                        Alignment
                            .BottomCenter
                    )
                    .fillMaxWidth()
        ) {

            BottomNavBar(
                navController =
                    navController,

                currentScreen =
                    Routes.EVENT_LIST
            )
        }

        // -------------------------
        // PERFIL
        // -------------------------

        if (mostrarPerfil) {

            Dialog(
                onDismissRequest = {
                    mostrarPerfil =
                        false
                }
            ) {

                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                16.dp
                            ),

                    shape =
                        RoundedCornerShape(
                            24.dp
                        ),

                    colors =
                        CardDefaults
                            .cardColors(
                                containerColor =
                                    if (
                                        Repo.modoOscuro
                                    ) {
                                        Color(
                                            0xFF252525
                                        )
                                    } else {
                                        Color(
                                            0xFFF1EFFE
                                        )
                                    }
                            ),

                    elevation =
                        CardDefaults
                            .cardElevation(
                                8.dp
                            )
                ) {

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    20.dp
                                ),

                        horizontalAlignment =
                            Alignment
                                .CenterHorizontally,

                        verticalArrangement =
                            Arrangement
                                .spacedBy(
                                    10.dp
                                )
                    ) {

                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush
                                            .horizontalGradient(
                                                listOf(
                                                    Color(
                                                        0xFF2C3E6B
                                                    ),
                                                    Color(
                                                        0xFF4B6B94
                                                    )
                                                )
                                            ),

                                        shape =
                                            RoundedCornerShape(
                                                16.dp
                                            )
                                    )
                                    .padding(
                                        vertical =
                                            12.dp
                                    ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text =
                                    "Perfil de Usuario",

                                color =
                                    Color.White,

                                fontSize =
                                    20.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }

                        val inicial =
                            usuario
                                ?.nombre
                                ?.trim()
                                ?.firstOrNull()
                                ?.uppercase()
                                ?: "U"

                        Surface(
                            modifier =
                                Modifier
                                    .size(
                                        72.dp
                                    ),

                            shape =
                                CircleShape,

                            color =
                                Color(
                                    0xFF2C3E6B
                                ),

                            border =
                                BorderStroke(
                                    3.dp,
                                    Color.White
                                )
                        ) {

                            Box(
                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Text(
                                    text =
                                        inicial,

                                    color =
                                        Color.White,

                                    fontSize =
                                        36.sp,

                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text =
                                usuario?.nombre
                                    ?: "",

                            fontSize =
                                20.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                if (
                                    Repo.modoOscuro
                                ) {
                                    Color.White
                                } else {
                                    Color(
                                        0xFF1E293B
                                    )
                                }
                        )

                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth(),

                            shape =
                                RoundedCornerShape(
                                    16.dp
                                ),

                            colors =
                                CardDefaults
                                    .cardColors(
                                        containerColor =
                                            if (
                                                Repo.modoOscuro
                                            ) {
                                                Color(
                                                    0xFF343434
                                                )
                                            } else {
                                                Color(
                                                    0xFFEDE2FF
                                                )
                                            }
                                    )
                        ) {

                            Column(
                                modifier =
                                    Modifier
                                        .padding(
                                            14.dp
                                        ),

                                verticalArrangement =
                                    Arrangement
                                        .spacedBy(
                                            6.dp
                                        )
                            ) {

                                Text(
                                    text =
                                        "Correo: ${usuario?.correo ?: ""}",

                                    color =
                                        if (
                                            Repo.modoOscuro
                                        ) {
                                            Color.White
                                        } else {
                                            Color.Black
                                        }
                                )

                                Text(
                                    text =
                                        "Teléfono: ${usuario?.telefono ?: ""}",

                                    color =
                                        if (
                                            Repo.modoOscuro
                                        ) {
                                            Color.White
                                        } else {
                                            Color.Black
                                        }
                                )

                                Text(
                                    text =
                                        "Edad: ${usuario?.edad ?: ""} años",

                                    color =
                                        if (
                                            Repo.modoOscuro
                                        ) {
                                            Color.White
                                        } else {
                                            Color.Black
                                        }
                                )
                            }
                        }

                        Button(
                            onClick = {

                                mostrarPerfil =
                                    false

                                mostrarEditar =
                                    true
                            },

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(
                                        46.dp
                                    ),

                            shape =
                                RoundedCornerShape(
                                    20.dp
                                ),

                            colors =
                                ButtonDefaults
                                    .buttonColors(
                                        containerColor =
                                            Color(
                                                0xFF2C3E6B
                                            )
                                    )
                        ) {

                            Text(
                                text =
                                    "Editar cuenta",

                                color =
                                    Color.White,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }

                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement
                                    .SpaceBetween
                        ) {

                            TextButton(
                                onClick = {

                                    mostrarPerfil =
                                        false

                                    navController
                                        .navigate(
                                            Routes.LOGIN
                                        ) {

                                            popUpTo(0) {
                                                inclusive =
                                                    true
                                            }
                                        }
                                }
                            ) {

                                Text(
                                    text =
                                        "Cerrar sesión",

                                    color =
                                        Color(
                                            0xFFC62828
                                        )
                                )
                            }

                            TextButton(
                                onClick = {

                                    mostrarPerfil =
                                        false
                                }
                            ) {

                                Text(
                                    text =
                                        "Volver al menú",

                                    color =
                                        Color(
                                            0xFF2C3E6B
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }

        if (mostrarEditar) {

            FormularioCuentaDialog(
                titulo =
                    "Editar Cuenta",

                usuarioInicial =
                    usuario,

                onDismiss = {

                    mostrarEditar =
                        false
                },

                onGuardar = {
                        usuarioEditado ->

                    Repo.usuarioActual =
                        usuarioEditado

                    usuario =
                        usuarioEditado

                    Toast
                        .makeText(
                            ctx,
                            "Cuenta actualizada",
                            Toast.LENGTH_SHORT
                        )
                        .show()

                    mostrarEditar =
                        false
                }
            )
        }

        // ==================================
        // PANEL AÑADIR ACTIVIDAD
        // ==================================

        AnimatedVisibility(
            visible =
                mostrarPanelAgregar,

            enter =
                slideInHorizontally(
                    initialOffsetX = {
                        -it
                    },

                    animationSpec =
                        tween(
                            durationMillis =
                                300
                        )
                ) +
                        fadeIn(
                            animationSpec =
                                tween(
                                    durationMillis =
                                        300
                                )
                        ),

            exit =
                slideOutHorizontally(
                    targetOffsetX = {
                        -it
                    },

                    animationSpec =
                        tween(
                            durationMillis =
                                300
                        )
                ) +
                        fadeOut(
                            animationSpec =
                                tween(
                                    durationMillis =
                                        300
                                )
                        )
        ) {

            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Color.Black.copy(
                                alpha =
                                    0.25f
                            )
                        )
            ) {

                EventFormScreen(
                    navController =
                        navController,

                    viewModel =
                        viewModel,

                    modoOverlay =
                        true,

                    onCerrar = {

                        mostrarPanelAgregar =
                            false
                    }
                )
            }
        }

        // ==================================
        // PANEL EDITAR ACTIVIDAD
        // ==================================

        AnimatedVisibility(
            visible =
                mostrarPanelEdicion,

            enter =
                slideInHorizontally(
                    initialOffsetX = {
                        -it
                    },

                    animationSpec =
                        tween(
                            durationMillis =
                                300
                        )
                ) +
                        fadeIn(
                            animationSpec =
                                tween(
                                    durationMillis =
                                        300
                                )
                        ),

            exit =
                slideOutHorizontally(
                    targetOffsetX = {
                        -it
                    },

                    animationSpec =
                        tween(
                            durationMillis =
                                300
                        )
                ) +
                        fadeOut(
                            animationSpec =
                                tween(
                                    durationMillis =
                                        300
                                )
                        )
        ) {

            eventoEditandoId?.let {
                    id ->

                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(
                                Color.Black.copy(
                                    alpha =
                                        0.25f
                                )
                            )
                ) {

                    EventEditScreen(
                        eventId = id,

                        viewModel =
                            viewModel,

                        modoOverlay =
                            true,

                        onCerrar = {

                            mostrarPanelEdicion =
                                false

                            scope.launch {

                                delay(300)

                                eventoEditandoId =
                                    null
                            }
                        }
                    )
                }
            }
        }
    }
}