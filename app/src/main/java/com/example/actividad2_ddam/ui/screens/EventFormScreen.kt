package com.example.actividad2_ddam.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.actividad2_ddam.R
import com.example.actividad2_ddam.model.Tarea
import com.example.actividad2_ddam.viewmodel.EventViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import com.example.actividad2_ddam.ui.theme.scaledSp
import com.example.actividad2_ddam.ui.theme.scaledWeight
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFormScreen(
    navController: NavController,
    viewModel: EventViewModel = hiltViewModel(),
    onCerrar: () -> Unit
) {
    var tit by remember { mutableStateOf("") }
    var des by remember { mutableStateOf("") }
    var horaSeleccionada by remember { mutableStateOf("") }
    var fechaSeleccionada by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    var repetirSeleccionados by remember { mutableStateOf(setOf<String>()) }
    var priorizar by remember { mutableStateOf(true) }
    var mensajeError by remember { mutableStateOf("") }

    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val fondo = Brush.verticalGradient(listOf(Color(0xFF33436F), Color(0xFF4B84A8), Color(0xFF8BB5CE)))

    Box(modifier = Modifier.fillMaxSize().background(fondo).padding(16.dp)) {

        // MAIN CARD
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(if (com.example.actividad2_ddam.model.Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF3EDF7))
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                stringResource(R.string.form_title),
                fontSize = 22.sp.scaledSp, fontWeight = FontWeight.Bold.scaledWeight, color = Color(0xFF4B6B94)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // TITLE
            Text(stringResource(R.string.form_enter_title), fontSize = 14.sp.scaledSp, color = Color.Black, fontWeight = FontWeight.Medium.scaledWeight)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = tit,
                onValueChange = { tit = it },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF90A5B8),
                    unfocusedContainerColor = Color(0xFF90A5B8),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // DESCRIPTION
            Text(stringResource(R.string.form_enter_desc), fontSize = 14.sp.scaledSp, color = Color.Black, fontWeight = FontWeight.Medium.scaledWeight)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = des,
                onValueChange = { des = it },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF90A5B8),
                    unfocusedContainerColor = Color(0xFF90A5B8),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // HORA - Real TimePicker
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.form_hour), fontSize = 14.sp.scaledSp, color = Color.Black, fontWeight = FontWeight.Medium.scaledWeight)
                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF4B6B94))
                        .clickable {
                            val now = java.util.Calendar.getInstance()
                            TimePickerDialog(
                                context,
                                { _, selectedHour, selectedMinute ->
                                    val amPm = if (selectedHour < 12) "am" else "pm"
                                    val hour12 = if (selectedHour == 0) 12 else if (selectedHour > 12) selectedHour - 12 else selectedHour
                                    val minStr = selectedMinute.toString().padStart(2, '0')
                                    horaSeleccionada = "$hour12:$minStr $amPm"
                                },
                                now.get(java.util.Calendar.HOUR_OF_DAY),
                                now.get(java.util.Calendar.MINUTE),
                                false
                            ).show()
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = horaSeleccionada.ifEmpty { stringResource(R.string.form_select_hour) },
                        color = Color.White,
                        fontSize = 14.sp.scaledSp,
                        fontWeight = FontWeight.Medium.scaledWeight
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // FECHA
            Text(stringResource(R.string.form_activity_date), fontSize = 14.sp.scaledSp, color = Color.Black, fontWeight = FontWeight.Medium.scaledWeight)
            Spacer(modifier = Modifier.height(16.dp))

            // MINI CALENDAR
            MiniCalendar(selectedDate = fechaSeleccionada, onDateSelected = { fechaSeleccionada = it })

            Spacer(modifier = Modifier.height(20.dp))

            // REPETIR
            Text(stringResource(R.string.form_repeat), fontSize = 14.sp.scaledSp, color = Color.Black, fontWeight = FontWeight.Medium.scaledWeight)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                val dias = listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom")
                dias.forEach { d ->
                    val isSelected = repetirSeleccionados.contains(d)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color.White else Color(0xFF4B6B94))
                            .border(1.dp, if (isSelected) Color.Black else Color.Transparent, RoundedCornerShape(12.dp))
                            .clickable {
                                repetirSeleccionados = if (isSelected) {
                                    repetirSeleccionados - d
                                } else {
                                    repetirSeleccionados + d
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(d, color = if (isSelected) Color.Black else Color.White, fontSize = 12.sp.scaledSp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // PRIORIZAR
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.form_prioritize), fontSize = 14.sp.scaledSp, color = Color.Black, fontWeight = FontWeight.Medium.scaledWeight)
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (priorizar) Color(0xFF4B6B94) else Color.Gray)
                        .clickable { priorizar = !priorizar }
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("SI", color = Color.White, fontSize = 12.sp.scaledSp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Filled.StarBorder, contentDescription = "Star", tint = Color(0xFF4B6B94))
            }

            Spacer(modifier = Modifier.height(30.dp))

            if (mensajeError.isNotEmpty()) {
                Text(mensajeError, color = Color.Red, fontSize = 14.sp.scaledSp, modifier = Modifier.padding(bottom = 8.dp))
            }

            // CREAR ACTIVIDAD BUTTON
            Button(
                onClick = {
                    when {
                        tit.isBlank() -> {
                            mensajeError = context.getString(R.string.error_title_required)
                        }
                        horaSeleccionada.isEmpty() -> {
                            mensajeError = context.getString(R.string.error_hour_required)
                        }
                        fechaSeleccionada == null -> {
                            mensajeError = context.getString(R.string.error_date_required)
                        }
                        fechaSeleccionada!!.isBefore(LocalDate.now()) -> {
                            mensajeError = context.getString(R.string.error_date_past)
                        }
                        else -> {
                            val diaSemana = fechaSeleccionada?.let {
                                listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom")[it.dayOfWeek.value - 1]
                            } ?: "Lun"
                            val repStr = if (repetirSeleccionados.isEmpty()) "No" else repetirSeleccionados.joinToString(", ")
                            val fechaStr = fechaSeleccionada?.toString() ?: ""
                            val t = Tarea(
                                titulo = tit,
                                desc = des.ifBlank { null },
                                hora = horaSeleccionada,
                                dia = diaSemana,
                                fecha = fechaStr,
                                repetir = repStr,
                                esAnclada = priorizar
                            )
                            viewModel.addEvent(t)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onCerrar()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B6B94))
            ) {
                Text(stringResource(R.string.form_create_button), color = Color.White, fontSize = 16.sp.scaledSp, fontWeight = FontWeight.Bold.scaledWeight)
            }
        }

        // BACK BUTTON
        IconButton(
            onClick = { onCerrar() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = 20.dp, x = 10.dp)
                .background(Color(0xFF4A6DA7), CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
        }
    }
}

