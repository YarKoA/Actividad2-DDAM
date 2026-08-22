package com.example.actividad2_ddam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme

class MainstreamActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Actividad2DDAMTheme { MainstreamScreen { finish() } } }
    }
}

@Composable
fun MainstreamScreen(alCerrar: () -> Unit) {
    var tit by remember { mutableStateOf("") }
    var des by remember { mutableStateOf("") }
    val fondo = Brush.verticalGradient(listOf(Color(0xFF2E3B55), Color(0xFF6B93B5), Color(0xFF9ABED4)))

    Box(Modifier.fillMaxSize().background(fondo).padding(16.dp), Alignment.Center) {
        Card(Modifier.fillMaxWidth(), RoundedCornerShape(24.dp), CardDefaults.cardColors(Color(0xFFDCE7F0))) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Añadir actividad", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C567A))
                
                CampoTexto("Ingresa un título *", tit) { tit = it }
                CampoTexto("Ingresa una descripción", des) { des = it }

                Button(
                    onClick = {
                        Repo.tareas.add(Tarea(if(tit.isEmpty()) "Sin título" else tit, des, "9:30 am", "Vie"))
                        alCerrar()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A93BE))
                ) {
                    Text("Crear Actividad", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CampoTexto(label: String, valor: String, cambio: (String) -> Unit) {
    Column {
        Text(label, fontSize = 13.sp)
        OutlinedTextField(
            value = valor,
            onValueChange = cambio,
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFB4C8D8),
                unfocusedContainerColor = Color(0xFFB4C8D8),
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}
