package com.example.actividad2_ddam.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.navigation.NavController
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.navigation.Routes
import com.example.actividad2_ddam.ui.components.BottomNavBar
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(navController: NavController) {
    var yearMonth by remember { mutableStateOf(YearMonth.now()) }

    val nombreMes = yearMonth.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")).uppercase()
    val totalDias = yearMonth.lengthOfMonth()

    val fondo = Brush.verticalGradient(listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94), Color(0xFF8BB5CE)))

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(fondo)) {
        val isWideScreen = maxWidth > 600.dp

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Card(
                    modifier = Modifier.width(220.dp).height(45.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF292929) else Color(0xFFEDE2FF))
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("$nombreMes ${yearMonth.year}", fontWeight = FontWeight.Bold, color = if (Repo.modoOscuro) Color.White else Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("L", "M", "M", "J", "V", "S", "D").forEach { dia ->
                        Card(
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF292929) else Color(0xFFEDE2FF))
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(dia, fontWeight = FontWeight.Bold, color = if (Repo.modoOscuro) Color.White else Color.Black)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(totalDias) { index ->
                        Card(
                            modifier = Modifier.height(if (isWideScreen) 100.dp else 80.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = if (Repo.modoOscuro) Color(0xFF292929) else Color(0xFFEDE2FF))
                        ) {
                            Box(Modifier.padding(4.dp)) {
                                Text((index + 1).toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (Repo.modoOscuro) Color.White else Color.Black)
                            }
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(onClick = { yearMonth = yearMonth.minusMonths(1) }) {
                        Icon(painterResource(android.R.drawable.ic_media_previous), contentDescription = "Mes anterior", tint = Color.White)
                    }
                    IconButton(onClick = { yearMonth = yearMonth.plusMonths(1) }) {
                        Icon(painterResource(android.R.drawable.ic_media_next), contentDescription = "Mes siguiente", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            BottomNavBar(navController = navController, currentScreen = Routes.CALENDAR)
        }
    }
}
