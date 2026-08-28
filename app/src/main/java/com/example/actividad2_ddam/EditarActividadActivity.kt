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
