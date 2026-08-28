package com.example.actividad2_ddam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
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
import java.time.format.TextStyle
import java.util.Locale

class EditarActividadActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // CORRECCIÓN: Aquí recibimos el ID que enviaste desde la otra pantalla
        // (Asegúrate de enviar intent.putExtra("TAREA_ID", tarea.id) al abrir esta Activity)
        val tareaId = intent.getIntExtra("TAREA_ID", -1)
        val tareaAEditar = Repo.tareas.find { it.id == tareaId } ?: Repo.tareas.firstOrNull()

        setContent {
            Actividad2DDAMTheme {
                EditarScreen(tareaAEditar) { finish() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarScreen(tarea: Tarea?, alCerrar: () -> Unit) {
    // 1. Inicializamos los estados con la información de la tarea seleccionada
    var tit by remember { mutableStateOf(tarea?.titulo ?: "") }
    var des by remember { mutableStateOf(tarea?.desc ?: "") }
    var repetidor by remember { mutableStateOf(tarea?.repetir != "No" && tarea?.repetir?.isNotBlank() == true) }

    // Frecuencia y días
    var frecuencia by remember { mutableIntStateOf(1) }
    var repetirSeleccionados by remember {
        mutableStateOf(tarea?.repetir?.split(", ")?.filter { it != "No" }?.toSet() ?: setOf())
    }

    // Estados para fecha y hora (Usando la lógica de la pantalla anterior)
    var fechaSeleccionada by remember { mutableStateOf<LocalDate?>(LocalDate.now()) } // Idealmente parsear de 'tarea'
    var horaSeleccionada by remember { mutableStateOf(tarea?.hora ?: "") }

    // Controles de interfaz
    var mostrarDatePicker by remember { mutableStateOf(false) }
    var mostrarTimePicker by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    // --- DIÁLOGOS (Iguales a la pantalla de crear) ---
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
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (mostrarTimePicker) {
        AlertDialog(
            onDismissRequest = { mostrarTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    horaSeleccionada = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    mostrarTimePicker = false
                }) { Text("Aceptar") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }

    // --- INTERFAZ VISUAL ---
    val fondo = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))

    Box(Modifier.fillMaxSize().background(fondo).padding(16.dp), Alignment.Center) {
        Card(
            Modifier.fillMaxWidth().fillMaxHeight(0.95f), // Más alto para acomodar el error y las opciones
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFFE))
        ) {
            Column(Modifier.padding(24.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Editar Actividad", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5A75A7))

                // BANNER DE ERROR
                if (mensajeError.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFF8B2323), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE2E2E2))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Info, "Error", tint = Color.Red, modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(12.dp))
                            Text(mensajeError, color = Color.Black, fontSize = 14.sp)
                        }
                    }
                }

                // Campos de Texto
                Text("Título de la actividad:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                OutlinedTextField(
                    value = tit,
                    onValueChange = { tit = it; if(mensajeError.contains("título")) mensajeError = "" },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Gray, unfocusedBorderColor = Color.Gray),
                    trailingIcon = { Icon(painterResource(android.R.drawable.ic_menu_edit), null, tint = Color(0xFF5A75A7)) }
                )

                Text("Descripción de la actividad:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                OutlinedTextField(
                    value = des,
                    onValueChange = { des = it },
                    modifier = Modifier.fillMaxWidth().height(90.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Gray, unfocusedBorderColor = Color.Gray),
                    trailingIcon = { Icon(painterResource(android.R.drawable.ic_menu_edit), null, tint = Color(0xFF5A75A7)) }
                )

                // Selectores de Fecha y Hora (Simulando el diseño pero funcionales)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.clickable { mostrarTimePicker = true }) {
                        Text("Hora de la actividad:", fontSize = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            val partesHora = horaSeleccionada.split(":")
                            MiniBox(if(partesHora.isNotEmpty() && horaSeleccionada.isNotBlank()) partesHora[0] else "--")
                            MiniBox(if(partesHora.size > 1) partesHora[1].take(2) else "--")
                        }
                    }
                    Column(Modifier.clickable { mostrarDatePicker = true }) {
                        Text("Fecha de la actividad:", fontSize = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            val mesStr = fechaSeleccionada?.month?.getDisplayName(TextStyle.SHORT, Locale("es", "ES"))?.replaceFirstChar { it.uppercase() } ?: "Mes"
                            val diaStr = fechaSeleccionada?.dayOfMonth?.toString() ?: "--"
                            MiniBox(mesStr, width = 60)
                            MiniBox(diaStr)
                        }
                    }
                }

                // Switch de repetición
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Repetir: ", fontSize = 14.sp)
                    Switch(checked = repetidor, onCheckedChange = { repetidor = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF5A75A7)))
                    Text(if(repetidor) " sí" else " no", fontSize = 14.sp)
                }

                // Bloque de días de la semana (Se muestra solo si el switch está encendido)
                if (repetidor) {
                    Card(
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray, RoundedCornerShape(15.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Cada:", fontSize = 13.sp)
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("$frecuencia", color = Color(0xFF5A75A7), fontWeight = FontWeight.Bold)
                                    Text("Día(s)", fontSize = 13.sp)
                                    // Botones + y -
                                    Card(Modifier.height(30.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF8BB5CE))) {
                                        Row(Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Text("+", modifier = Modifier.clickable { frecuencia++ }.padding(4.dp), fontWeight = FontWeight.Bold)
                                            Text("  -", modifier = Modifier.clickable { if(frecuencia > 1) frecuencia-- }.padding(4.dp), fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }

                            Text("Días de la semana:", fontSize = 12.sp)
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom").forEach { dia ->
                                    val seleccionado = repetirSeleccionados.contains(dia)
                                    Card(
                                        shape = CircleShape,
                                        colors = CardDefaults.cardColors(if(seleccionado) Color(0xFF5A75A7) else Color.Transparent),
                                        border = if(!seleccionado) BorderStroke(1.dp, Color.Gray) else null,
                                        modifier = Modifier.size(35.dp).clickable {
                                            val nuevos = repetirSeleccionados.toMutableSet()
                                            if(seleccionado) nuevos.remove(dia) else nuevos.add(dia)
                                            repetirSeleccionados = nuevos
                                        }
                                    ) {
                                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                                            Text(dia, fontSize = 10.sp, color = if(seleccionado) Color.White else Color.Black)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // Botón Guardar con validaciones
                Button(
                    onClick = {
                        val hoy = LocalDate.now()
                        when {
                            tit.isBlank() -> mensajeError = "Por favor ingresa un título."
                            horaSeleccionada.isEmpty() -> mensajeError = "Por favor selecciona una hora."
                            fechaSeleccionada == null -> mensajeError = "Por favor selecciona una fecha."
                            fechaSeleccionada!!.isBefore(hoy) -> mensajeError = "La fecha no puede ser en el pasado."
                            else -> {
                                tarea?.let { tareaOriginal ->
                                    // Buscamos en qué posición de la lista está la tarea que editamos
                                    val index = Repo.tareas.indexOfFirst { it.id == tareaOriginal.id }
                                    if (index != -1) {
                                        // REEMPLAZAMOS el objeto completo en la lista.
                                        Repo.tareas[index] = Tarea(
                                            id = tareaOriginal.id, // Mantenemos el ID original
                                            titulo = tit,
                                            desc = des.ifBlank { null },
                                            hora = horaSeleccionada,
                                            dia = tareaOriginal.dia,
                                            repetir = if (repetidor && repetirSeleccionados.isNotEmpty()) repetirSeleccionados.joinToString(", ") else "No"
                                        )
                                    }
                                }
                                alCerrar()
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.8f).height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A75A7))
                ) {
                    Text("Guardar Cambios", color = Color.White, fontSize = 16.sp)
                }
            }
        }

        IconButton(
            onClick = { alCerrar() },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).size(50.dp).background(Color(0xFF4A6DA7), CircleShape)
        ) {
            Icon(painterResource(id = android.R.drawable.ic_menu_revert), null, tint = Color.White)
        }
    }
}

