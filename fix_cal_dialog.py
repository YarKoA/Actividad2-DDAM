import re
file_path = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\ui\screens\CalendarScreen.kt'

content = '''package com.example.actividad2_ddam.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.actividad2_ddam.navigation.Routes
import com.example.actividad2_ddam.ui.components.BottomNavBar
import com.example.actividad2_ddam.viewmodel.EventViewModel
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(navController: NavController, viewModel: EventViewModel = hiltViewModel()) {
    var yearMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDayAction by remember { mutableStateOf<Int?>(null) }
    
    val todasLasTareas by viewModel.todasLasTareas.collectAsState(initial = emptyList())
    val context = LocalContext.current

    val nombreMes = yearMonth.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")).uppercase()
    val fondo = Brush.verticalGradient(listOf(Color(0xFF33436F), Color(0xFF4B84A8), Color(0xFF8BB5CE)))

    Box(modifier = Modifier.fillMaxSize().background(fondo)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.Start
        ) {
            
            // TITULO ENERO
            Text(
                text = nombreMes,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // CONTENEDOR DEL CALENDARIO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF5E8AA8))
                    .padding(4.dp)
            ) {
                Column {
                    // DIAS DE LA SEMANA
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf("D", "L", "m", "m", "J", "V", "S").forEach { dia ->
                            Text(
                                text = dia,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    
                    HorizontalDivider(color = Color.White, thickness = 2.dp)
                    Spacer(modifier = Modifier.height(4.dp))

                    // CUADRICULA
                    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value
                    val startOffset = if (firstDayOfWeek == 7) 0 else firstDayOfWeek
                    val daysInMonth = yearMonth.lengthOfMonth()
                    
                    val prevMonth = yearMonth.minusMonths(1)
                    val daysInPrevMonth = prevMonth.lengthOfMonth()
                    
                    val totalCells = 42

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        userScrollEnabled = false,
                        modifier = Modifier.height(450.dp)
                    ) {
                        items(totalCells) { index ->
                            val isCurrentMonth = index in startOffset until (startOffset + daysInMonth)
                            val dayNumber = when {
                                index < startOffset -> daysInPrevMonth - startOffset + index + 1
                                isCurrentMonth -> index - startOffset + 1
                                else -> index - startOffset - daysInMonth + 1
                            }
                            
                            val bgColor = if (isCurrentMonth) Color(0xFFEFEAFA) else Color(0xFF5C88AA)
                            val txtColor = if (isCurrentMonth) Color.Black else Color.White
                            
                            Box(
                                modifier = Modifier
                                    .aspectRatio(0.75f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(bgColor)
                                    .clickable(enabled = isCurrentMonth) {
                                        selectedDayAction = dayNumber
                                    }
                                    .padding(4.dp)
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    color = txtColor,
                                    fontSize = 14.sp
                                )
                                
                                // EVENT BADGE 
                                if (isCurrentMonth) {
                                    val dateObj = yearMonth.atDay(dayNumber)
                                    val diaSemana = listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom")[dateObj.dayOfWeek.value - 1]
                                    val count = todasLasTareas.count { it.dia == diaSemana }
                                    
                                    if (count > 0) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .offset(x = 4.dp, y = (-4).dp)
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF3B5E8C)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(count.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            BottomNavBar(navController = navController, currentScreen = Routes.CALENDAR)
        }
        
        // DIALOGO MODIFICAR
        if (selectedDayAction != null) {
            Dialog(onDismissRequest = { selectedDayAction = null }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF3EDF7))
                        .padding(20.dp)
                ) {
                    // Close button
                    IconButton(
                        onClick = { selectedDayAction = null },
                        modifier = Modifier.align(Alignment.TopEnd).offset(x = 8.dp, y = (-8).dp)
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = Color(0xFF33436F))
                    }
                    
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Modificar:",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // MARCAR BUTTON
                        Button(
                            onClick = { 
                                selectedDayAction = null
                                Toast.makeText(context, "Día marcado", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(56.dp)
                                .shadow(8.dp, RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6B94))
                        ) {
                            Icon(Icons.Filled.BookmarkBorder, contentDescription = "Marcar", tint = Color.White)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("MARCAR", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // AÑADIR ACTIVIDAD BUTTON
                        Button(
                            onClick = { 
                                selectedDayAction = null
                                navController.navigate(Routes.EVENT_FORM)
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(56.dp)
                                .shadow(8.dp, RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6B94))
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Añadir", tint = Color.White)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("AÑADIR ACTIVIDAD", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
'''
with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
