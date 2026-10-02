package com.example.actividad2_ddam.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.actividad2_ddam.viewmodel.SettingsViewModel
import androidx.navigation.NavController
import com.example.actividad2_ddam.R
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.navigation.Routes
import com.example.actividad2_ddam.ui.components.BottomNavBar
import com.example.actividad2_ddam.ui.theme.scaledSp
import com.example.actividad2_ddam.ui.theme.scaledWeight
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun SettingsScreen(
    navController: NavController,
    onCerrarSesion: () -> Unit = {},
    vm: SettingsViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    fun mostrarProximamente(mensaje: String = ctx.getString(R.string.settings_coming_soon)) {
        Toast.makeText(ctx, mensaje, Toast.LENGTH_SHORT).show()
    }

    val fondoGeneral = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))

    Box(Modifier.fillMaxSize().background(fondoGeneral)) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier.widthIn(max = 700.dp).fillMaxWidth().fillMaxHeight(0.85f),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF1EFFE))
            ) {
                Column(Modifier.padding(24.dp).fillMaxSize()) {

                    Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 24.dp)) {
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
                                    Text(Repo.usuarioActual?.nombre ?: "Guest", fontSize = 20.sp.scaledSp, color = if (Repo.modoOscuro) Color.White else Color.Black)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Card(
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF343434) else Color(0xFFEDE2FF)),
                                        modifier = Modifier.clickable {
                                            mostrarProximamente(ctx.getString(R.string.settings_google_soon))
                                        }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            Text(stringResource(R.string.settings_connect_google), fontSize = 14.sp.scaledSp, color = if (Repo.modoOscuro) Color.White else Color(0xFF3F5A8A))
                                            Text(text = "G", color = Color(0xFF4285F4), fontSize = 22.sp.scaledSp, fontWeight = FontWeight.Bold.scaledWeight)
                                        }
                                    }
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.size(90.dp).align(Alignment.TopEnd).padding(end = 24.dp),
                            shape = CircleShape,
                            border = BorderStroke(4.dp, if (Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF1EFFE)),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().background(
                                    Brush.verticalGradient(listOf(Color(0xFF8BB5CE), Color(0xFF5A75A7)))
                                )
                            )
                        }
                    }

                    Text(stringResource(R.string.settings_colors), fontSize = 16.sp.scaledSp, color = if (Repo.modoOscuro) Color.White else Color.Black, modifier = Modifier.padding(bottom = 12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val colores = listOf(Color(0xFF3B4874), Color(0xFF4A7D9D), Color(0xFF9AB4C9))
                        colores.forEach { color ->
                            Card(
                                modifier = Modifier.size(80.dp).clickable { mostrarProximamente() },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = color),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {}
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(R.string.settings_dark_mode), fontSize = 16.sp.scaledSp, color = if (Repo.modoOscuro) Color.White else Color.Black, modifier = Modifier.weight(1f))
                            Switch(
                                checked = Repo.modoOscuro,
                                onCheckedChange = { Repo.modoOscuro = it; vm.setModoOscuro(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFFEDE2FF), checkedTrackColor = Color(0xFF5A75A7),
                                    uncheckedThumbColor = Color(0xFF5A75A7), uncheckedTrackColor = Color(0xFFEDE2FF), uncheckedBorderColor = Color.LightGray
                                )
                            )
                            Text(
                                text = if (Repo.modoOscuro) stringResource(R.string.settings_yes) else stringResource(R.string.settings_no),
                                color = if (Repo.modoOscuro) Color.White else Color.Black,
                                fontSize = 16.sp.scaledSp, modifier = Modifier.width(85.dp).padding(start = 8.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(R.string.settings_typography), fontSize = 16.sp.scaledSp, color = if (Repo.modoOscuro) Color.White else Color.Black, modifier = Modifier.weight(1f))
                            Switch(
                                checked = Repo.letraGrande,
                                onCheckedChange = { Repo.letraGrande = it; vm.setLetraGrande(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFFEDE2FF), checkedTrackColor = Color(0xFF5A75A7),
                                    uncheckedThumbColor = Color(0xFF5A75A7), uncheckedTrackColor = Color(0xFFEDE2FF), uncheckedBorderColor = Color.LightGray
                                )
                            )
                            Text(
                                text = if (Repo.letraGrande) stringResource(R.string.settings_large) else stringResource(R.string.settings_small),
                                color = if (Repo.modoOscuro) Color.White else Color.Black,
                                fontSize = 16.sp.scaledSp, modifier = Modifier.width(85.dp).padding(start = 8.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(R.string.settings_font_weight), fontSize = 16.sp.scaledSp, color = if (Repo.modoOscuro) Color.White else Color.Black, modifier = Modifier.weight(1f))
                            Switch(
                                checked = Repo.grosorGrueso,
                                onCheckedChange = { Repo.grosorGrueso = it; vm.setGrosorGrueso(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFFEDE2FF), checkedTrackColor = Color(0xFF5A75A7),
                                    uncheckedThumbColor = Color(0xFF5A75A7), uncheckedTrackColor = Color(0xFFEDE2FF), uncheckedBorderColor = Color.LightGray
                                )
                            )
                            Text(
                                text = if (Repo.grosorGrueso) stringResource(R.string.settings_bold) else stringResource(R.string.settings_thin),
                                color = if (Repo.modoOscuro) Color.White else Color.Black,
                                fontSize = 16.sp.scaledSp, modifier = Modifier.width(85.dp).padding(start = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { 
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showLogoutDialog = true 
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.settings_logout), color = Color.White, fontWeight = FontWeight.Bold.scaledWeight)
                    }

                    if (showLogoutDialog) {
                        AlertDialog(
                            onDismissRequest = { showLogoutDialog = false },
                            title = { Text(stringResource(R.string.settings_logout)) },
                            text = { Text(stringResource(R.string.settings_logout_confirm)) },
                            confirmButton = {
                                TextButton(onClick = {
                                    showLogoutDialog = false
                                    onCerrarSesion()
                                }) {
                                    Text(stringResource(R.string.settings_logout_exit), color = Color.Red)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showLogoutDialog = false }) {
                                    Text(stringResource(R.string.settings_cancel))
                                }
                            }
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            BottomNavBar(navController = navController, currentScreen = Routes.SETTINGS)
        }
    }
}