@Composable
fun MiniBox(txt: String, width: Int = 40) {
    Card(
        Modifier.size(width.dp, 35.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray),
        colors = CardDefaults.cardColors(Color.Transparent) // Ajustado al diseño (fondo claro)
    ) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text(txt, fontSize = 11.sp, color = Color.Black)
        }
    }
}

























/*
package com.example.actividad2_ddam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

// Actividad para modificar los datos de una tarea existente
class EditarActividadActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Carga de la tarea seleccionada (ejemplo con la primera de la lista)
        val tareaAEditar = Repo.tareas.firstOrNull() 
        setContent {
            Actividad2DDAMTheme {
                EditarScreen(tareaAEditar) { finish() }
            }
        }
    }
}

// Pantalla con el formulario de edicion y sus controles
@Composable
fun EditarScreen(tarea: Tarea?, alCerrar: () -> Unit) {
    // Estados sincronizados inicialmente con la tarea actual
    var tit by remember { mutableStateOf(tarea?.titulo ?: "") }
    var des by remember { mutableStateOf(tarea?.desc ?: "") }
    var repetidor by remember { mutableStateOf(false) }

    val fondo = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))

    Box(Modifier.fillMaxSize().background(fondo).padding(16.dp), Alignment.Center) {
        Card(
            Modifier.fillMaxWidth().fillMaxHeight(0.85f),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFFE))
        ) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Editar Actividad", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5A75A7))

                // Campo para actualizar el nombre de la actividad
                Text("Titulo de la actividad:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                OutlinedTextField(
                    value = tit,
                    onValueChange = { tit = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    placeholder = { Text("Titulo aqui") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Gray, unfocusedBorderColor = Color.Gray),
                    trailingIcon = { Icon(painterResource(android.R.drawable.ic_menu_edit), null, tint = Color(0xFFB4E6FF)) }
                )

                // Campo para actualizar la descripcion detallada
                Text("Descripcion de la actividad:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                OutlinedTextField(
                    value = des,
                    onValueChange = { des = it },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(15.dp),
                    placeholder = { Text("Vacio") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Gray, unfocusedBorderColor = Color.Gray),
                    trailingIcon = { Icon(painterResource(android.R.drawable.ic_menu_edit), null, tint = Color(0xFFB4E6FF)) }
                )

                // Seccion para configurar tiempo y fecha exacta
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Hora de la actividad:", fontSize = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            MiniBox("0")
                            MiniBox("0")
                            MiniBox("am")
                        }
                    }
                    Column {
                        Text("Fecha de la actividad:", fontSize = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            MiniBox("Enero", width = 60)
                            MiniBox("1")
                        }
                    }
                }

                // Control de activacion para repeticion automatica
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Repetir: ", fontSize = 14.sp)
                    Switch(checked = repetidor, onCheckedChange = { repetidor = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF5A75A7)))
                    Text(if(repetidor) " si" else " no", fontSize = 14.sp)
                }

                // Selector de frecuencia de repeticion personalizado
                Row(
                    Modifier.fillMaxWidth().height(45.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Cada", fontSize = 13.sp)
                    Text("1", color = Color(0xFF5A75A7), fontWeight = FontWeight.Bold)
                    Card(Modifier.size(60.dp, 30.dp), border = BorderStroke(1.dp, Color.Gray), colors = CardDefaults.cardColors(Color.Transparent)) {
                        Box(Modifier.fillMaxSize(), Alignment.Center) { Text("+  -", fontWeight = FontWeight.Bold) }
                    }
                    Text("Dia", fontSize = 13.sp)
                }

                Spacer(Modifier.weight(1f))

                // Boton para confirmar y aplicar los cambios en el repositorio
                Button(
                    onClick = {
                        tarea?.let { it.titulo = tit; it.desc = des }
                        alCerrar()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.8f).height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A75A7))
                ) {
                    Text("Guardar Cambios", color = Color.White, fontSize = 16.sp)
                }
            }
        }
        
        // Boton de salida sin guardar cambios
        IconButton(
            onClick = { alCerrar() },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).size(50.dp).background(Color(0xFF4A6DA7), CircleShape)
        ) {
            Icon(painterResource(id = android.R.drawable.ic_menu_revert), null, tint = Color.White)
        }
    }
}

// Pequeño contenedor estilizado para datos breves
@Composable
fun MiniBox(txt: String, width: Int = 40) {
    Card(
        Modifier.size(width.dp, 35.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text(txt, fontSize = 11.sp, color = Color.Black)
        }
    }
}

 */
