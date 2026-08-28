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
