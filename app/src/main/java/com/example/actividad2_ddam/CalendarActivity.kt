package com.example.actividad2_ddam

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme

// Actividad para visualizar el calendario mensual de tareas
class CalendarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actividad2DDAMTheme {
                CalendarScreen()
            }
        }
    }
}

// Diseño de la vista mensual del calendario
@Composable
fun CalendarScreen() {
    val ctx = LocalContext.current
    val fondo = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))

    Box(modifier = Modifier.fillMaxSize().background(fondo)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Encabezado del mes actual
            Card(
                modifier = Modifier.width(200.dp).height(45.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("ENERO", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Indicadores de los dias de la semana
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("L", "M", "M", "J", "V", "S", "D").forEach { dia ->
                    Card(
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(dia, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cuadricula para representar los dias del mes
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(30) { index ->
                    Card(
                        modifier = Modifier.height(80.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                    ) {
                        Box(Modifier.padding(4.dp)) {
                            Text((index + 1).toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }

            // Controles de navegacion de meses
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {}) { Icon(painterResource(android.R.drawable.ic_media_previous), null, tint = Color.White) }
                IconButton(onClick = {}) { Icon(painterResource(android.R.drawable.ic_media_next), null, tint = Color.White) }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        // Sistema de navegacion inferior con redireccion al menu principal
        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.barracalendar),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Row(
                modifier = Modifier.fillMaxWidth().height(75.dp).padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Box(Modifier.size(55.dp).clickable { })
                Spacer(modifier = Modifier.width(10.dp))
                Box(Modifier.size(55.dp).clickable { })
                Spacer(modifier = Modifier.width(10.dp))
                // Enlace de retorno al listado de tareas
                Box(Modifier.size(65.dp).clickable {
                    ctx.startActivity(Intent(ctx, MainMenuActivity::class.java))
                })
            }
        }
    }
}
