package com.example.actividad2_ddam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainstreamActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actividad2DDAMTheme(darkTheme = Repo.modoOscuro, dynamicColor = false) {
                MainstreamScreen {
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainstreamScreen(alCerrar: () -> Unit) {
    var tit by remember { mutableStateOf("") }
    var des by remember { mutableStateOf("") }

    var fechaSeleccionada by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    var horaSeleccionada by remember { mutableStateOf("09:30 am") }
    var repetirSeleccionados by remember { mutableStateOf(setOf<String>()) }

    var mostrarDatePicker by remember { mutableStateOf(false) }
    var mostrarTimePicker by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    val fondo = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))

    if (mostrarDatePicker) {
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        fechaSeleccionada = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                    }
                    mostrarDatePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (mostrarTimePicker) {
        AlertDialog(
            onDismissRequest = { mostrarTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val horaFormateada = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    horaSeleccionada = horaFormateada
                    mostrarTimePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarTimePicker = false }) { Text("Cancelar") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }

    Box(Modifier.fillMaxSize().background(fondo).padding(16.dp), Alignment.Center) {
        Card(
            Modifier.fillMaxWidth().fillMaxHeight(0.92f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF1EFFE))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Añadir actividad", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = if (Repo.modoOscuro) Color.White else Color(0xFF4A6DA7))

                // Banner de error
                if (mensajeError.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFF8B2323), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF4A4A4A) else Color(0xFFE2E2E2)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.Info, contentDescription = "Error", tint = Color(0xFFC62828), modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(10.dp))
                            Text(mensajeError, color = if (Repo.modoOscuro) Color.White else Color.Black, fontSize = 14.sp)
                        }
                    }
                }

                // Título
                Text("Ingresa un título *", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = if (Repo.modoOscuro) Color.White else Color.Black)
                OutlinedTextField(
                    value = tit,
                    onValueChange = {
                        tit = it
                        if (mensajeError.isNotEmpty()) mensajeError = ""
                    },
                    placeholder = { Text("Ej: Ir al gimnasio") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (Repo.modoOscuro) Color(0xFF343434) else Color(0xFFE8E6FF),
                        unfocusedContainerColor = if (Repo.modoOscuro) Color(0xFF343434) else Color(0xFFE8E6FF),
                        focusedTextColor = if (Repo.modoOscuro) Color.White else Color.Black,
                        unfocusedTextColor = if (Repo.modoOscuro) Color.White else Color.Black
                    )
                )

                // Descripción
                Text("Ingresa una descripción", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = if (Repo.modoOscuro) Color.White else Color.Black)
                OutlinedTextField(
                    value = des,
                    onValueChange = { des = it },
                    placeholder = { Text("Detalles adicionales aquí") },
                    modifier = Modifier.fillMaxWidth().height(90.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (Repo.modoOscuro) Color(0xFF343434) else Color(0xFFE8E6FF),
                        unfocusedContainerColor = if (Repo.modoOscuro) Color(0xFF343434) else Color(0xFFE8E6FF),
                        focusedTextColor = if (Repo.modoOscuro) Color.White else Color.Black,
                        unfocusedTextColor = if (Repo.modoOscuro) Color.White else Color.Black
                    )
                )

                // Hora
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Define una hora *:", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = if (Repo.modoOscuro) Color.White else Color.Black)
                    Button(
                        onClick = { mostrarTimePicker = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A85B0))
                    ) {
                        Text(text = if (horaSeleccionada.isEmpty()) "Seleccionar" else horaSeleccionada, color = Color.White)
                    }
                }

                // Fecha
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Define una fecha *:", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = if (Repo.modoOscuro) Color.White else Color.Black)
                    Button(
                        onClick = { mostrarDatePicker = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A85B0))
                    ) {
                        val textoFecha = fechaSeleccionada?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "Seleccionar"
                        Text(text = textoFecha, color = Color.White)
                    }
                }

                // Repetir
                Text("Repetir", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = if (Repo.modoOscuro) Color.White else Color.Black)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Cada", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom").forEach { opcion ->
                        val estaSeleccionado = repetirSeleccionados.contains(opcion)
                        Surface(
                            modifier = Modifier
                                .clickable {
                                    val nuevoSet = repetirSeleccionados.toMutableSet()
                                    if (estaSeleccionado) nuevoSet.remove(opcion) else nuevoSet.add(opcion)
                                    repetirSeleccionados = nuevoSet
                                },
                            shape = RoundedCornerShape(10.dp),
                            color = if (estaSeleccionado) Color(0xFF385A79) else Color(0xFF7A9BBF)
                        ) {
                            Text(
                                text = opcion,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Crear Actividad
                Button(
                    onClick = {
                        val hoy = LocalDate.now()
                        when {
                            tit.isBlank() -> mensajeError = "Por favor ingresa un título."
                            horaSeleccionada.isEmpty() -> mensajeError = "Por favor selecciona una hora."
                            fechaSeleccionada == null -> mensajeError = "Por favor selecciona una fecha."
                            fechaSeleccionada!!.isBefore(hoy) -> mensajeError = "La fecha no puede ser en el pasado."
                            else -> {
                                val repeticionFinal = if (repetirSeleccionados.isEmpty()) "No" else repetirSeleccionados.joinToString(", ")
                                try {
                                    val diasEspanolMap = mapOf(
                                        java.time.DayOfWeek.MONDAY to "Lun",
                                        java.time.DayOfWeek.TUESDAY to "Mar",
                                        java.time.DayOfWeek.WEDNESDAY to "Mie",
                                        java.time.DayOfWeek.THURSDAY to "Jue",
                                        java.time.DayOfWeek.FRIDAY to "Vie",
                                        java.time.DayOfWeek.SATURDAY to "Sab",
                                        java.time.DayOfWeek.SUNDAY to "Dom"
                                    )
                                    val diaDeLaSemana = diasEspanolMap[fechaSeleccionada!!.dayOfWeek] ?: "Lun"

                                    val nueva = Tarea(
                                        id = Repo.contadorId++,
                                        titulo = tit,
                                        desc = des.ifBlank { null },
                                        hora = horaSeleccionada,
                                        dia = diaDeLaSemana,
                                        repetir = repeticionFinal
                                    )
                                    Repo.tareas.add(nueva)
                                    alCerrar()
                                } catch (e: Exception) {
                                    mensajeError = "Ocurrió un error al guardar."
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B5E8C))
                ) {
                    Text("Crear Actividad", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }

        // Botón volver
        IconButton(
            onClick = { alCerrar() },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).background(Color(0xFF4A6DA7), CircleShape)
        ) {
            Icon(painterResource(id = android.R.drawable.ic_menu_revert), null, tint = Color.White)
        }
    }
}
