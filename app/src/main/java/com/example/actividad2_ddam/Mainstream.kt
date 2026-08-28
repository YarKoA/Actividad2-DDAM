package com.example.actividad2_ddam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
        setContent { Actividad2DDAMTheme { MainstreamScreen { finish() } } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainstreamScreen(alCerrar: () -> Unit) {
    // Estados de los campos
    var tit by remember { mutableStateOf("") }
    var des by remember { mutableStateOf("") }

    // Estados para fecha y hora
    var fechaSeleccionada by remember { mutableStateOf<LocalDate?>(null) }
    var horaSeleccionada by remember { mutableStateOf("") } // Guardaremos la hora como String
    var repetirSeleccionados by remember { mutableStateOf(setOf<String>()) }

    // Estados para controlar los diálogos de fecha y hora
    var mostrarDatePicker by remember { mutableStateOf(false) }
    var mostrarTimePicker by remember { mutableStateOf(false) }

    // Estado para el mensaje de error
    var mensajeError by remember { mutableStateOf("") }

    val fondo = Brush.verticalGradient(listOf(Color(0xFF2E3B55), Color(0xFF6B93B5), Color(0xFF9ABED4)))

    // Configuración del DatePicker
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

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
            Modifier.fillMaxWidth().fillMaxHeight(0.90f), // Ligeramente más grande para acomodar el error
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F0FF))
        ) {
            Column(Modifier.padding(20.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Añadir actividad", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A6DA7))

                // --- BANNER DE ERROR TIPO "SCREENSHOT" ---
                if (mensajeError.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFF8B2323), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE2E2E2)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.Info, contentDescription = "Error", tint = Color.Red, modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(12.dp))
                            Text(mensajeError, color = Color.Black, fontSize = 16.sp)
                        }
                    }
                }

                // Entrada del titulo
                Text("Ingresa un titulo *", fontSize = 13.sp)
                OutlinedTextField(
                    value = tit,
                    onValueChange = {
                        tit = it
                        // Limpiar error al escribir
                        if(mensajeError.contains("título")) mensajeError = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFD9D9E9))
                )

                // Entrada de descripcion
                Text("Ingresa una descripcion", fontSize = 13.sp)
                OutlinedTextField(
                    value = des,
                    onValueChange = { des = it },
                    placeholder = { Text("Detalles aqui") },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFD9D9E9))
                )

                // Selección de horario
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Define una hora *:", fontSize = 13.sp)
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF5A85B0)),
                        modifier = Modifier.clickable { mostrarTimePicker = true }
                    ) {
                        Text(
                            text = if (horaSeleccionada.isEmpty()) "Seleccionar" else horaSeleccionada,
                            color = Color.White,
                            modifier = Modifier.padding(12.dp, 8.dp)
                        )
                    }
                }

                // Selección de fecha real
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Define una fecha *:", fontSize = 13.sp)
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF5A85B0)),
                        modifier = Modifier.clickable { mostrarDatePicker = true }
                    ) {
                        val textoFecha = fechaSeleccionada?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "Seleccionar"
                        Text(
                            text = textoFecha,
                            color = Color.White,
                            modifier = Modifier.padding(12.dp, 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f)) // Empuja el botón al fondo

                // Opciones de repeticion de la tarea
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Repetir", modifier = Modifier.weight(1f), fontSize = 13.sp)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("Cada", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom").forEach { opcion ->
                            val estaSeleccionado = repetirSeleccionados.contains(opcion)

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    // Cambia el color si está seleccionado
                                    containerColor = if (estaSeleccionado) Color(0xFF4A6DA7) else Color(0xFF5A85B0)
                                ),
                                modifier = Modifier.clickable {
                                    // Lógica para añadir o quitar de la lista
                                    val nuevoSet = repetirSeleccionados.toMutableSet()
                                    if (estaSeleccionado) nuevoSet.remove(opcion) else nuevoSet.add(opcion)
                                    repetirSeleccionados = nuevoSet
                                }
                            ) {
                                Text(
                                    text = opcion,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                // Botón para procesar el registro con Validaciones
                Button(
                    onClick = {
                        val hoy = LocalDate.now()

                        // Sistema de validaciones
                        when {
                            tit.isBlank() -> mensajeError = "Por favor ingresa un título."
                            horaSeleccionada.isEmpty() -> mensajeError = "Por favor selecciona una hora."
                            fechaSeleccionada == null -> mensajeError = "Por favor selecciona una fecha."
                            fechaSeleccionada!!.isBefore(hoy) -> mensajeError = "La fecha no puede ser en el pasado."
                            else -> {
                                // Convertimos los días elegidos en un solo String separado por comas
                                val repeticionFinal = if (repetirSeleccionados.isEmpty()) "No" else repetirSeleccionados.joinToString(", ")
                                try {
                                    val diaDeLaSemana = fechaSeleccionada!!.dayOfWeek.name.take(3)

                                    // Usamos parámetros nombrados para ser explícitos con tu clase Tarea
                                    val nueva = Tarea(
                                        id = Repo.contadorId++,
                                        titulo = tit,
                                        desc = des.ifBlank { null }, // Si la descripción quedó en blanco, enviamos null
                                        hora = horaSeleccionada,
                                        dia = diaDeLaSemana,
                                        repetir = repeticionFinal // Inyectamos: "Lun, Vie" o "No"
                                    )
                                    Repo.tareas.add(nueva)
                                    alCerrar()
                                } catch (e: Exception) {
                                    mensajeError = "Ocurrió un error al guardar."
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A93BE))
                ) {
                    Text("Crear Actividad", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Boton para volver a la pantalla anterior
        IconButton(
            onClick = { alCerrar() },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).background(Color(0xFF4A6DA7), CircleShape)
        ) {
            Icon(painterResource(id = android.R.drawable.ic_menu_revert), null, tint = Color.White)
        }
    }
}










/*
package com.example.actividad2_ddam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

// Actividad para la creacion de nuevas actividades
class MainstreamActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Actividad2DDAMTheme { MainstreamScreen { finish() } } }
    }
}

// Interfaz del formulario para añadir tareas
@Composable
fun MainstreamScreen(alCerrar: () -> Unit) {
    // Variables de estado para capturar la entrada del usuario
    var tit by remember { mutableStateOf("") }
    var des by remember { mutableStateOf("") }
    
    val fondo = Brush.verticalGradient(listOf(Color(0xFF2E3B55), Color(0xFF6B93B5), Color(0xFF9ABED4)))

    Box(Modifier.fillMaxSize().background(fondo).padding(16.dp), Alignment.Center) {
        Card(
            Modifier.fillMaxWidth().fillMaxHeight(0.85f), 
            shape = RoundedCornerShape(24.dp), 
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F0FF))
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Añadir actividad", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A6DA7))
                
                // Entrada del titulo obligatorio
                Text("Ingresa un titulo *", fontSize = 13.sp)
                OutlinedTextField(
                    value = tit,
                    onValueChange = { tit = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFD9D9E9))
                )

                // Entrada de descripcion opcional
                Text("Ingresa una descripcion", fontSize = 13.sp)
                OutlinedTextField(
                    value = des,
                    onValueChange = { des = it },
                    placeholder = { Text("Detalles aqui") },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFD9D9E9))
                )

                // Seleccion de horario simplificada
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Define una hora:", fontSize = 13.sp)
                    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF5A85B0))) {
                        Text("9:30 am", color = Color.White, modifier = Modifier.padding(8.dp))
                    }
                }

                // Grid de dias para seleccionar la fecha
                Text("Define una fecha:", fontSize = 13.sp)
                Card(modifier = Modifier.fillMaxWidth().height(140.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFAAB8CF))) {
                    Column(Modifier.padding(8.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            listOf("Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab").forEach {
                                Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Color(0xFF5A85B0)), modifier = Modifier.size(35.dp)) {
                                    Box(Modifier.fillMaxSize(), Alignment.Center) { Text(it, fontSize = 10.sp, color = Color.White) }
                                }
                            }
                        }
                    }
                }

                // Opciones de repeticion de la tarea
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Repetir", modifier = Modifier.weight(1f))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("Cada", "Lun", "Mie", "Vie").forEach {
                            Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF5A85B0))) {
                                Text(it, color = Color.White, modifier = Modifier.padding(4.dp), fontSize = 10.sp)
                            }
                        }
                    }
                }

                // Boton para procesar el registro de la actividad
                Button(
                    onClick = {
                        try {
                            if (tit.isBlank()) throw Exception("Datos faltantes")
                            // Creacion de la tarea y guardado en la lista global
                            val nueva = Tarea(Repo.contadorId++, tit, des, "9:30 am", "Vie")
                            Repo.tareas.add(nueva)
                            alCerrar()
                        } catch (e: Exception) { }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A93BE))
                ) {
                    Text("Crear Actividad", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        // Boton para volver a la pantalla anterior
        IconButton(
            onClick = { alCerrar() },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).background(Color(0xFF4A6DA7), CircleShape)
        ) {
            Icon(painterResource(id = android.R.drawable.ic_menu_revert), null, tint = Color.White)
        }
    }
}
*/
