package com.example.actividad2_ddam

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// Actividad para visualizar el calendario mensual de tareas
class CalendarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actividad2DDAMTheme(darkTheme = Repo.modoOscuro, dynamicColor = false) {
                CalendarScreen()
            }
        }
    }
}

// Diseño de la vista mensual del calendario
@Composable
fun CalendarScreen() {
    val ctx = LocalContext.current
    var yearMonth by remember { mutableStateOf(YearMonth.now()) }

    val nombreMes = yearMonth.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")).uppercase()
    val totalDias = yearMonth.lengthOfMonth()

    val fondo = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))

    Box(modifier = Modifier.fillMaxSize().background(fondo)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Encabezado del mes actual dinámico
            Card(
                modifier = Modifier.width(220.dp).height(45.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF292929) else Color(0xFFEDE2FF))
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("$nombreMes ${yearMonth.year}", fontWeight = FontWeight.Bold, color = if (Repo.modoOscuro) Color.White else Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Indicadores de los dias de la semana
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("L", "M", "M", "J", "V", "S", "D").forEach { dia ->
                    Card(
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF292929) else Color(0xFFEDE2FF))
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(dia, fontWeight = FontWeight.Bold, color = if (Repo.modoOscuro) Color.White else Color.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cuadricula para representar los dias del mes dinámicos
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(totalDias) { index ->
                    Card(
                        modifier = Modifier.height(80.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF292929) else Color(0xFFEDE2FF))
                    ) {
                        Box(Modifier.padding(4.dp)) {
                            Text((index + 1).toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (Repo.modoOscuro) Color.White else Color.Black)
                        }
                    }
                }
            }

            // Controles de navegacion de meses funcionales
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(onClick = { yearMonth = yearMonth.minusMonths(1) }) {
                    Icon(painterResource(android.R.drawable.ic_media_previous), contentDescription = "Mes anterior", tint = Color.White)
                }
                IconButton(onClick = { yearMonth = yearMonth.plusMonths(1) }) {
                    Icon(painterResource(android.R.drawable.ic_media_next), contentDescription = "Mes siguiente", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        // Barra de navegacion inferior moderna y funcional
        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            BottomNavBar(currentScreen = "CALENDARIO")
        }
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
            CalendarNavItemButton(
                icon = Icons.Default.Home,
                label = "HOME",
                isActive = currentScreen == "HOME",
                onClick = { navegarConTransicionSuave(ctx, MainMenuActivity::class.java) }
            )

            // 2. Calendar
            CalendarNavItemButton(
                icon = Icons.Default.DateRange,
                label = "CALEND",
                isActive = currentScreen == "CALENDARIO",
                onClick = { navegarConTransicionSuave(ctx, CalendarActivity::class.java) }
            )

            // 3. Settings / Config
            CalendarNavItemButton(
                icon = Icons.Default.Settings,
                label = "CONFIG",
                isActive = currentScreen == "SETTINGS",
                onClick = { navegarConTransicionSuave(ctx, SettingsActivity::class.java) }
            )
        }
    }
}

@Composable
private fun CalendarNavItemButton(
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
