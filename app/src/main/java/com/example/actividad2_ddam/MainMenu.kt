package com.example.actividad2_ddam

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme

class MainMenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Actividad2DDAMTheme { MainMenuScreen() } }
    }
}

@Composable
fun MainMenuScreen() {
    val ctx = LocalContext.current
    val fondo = Brush.verticalGradient(listOf(Color(0xFF2E3B55), Color(0xFF6B93B5), Color(0xFF9ABED4)))

    Box(Modifier.fillMaxSize().background(fondo)) {
        Column(Modifier.padding(16.dp)) {
            Spacer(Modifier.height(48.dp))
            // Barra de dias
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom").forEach { dia ->
                    val esHoy = dia == "Vie"
                    Card(
                        Modifier.size(45.dp, 40.dp),
                        RoundedCornerShape(12.dp),
                        CardDefaults.cardColors(if(esHoy) Color(0xFF5A85B0) else Color(0xFF708090).copy(0.5f))
                    ) {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Text(dia, fontSize = 12.sp, color = if(esHoy) Color.White else Color.Black)
                        }
                    }
                }
            }
            // Lista de tareas
            LazyColumn(Modifier.weight(1f).padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(Repo.tareas) { tarea -> TareaCard(tarea) }
            }
            Spacer(Modifier.height(80.dp))
        }

        // Boton Flotante
        FloatingActionButton(
            onClick = { ctx.startActivity(Intent(ctx, MainstreamActivity::class.java)) },
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 24.dp, bottom = 100.dp).size(60.dp),
            containerColor = Color(0xFF90B4CE),
            shape = CircleShape
        ) { Icon(Icons.Default.Add, null, tint = Color.White) }

        // Barra inferior
        Surface(
            Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp).fillMaxWidth(0.9f).height(80.dp),
            shape = RoundedCornerShape(40.dp),
            color = Color(0xFFF0F4F7).copy(0.9f)
        ) {
            Row(Modifier.fillMaxSize(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, null, Modifier.size(32.dp))
                Box(Modifier.size(56.dp).background(Color(0xFF555555), CircleShape), Alignment.Center) {
                    Icon(Icons.Default.Home, null, tint = Color.White)
                }
                Icon(Icons.Default.Settings, null, Modifier.size(32.dp))
            }
        }
    }
}

@Composable
fun TareaCard(t: Tarea) {
    Card(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), CardDefaults.cardColors(Color(0xFFD9E3EA))) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Column {
                    Text(t.titulo, fontWeight = FontWeight.Bold)
                    Text(t.desc, fontSize = 12.sp, color = Color.DarkGray)
                }
                Text("A las: ${t.hora}", fontSize = 12.sp, color = Color(0xFF4A6B8A))
            }
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text("En: 60 min.", fontSize = 12.sp)
                Row {
                    Icon(Icons.Default.Alarm, null, Modifier.padding(8.dp), Color(0xFF4A6B8A))
                    Icon(Icons.Default.Edit, null, Modifier.padding(8.dp), Color(0xFF4A6B8A))
                }
            }
        }
    }
}