@Composable
fun MiniCalendar(selectedDate: LocalDate?, onDateSelected: (LocalDate) -> Unit) {
    val yearMonth = YearMonth.now()
    val nombreMes = yearMonth.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")).replaceFirstChar { it.uppercase() }

    Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Column {
            // TABS (Bubbles)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom").forEach { dia ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(Color(0xFF4B6B94))
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(dia, color = Color.White, fontSize = 12.sp.scaledSp)
                    }
                }
            }

            // GRID CONTAINER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-2).dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF8BB5CE))
                    .padding(4.dp)
            ) {
                val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value
                val startOffset = firstDayOfWeek - 1
                val daysInMonth = yearMonth.lengthOfMonth()
                val totalCells = 35

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    userScrollEnabled = false,
                    modifier = Modifier.height(180.dp)
                ) {
                    items(totalCells) { index ->
                        val isCurrentMonth = index in startOffset until (startOffset + daysInMonth)
                        val dayNumber = if (isCurrentMonth) index - startOffset + 1 else -1

                        val isSelected = isCurrentMonth && selectedDate?.dayOfMonth == dayNumber && selectedDate?.year == yearMonth.year && selectedDate?.month == yearMonth.month

                        val bgColor = if (isSelected) Color(0xFF4B6B94) else if (isCurrentMonth) if (com.example.actividad2_ddam.model.Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF3EDF7) else Color(0xFF90A5B8)
                        val txtColor = if (isSelected) Color.White else Color.Black

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(bgColor)
                                .clickable(enabled = isCurrentMonth) {
                                    if (isCurrentMonth) {
                                        onDateSelected(yearMonth.atDay(dayNumber))
                                    }
                                }
                                .padding(4.dp)
                        ) {
                            if (dayNumber > 0) {
                                Text(dayNumber.toString(), color = txtColor, fontSize = 12.sp.scaledSp)
                            }
                        }
                    }
                }
            }
        }

        // MONTH BADGE
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = 12.dp, x = (-16).dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF4B6B94))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(nombreMes, color = Color.White, fontSize = 12.sp.scaledSp, fontWeight = FontWeight.Bold.scaledWeight)
        }
    }
}