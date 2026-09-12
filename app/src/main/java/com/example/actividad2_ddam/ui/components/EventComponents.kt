package com.example.actividad2_ddam.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.actividad2_ddam.R
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.model.Tarea
import com.example.actividad2_ddam.navigation.Routes
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EventCard(
    event: Tarea,
    diaTexto: String = event.dia,
    onExpand: (Boolean) -> Unit = {},
    onAnclar: () -> Unit = {},
    onEditarClick: () -> Unit = {},
    onAlarmaClick: () -> Unit = {}
) {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    // Estados de animación
    var animarAlarma by remember {
        mutableStateOf(false)
    }

    var animarEditar by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    // La animación completa dura 300 ms:
    // 150 ms para encogerse
    // 150 ms para regresar
    val escalaAlarma by animateFloatAsState(
        targetValue = if (animarAlarma) 0.70f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "alarmaAnimation"
    )

    val escalaEditar by animateFloatAsState(
        targetValue = if (animarEditar) 0.70f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "editarAnimation"
    )

    val mesActual =
        LocalDate.now()
            .month
            .getDisplayName(
                TextStyle.SHORT,
                Locale("es", "ES")
            )
            .replaceFirstChar {
                it.uppercase()
            }

    val pointerModifier =
        Modifier.pointerInput(
            event.id,
            event.esAnclada
        ) {
            detectTapGestures(
                onLongPress = {
                    onAnclar()
                }
            )
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(pointerModifier)
            .animateContentSize(),

        shape = RoundedCornerShape(20.dp),

        border =
            if (event.esAnclada) {
                BorderStroke(
                    2.dp,
                    Color(0xFF556DB5)
                )
            } else {
                null
            },

        colors = CardDefaults.cardColors(
            containerColor =
                if (event.esAnclada) {

                    if (Repo.modoOscuro) {
                        Color(0xFF343A4D)
                    } else {
                        Color(0xFFDCE6FF)
                    }

                } else {

                    if (Repo.modoOscuro) {
                        Color(0xFF252525)
                    } else {
                        Color(0xFFF1EFFE)
                    }
                }
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            if (event.esAnclada) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically,

                    modifier =
                        Modifier.padding(
                            bottom = 6.dp
                        )
                ) {

                    Text(
                        text = "📌 Tarea importante",
                        fontSize = 12.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(0xFF3B5E8C)
                    )
                }
            }

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = event.titulo,
                    fontWeight =
                        FontWeight.Bold,
                    fontSize = 15.sp,

                    color =
                        if (Repo.modoOscuro) {
                            Color.White
                        } else {
                            Color.Black
                        }
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = event.hora,
                        fontSize = 12.sp,

                        color =
                            if (Repo.modoOscuro) {
                                Color(0xFF8FC7FF)
                            } else {
                                Color(0xFF3F5A8A)
                            }
                    )

                    Spacer(
                        modifier =
                            Modifier.width(4.dp)
                    )

                    IconButton(
                        onClick = {

                            isExpanded =
                                !isExpanded

                            onExpand(
                                isExpanded
                            )
                        }
                    ) {

                        Icon(
                            imageVector =
                                if (isExpanded) {
                                    Icons.Filled.ExpandLess
                                } else {
                                    Icons.Filled.ExpandMore
                                },

                            contentDescription =
                                if (isExpanded) {
                                    "Colapsar detalles"
                                } else {
                                    "Expandir detalles"
                                },

                            tint =
                                if (Repo.modoOscuro) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                        )
                    }
                }
            }

            if (isExpanded) {

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text =
                        event.desc
                            ?: "Sin detalles adicionales",

                    fontSize = 13.sp,

                    color =
                        if (Repo.modoOscuro) {
                            Color(0xFFD0D0D0)
                        } else {
                            Color.Gray
                        }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "Duración aprox: 60 min",

                    fontSize = 12.sp,

                    color =
                        if (Repo.modoOscuro) {
                            Color(0xFF8FC7FF)
                        } else {
                            Color(0xFF3F5A8A)
                        }
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            "$diaTexto · $mesActual",

                        fontSize = 10.sp,

                        color =
                            if (Repo.modoOscuro) {
                                Color.White
                            } else {
                                Color.Black
                            }
                    )

                    // -------------------------
                    // BOTÓN ALARMA
                    // -------------------------

                    Image(
                        painter =
                            painterResource(
                                R.drawable.relojazul
                            ),

                        contentDescription =
                            "Activar alarma",

                        modifier = Modifier
                            .size(24.dp)
                            .scale(escalaAlarma)
                            .clickable(
                                interactionSource =
                                    remember {
                                        MutableInteractionSource()
                                    },

                                indication = null
                            ) {

                                scope.launch {

                                    // Se encoge
                                    animarAlarma = true

                                    delay(150)

                                    // Regresa
                                    animarAlarma = false

                                    delay(150)

                                    // Después ejecuta
                                    // la función original
                                    onAlarmaClick()
                                }
                            }
                    )

                    // -------------------------
                    // BOTÓN EDITAR
                    // -------------------------

                    Image(
                        painter =
                            painterResource(
                                R.drawable.editarazul
                            ),

                        contentDescription =
                            "Editar actividad",

                        modifier = Modifier
                            .size(24.dp)
                            .scale(escalaEditar)
                            .clickable(
                                interactionSource =
                                    remember {
                                        MutableInteractionSource()
                                    },

                                indication = null
                            ) {

                                scope.launch {

                                    // Se encoge
                                    animarEditar = true

                                    delay(150)

                                    // Regresa
                                    animarEditar = false

                                    delay(150)

                                    // Después navega
                                    // a editar
                                    onEditarClick()
                                }
                            }
                    )
                }
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun SwipeableEventCard(
    event: Tarea,
    diaTexto: String = event.dia,
    onDelete: () -> Unit,
    onAnclar: () -> Unit = {},
    onEditarClick: () -> Unit = {},
    onAlarmaClick: () -> Unit = {}
) {

    val dismissState =
        rememberSwipeToDismissBoxState(

            confirmValueChange = {
                    dismissValue ->

                if (
                    dismissValue ==
                    SwipeToDismissBoxValue
                        .EndToStart
                    ||
                    dismissValue ==
                    SwipeToDismissBoxValue
                        .StartToEnd
                ) {

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

            val color =
                Color(0xFFC62828)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color,
                        RoundedCornerShape(
                            20.dp
                        )
                    )
                    .padding(
                        horizontal = 20.dp
                    ),

                contentAlignment =
                    Alignment.CenterEnd
            ) {

                Text(
                    text =
                        "Deslizar para eliminar",

                    color =
                        Color.White,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    ) {

        EventCard(
            event = event,
            diaTexto = diaTexto,
            onExpand = {},
            onAnclar = onAnclar,
            onEditarClick =
                onEditarClick,
            onAlarmaClick =
                onAlarmaClick
        )
    }
}

@Composable
fun BottomNavBar(
    navController: NavController,
    currentScreen: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),

        contentAlignment =
            Alignment.BottomCenter
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),

            color =
                if (Repo.modoOscuro) {
                    Color(0xFF202020)
                } else {
                    Color(0xFFF1EFFE)
                },

            shape =
                RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp
                ),

            shadowElevation = 12.dp
        ) {

            Box(
                modifier =
                    Modifier.fillMaxSize(),

                contentAlignment =
                    Alignment.BottomCenter
            ) {

                Box(
                    modifier = Modifier
                        .padding(
                            bottom = 8.dp
                        )
                        .width(130.dp)
                        .height(4.dp)
                        .background(
                            color =
                                if (
                                    Repo.modoOscuro
                                ) {
                                    Color.LightGray
                                } else {
                                    Color.Black
                                },

                            shape =
                                CircleShape
                        )
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 14.dp
                ),

            horizontalArrangement =
                Arrangement.spacedBy(16.dp),

            verticalAlignment =
                Alignment.Bottom
        ) {

            NavItemButton(
                icon =
                    Icons.Default.Home,

                label = "HOME",

                isActive =
                    currentScreen ==
                            Routes.EVENT_LIST,

                onClick = {

                    if (
                        currentScreen !=
                        Routes.EVENT_LIST
                    ) {

                        navController.navigate(
                            Routes.EVENT_LIST
                        ) {

                            popUpTo(
                                Routes.EVENT_LIST
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    }
                }
            )

            NavItemButton(
                icon =
                    Icons.Default.DateRange,

                label = "CALEND",

                isActive =
                    currentScreen ==
                            Routes.CALENDAR,

                onClick = {

                    if (
                        currentScreen !=
                        Routes.CALENDAR
                    ) {

                        navController.navigate(
                            Routes.CALENDAR
                        ) {

                            popUpTo(
                                Routes.EVENT_LIST
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    }
                }
            )

            NavItemButton(
                icon =
                    Icons.Default.Settings,

                label = "CONFIG",

                isActive =
                    currentScreen ==
                            Routes.SETTINGS,

                onClick = {

                    if (
                        currentScreen !=
                        Routes.SETTINGS
                    ) {

                        navController.navigate(
                            Routes.SETTINGS
                        ) {

                            popUpTo(
                                Routes.EVENT_LIST
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun NavItemButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {

    val size by animateDpAsState(
        targetValue =
            if (isActive) {
                72.dp
            } else {
                54.dp
            },

        animationSpec =
            tween(
                durationMillis = 200
            ),

        label = "sizeAnimation"
    )

    val offsetY by animateDpAsState(
        targetValue =
            if (isActive) {
                (-20).dp
            } else {
                0.dp
            },

        animationSpec =
            tween(
                durationMillis = 200
            ),

        label = "offsetAnimation"
    )

    val backgroundColor
            by animateColorAsState(

                targetValue =
                    if (isActive) {
                        Color(0xFFAAD9E8)
                    } else {
                        Color(0xFF385A79)
                    },

                animationSpec =
                    tween(
                        durationMillis = 200
                    ),

                label =
                    "backgroundAnimation"
            )

    val iconTint
            by animateColorAsState(

                targetValue =
                    if (isActive) {
                        Color(0xFF385A79)
                    } else {
                        Color.White
                    },

                animationSpec =
                    tween(
                        durationMillis = 200
                    ),

                label =
                    "iconAnimation"
            )

    Surface(
        modifier = Modifier
            .offset(y = offsetY)
            .size(size)
            .clickable(
                interactionSource =
                    remember {
                        MutableInteractionSource()
                    },

                indication = null
            ) {
                onClick()
            },

        shape = CircleShape,

        color = backgroundColor,

        shadowElevation =
            if (isActive) {
                10.dp
            } else {
                2.dp
            },

        border =
            if (isActive) {

                BorderStroke(
                    2.dp,
                    Color.White
                )

            } else {

                null
            }
    ) {

        Box(
            contentAlignment =
                Alignment.Center
        ) {

            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Center
            ) {

                Icon(
                    imageVector = icon,

                    contentDescription =
                        label,

                    tint = iconTint,

                    modifier =
                        Modifier.size(
                            if (isActive) {
                                32.dp
                            } else {
                                28.dp
                            }
                        )
                )

                if (isActive) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                1.dp
                            )
                    )

                    Text(
                        text = label,

                        fontSize = 10.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(
                                0xFF385A79
                            )
                    )
                }
            }
        }
    }
}