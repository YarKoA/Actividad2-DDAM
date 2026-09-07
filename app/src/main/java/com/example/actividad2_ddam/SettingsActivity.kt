package com.example.actividad2_ddam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Actividad2DDAMTheme(darkTheme = Repo.modoOscuro, dynamicColor = false) { SettingsScreen { finish() } } }
    }
}

@Composable
fun SettingsScreen(alCerrar: () -> Unit) {
    val ctx = LocalContext.current

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
            colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF1EFFE))
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
                                Text("Guest", fontSize = 20.sp, color = if (Repo.modoOscuro) Color.White else Color.Black)
                                Spacer(modifier = Modifier.height(16.dp))

                                // Botón de conectar con Google
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF343434) else Color(0xFFEDE2FF)),
                                    modifier = Modifier.clickable {
                                        mostrarProximamente("No es posible ingresar con Google. Funcionalidad próxima")
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Text("Conectar con Google", fontSize = 14.sp, color = if (Repo.modoOscuro) Color.White else Color(0xFF3F5A8A))
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
                        border = BorderStroke(4.dp, if (Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF1EFFE)), // Borde del mismo color que el fondo principal
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
                Text("Colores de Interfaz", fontSize = 16.sp, color = if (Repo.modoOscuro) Color.White else Color.Black, modifier = Modifier.padding(bottom = 12.dp))
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
                    Text("Modo oscuro", fontSize = 16.sp, color = if (Repo.modoOscuro) Color.White else Color.Black, modifier = Modifier.padding(end = 16.dp))
                    Switch(
                        checked = Repo.modoOscuro,
                        onCheckedChange = { Repo.modoOscuro = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFFEDE2FF),
                            checkedTrackColor = Color(0xFF5A75A7),
                            uncheckedThumbColor = Color(0xFF5A75A7),
                            uncheckedTrackColor = Color(0xFFEDE2FF),
                            uncheckedBorderColor = Color.LightGray
                        )
                    )
                    Text(
                        text = if (Repo.modoOscuro) " sí" else " no",
                        color = if (Repo.modoOscuro) Color.White else Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // Barra de navegacion inferior moderna y funcional
        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            BottomNavBar(currentScreen = "SETTINGS")
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
            SettingsNavItemButton(
                icon = Icons.Default.Home,
                label = "HOME",
                isActive = currentScreen == "HOME",
                onClick = { navegarConTransicionSuave(ctx, MainMenuActivity::class.java) }
            )

            // 2. Calendar
            SettingsNavItemButton(
                icon = Icons.Default.DateRange,
                label = "CALEND",
                isActive = currentScreen == "CALENDARIO",
                onClick = { navegarConTransicionSuave(ctx, CalendarActivity::class.java) }
            )

            // 3. Settings / Config
            SettingsNavItemButton(
                icon = Icons.Default.Settings,
                label = "CONFIG",
                isActive = currentScreen == "SETTINGS",
                onClick = { navegarConTransicionSuave(ctx, SettingsActivity::class.java) }
            )
        }
    }
}

@Composable
private fun SettingsNavItemButton(
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
