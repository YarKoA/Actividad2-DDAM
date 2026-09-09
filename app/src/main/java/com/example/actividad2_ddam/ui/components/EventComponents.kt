package com.example.actividad2_ddam.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.actividad2_ddam.R
import com.example.actividad2_ddam.model.Event
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.navigation.Routes

// Componente Tarjeta Plegable (EventCard) con animación de contenido
@Composable
fun EventCard(
    event: Event,
    diaTexto: String = event.dia,
    onExpand: (Boolean) -> Unit = {},
    onEditarClick: () -> Unit = {},
    onAlarmaClick: () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (Repo.modoOscuro) Color(0xFF252525) else Color(0xFFF1EFFE)
        )
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
                            contentDescription = if (isExpanded) "Colapsar detalles" else "Expandir detalles",
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Duración aprox: 60 min",
                    fontSize = 12.sp,
                    color = if (Repo.modoOscuro) Color(0xFF8FC7FF) else Color(0xFF3F5A8A)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$diaTexto · Ago",
                        fontSize = 10.sp,
                        color = if (Repo.modoOscuro) Color.White else Color.Black
                    )
                    Image(
                        painter = painterResource(R.drawable.relojazul),
                        contentDescription = "Activar alarma",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onAlarmaClick() }
                    )
                    Image(
                        painter = painterResource(R.drawable.editarazul),
                        contentDescription = "Editar actividad",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onEditarClick() }
                    )
                }
            }
        }
    }
}

// Componente con gesto de deslizamiento para eliminar (SwipeToDismissBox)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableEventCard(
    event: Event,
    diaTexto: String = event.dia,
    onDelete: () -> Unit,
    onEditarClick: () -> Unit = {},
    onAlarmaClick: () -> Unit = {}
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
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

// Barra de navegación inferior
@Composable
fun BottomNavBar(
    navController: NavController,
    currentScreen: String
) {
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
            NavItemButton(
                icon = Icons.Default.Home,
                label = "HOME",
                isActive = currentScreen == Routes.EVENT_LIST,
                onClick = {
                    if (currentScreen != Routes.EVENT_LIST) {
                        navController.navigate(Routes.EVENT_LIST) {
                            popUpTo(Routes.EVENT_LIST) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )

            // 2. Calendar
            NavItemButton(
                icon = Icons.Default.DateRange,
                label = "CALEND",
                isActive = currentScreen == Routes.CALENDAR,
                onClick = {
                    if (currentScreen != Routes.CALENDAR) {
                        navController.navigate(Routes.CALENDAR) {
                            popUpTo(Routes.EVENT_LIST) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )

            // 3. Settings
            NavItemButton(
                icon = Icons.Default.Settings,
                label = "CONFIG",
                isActive = currentScreen == Routes.SETTINGS,
                onClick = {
                    if (currentScreen != Routes.SETTINGS) {
                        navController.navigate(Routes.SETTINGS) {
                            popUpTo(Routes.EVENT_LIST) { saveState = true }
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
