package com.example.actividad2_ddam.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.model.Tarea
import com.example.actividad2_ddam.viewmodel.EventViewModel
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEditScreen(
    eventId: Int,
    viewModel: EventViewModel = hiltViewModel(),
    modoOverlay: Boolean = false,
    onCerrar: () -> Unit
) {

    // Buscar actividad
    val tareaOpt =
        remember(eventId) {
            viewModel.getEventById(
                eventId
            )
        }

    // Si no existe, cerrar
    if (tareaOpt == null) {

        LaunchedEffect(Unit) {
            onCerrar()
        }

        return
    }

    tareaOpt.let { tarea ->

        var tit by remember {
            mutableStateOf(
                tarea.titulo
            )
        }

        var des by remember {
            mutableStateOf(
                tarea.desc ?: ""
            )
        }

        var fechaSeleccionada by remember {
            mutableStateOf<LocalDate?>(
                LocalDate.now()
            )
        }

        var horaSeleccionada by remember {
            mutableStateOf(
                tarea.hora
            )
        }

        var repetirSeleccionados
                by remember {

                    val elementos =
                        if (
                            tarea.repetir == "No"
                        ) {

                            emptySet()

                        } else {

                            tarea.repetir
                                .split(", ")
                                .toSet()
                        }

                    mutableStateOf(
                        elementos
                    )
                }

        var mostrarDatePicker
                by remember {
                    mutableStateOf(false)
                }

        var mostrarTimePicker
                by remember {
                    mutableStateOf(false)
                }

        var mensajeError
                by remember {
                    mutableStateOf("")
                }

        val datePickerState =
            rememberDatePickerState()

        val timePickerState =
            rememberTimePickerState()

        val fondo =
            Brush.verticalGradient(
                listOf(
                    Color(
                        0xFF2C3E6B
                    ),
                    Color(
                        0xFF4B6B94
                    ),
                    Color(
                        0xFF8BB5CE
                    )
                )
            )

        // Si está como overlay,
        // el fondo es transparente
        val fondoPantalla =
            if (modoOverlay) {

                Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        Color.Transparent
                    )
                )

            } else {

                fondo
            }

        // -------------------------
        // SELECTOR DE FECHA
        // -------------------------

        if (mostrarDatePicker) {

            DatePickerDialog(
                onDismissRequest = {

                    mostrarDatePicker =
                        false
                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            datePickerState
                                .selectedDateMillis
                                ?.let { millis ->

                                    fechaSeleccionada =
                                        Instant
                                            .ofEpochMilli(
                                                millis
                                            )
                                            .atZone(
                                                ZoneId
                                                    .systemDefault()
                                            )
                                            .toLocalDate()
                                }

                            mostrarDatePicker =
                                false
                        }
                    ) {

                        Text(
                            "Aceptar"
                        )
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {

                            mostrarDatePicker =
                                false
                        }
                    ) {

                        Text(
                            "Cancelar"
                        )
                    }
                }
            ) {

                DatePicker(
                    state =
                        datePickerState
                )
            }
        }

        // -------------------------
        // SELECTOR DE HORA
        // -------------------------

        if (mostrarTimePicker) {

            AlertDialog(
                onDismissRequest = {

                    mostrarTimePicker =
                        false
                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            val horaFormateada =
                                String.format(
                                    java.util
                                        .Locale
                                        .getDefault(),

                                    "%02d:%02d",

                                    timePickerState
                                        .hour,

                                    timePickerState
                                        .minute
                                )

                            horaSeleccionada =
                                horaFormateada

                            mostrarTimePicker =
                                false
                        }
                    ) {

                        Text(
                            "Aceptar"
                        )
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {

                            mostrarTimePicker =
                                false
                        }
                    ) {

                        Text(
                            "Cancelar"
                        )
                    }
                },

                text = {

                    TimePicker(
                        state =
                            timePickerState
                    )
                }
            )
        }

        // -------------------------
        // PANTALLA / OVERLAY
        // -------------------------

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    fondoPantalla
                ),

            contentAlignment =
                if (modoOverlay) {

                    Alignment.CenterStart

                } else {

                    Alignment.Center
                }
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start =
                            if (
                                modoOverlay
                            ) {
                                12.dp
                            } else {
                                16.dp
                            },

                        end =
                            if (
                                modoOverlay
                            ) {
                                55.dp
                            } else {
                                16.dp
                            },

                        top = 16.dp,
                        bottom = 16.dp
                    ),

                contentAlignment =
                    if (modoOverlay) {

                        Alignment.CenterStart

                    } else {

                        Alignment.Center
                    }
            ) {

                Card(
                    modifier = Modifier
                        .widthIn(
                            max = 600.dp
                        )
                        .fillMaxWidth()
                        .fillMaxHeight(
                            if (modoOverlay) {
                                0.88f
                            } else {
                                0.92f
                            }
                        ),

                    shape =
                        RoundedCornerShape(
                            topStart =
                                32.dp,

                            topEnd =
                                32.dp,

                            bottomStart =
                                24.dp,

                            bottomEnd =
                                24.dp
                        ),

                    colors =
                        CardDefaults
                            .cardColors(
                                containerColor =
                                    if (
                                        Repo.modoOscuro
                                    ) {

                                        Color(
                                            0xFF202020
                                        )

                                    } else {

                                        Color(
                                            0xFFF1EFFE
                                        )
                                    }
                            ),

                    elevation =
                        CardDefaults
                            .cardElevation(
                                10.dp
                            )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(
                                rememberScrollState()
                            )
                            .padding(
                                24.dp
                            ),

                        verticalArrangement =
                            Arrangement
                                .spacedBy(
                                    14.dp
                                )
                    ) {

                        // -------------------------
                        // TÍTULO
                        // -------------------------

                        Text(
                            text =
                                "Editar Actividad",

                            fontSize = 22.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                if (
                                    Repo.modoOscuro
                                ) {

                                    Color.White

                                } else {

                                    Color(
                                        0xFF4A6DA7
                                    )
                                }
                        )

                        // -------------------------
                        // ERROR
                        // -------------------------

                        if (
                            mensajeError
                                .isNotEmpty()
                        ) {

                            Card(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .border(
                                            1.dp,
                                            Color(
                                                0xFF8B2323
                                            ),
                                            RoundedCornerShape(
                                                12.dp
                                            )
                                        ),

                                colors =
                                    CardDefaults
                                        .cardColors(
                                            containerColor =
                                                if (
                                                    Repo.modoOscuro
                                                ) {

                                                    Color(
                                                        0xFF4A4A4A
                                                    )

                                                } else {

                                                    Color(
                                                        0xFFE2E2E2
                                                    )
                                                }
                                        )
                            ) {

                                Row(
                                    modifier =
                                        Modifier
                                            .padding(
                                                horizontal =
                                                    16.dp,

                                                vertical =
                                                    12.dp
                                            ),

                                    verticalAlignment =
                                        Alignment
                                            .CenterVertically
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons
                                                .Outlined
                                                .Info,

                                        contentDescription =
                                            "Error",

                                        tint =
                                            Color(
                                                0xFFC62828
                                            ),

                                        modifier =
                                            Modifier
                                                .size(
                                                    22.dp
                                                )
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier
                                                .width(
                                                    10.dp
                                                )
                                    )

                                    Text(
                                        text =
                                            mensajeError,

                                        color =
                                            if (
                                                Repo.modoOscuro
                                            ) {
                                                Color.White
                                            } else {
                                                Color.Black
                                            },

                                        fontSize =
                                            14.sp
                                    )
                                }
                            }
                        }

                        // -------------------------
                        // TÍTULO ACTIVIDAD
                        // -------------------------

                        Text(
                            text =
                                "Título de la actividad *",

                            fontSize =
                                14.sp,

                            fontWeight =
                                FontWeight
                                    .SemiBold,

                            color =
                                if (
                                    Repo.modoOscuro
                                ) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                        )

                        OutlinedTextField(
                            value = tit,

                            onValueChange = {

                                tit = it

                                mensajeError =
                                    ""
                            },

                            modifier =
                                Modifier
                                    .fillMaxWidth(),

                            shape =
                                RoundedCornerShape(
                                    12.dp
                                ),

                            colors =
                                OutlinedTextFieldDefaults
                                    .colors(

                                        focusedContainerColor =
                                            if (
                                                Repo.modoOscuro
                                            ) {

                                                Color(
                                                    0xFF343434
                                                )

                                            } else {

                                                Color(
                                                    0xFFE8E6FF
                                                )
                                            },

                                        unfocusedContainerColor =
                                            if (
                                                Repo.modoOscuro
                                            ) {

                                                Color(
                                                    0xFF343434
                                                )

                                            } else {

                                                Color(
                                                    0xFFE8E6FF
                                                )
                                            },

                                        focusedTextColor =
                                            if (
                                                Repo.modoOscuro
                                            ) {
                                                Color.White
                                            } else {
                                                Color.Black
                                            },

                                        unfocusedTextColor =
                                            if (
                                                Repo.modoOscuro
                                            ) {
                                                Color.White
                                            } else {
                                                Color.Black
                                            }
                                    )
                        )

                        // -------------------------
                        // DESCRIPCIÓN
                        // -------------------------

                        Text(
                            text =
                                "Descripción de la actividad",

                            fontSize =
                                14.sp,

                            fontWeight =
                                FontWeight
                                    .SemiBold,

                            color =
                                if (
                                    Repo.modoOscuro
                                ) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                        )

                        OutlinedTextField(
                            value = des,

                            onValueChange = {
                                des = it
                            },

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(
                                        90.dp
                                    ),

                            shape =
                                RoundedCornerShape(
                                    12.dp
                                ),

                            colors =
                                OutlinedTextFieldDefaults
                                    .colors(

                                        focusedContainerColor =
                                            if (
                                                Repo.modoOscuro
                                            ) {

                                                Color(
                                                    0xFF343434
                                                )

                                            } else {

                                                Color(
                                                    0xFFE8E6FF
                                                )
                                            },

                                        unfocusedContainerColor =
                                            if (
                                                Repo.modoOscuro
                                            ) {

                                                Color(
                                                    0xFF343434
                                                )

                                            } else {

                                                Color(
                                                    0xFFE8E6FF
                                                )
                                            },

                                        focusedTextColor =
                                            if (
                                                Repo.modoOscuro
                                            ) {
                                                Color.White
                                            } else {
                                                Color.Black
                                            },

                                        unfocusedTextColor =
                                            if (
                                                Repo.modoOscuro
                                            ) {
                                                Color.White
                                            } else {
                                                Color.Black
                                            }
                                    )
                        )

                        // -------------------------
                        // HORA
                        // -------------------------

                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement
                                    .SpaceBetween,

                            verticalAlignment =
                                Alignment
                                    .CenterVertically
                        ) {

                            Text(
                                text =
                                    "Hora de la actividad *:",

                                fontSize =
                                    14.sp,

                                fontWeight =
                                    FontWeight
                                        .SemiBold,

                                color =
                                    if (
                                        Repo.modoOscuro
                                    ) {
                                        Color.White
                                    } else {
                                        Color.Black
                                    }
                            )

                            Button(
                                onClick = {

                                    mostrarTimePicker =
                                        true
                                },

                                shape =
                                    RoundedCornerShape(
                                        12.dp
                                    ),

                                colors =
                                    ButtonDefaults
                                        .buttonColors(
                                            containerColor =
                                                Color(
                                                    0xFF5A85B0
                                                )
                                        )
                            ) {

                                Text(
                                    text =
                                        if (
                                            horaSeleccionada
                                                .isEmpty()
                                        ) {
                                            "Seleccionar"
                                        } else {
                                            horaSeleccionada
                                        },

                                    color =
                                        Color.White
                                )
                            }
                        }

                        // -------------------------
                        // FECHA
                        // -------------------------

                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement
                                    .SpaceBetween,

                            verticalAlignment =
                                Alignment
                                    .CenterVertically
                        ) {

                            Text(
                                text =
                                    "Fecha de la actividad *:",

                                fontSize =
                                    14.sp,

                                fontWeight =
                                    FontWeight
                                        .SemiBold,

                                color =
                                    if (
                                        Repo.modoOscuro
                                    ) {
                                        Color.White
                                    } else {
                                        Color.Black
                                    }
                            )

                            Button(
                                onClick = {

                                    mostrarDatePicker =
                                        true
                                },

                                shape =
                                    RoundedCornerShape(
                                        12.dp
                                    ),

                                colors =
                                    ButtonDefaults
                                        .buttonColors(
                                            containerColor =
                                                Color(
                                                    0xFF5A85B0
                                                )
                                        )
                            ) {

                                val textoFecha =
                                    fechaSeleccionada
                                        ?.format(
                                            DateTimeFormatter
                                                .ofPattern(
                                                    "dd/MM/yyyy"
                                                )
                                        )
                                        ?: "Seleccionar"

                                Text(
                                    text =
                                        textoFecha,

                                    color =
                                        Color.White
                                )
                            }
                        }

                        // -------------------------
                        // REPETIR
                        // -------------------------

                        Text(
                            text = "Repetir",

                            fontSize =
                                14.sp,

                            fontWeight =
                                FontWeight
                                    .SemiBold,

                            color =
                                if (
                                    Repo.modoOscuro
                                ) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                        )

                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement
                                    .spacedBy(
                                        6.dp
                                    )
                        ) {

                            listOf(
                                "Lun",
                                "Mar",
                                "Mie",
                                "Jue",
                                "Vie",
                                "Sab",
                                "Dom"
                            ).forEach {
                                    opcion ->

                                val estaSeleccionado =
                                    repetirSeleccionados
                                        .contains(
                                            opcion
                                        )

                                Surface(
                                    modifier =
                                        Modifier
                                            .clickable {

                                                repetirSeleccionados =
                                                    if (
                                                        estaSeleccionado
                                                    ) {

                                                        repetirSeleccionados -
                                                                opcion

                                                    } else {

                                                        repetirSeleccionados +
                                                                opcion
                                                    }
                                            },

                                    shape =
                                        RoundedCornerShape(
                                            10.dp
                                        ),

                                    color =
                                        if (
                                            estaSeleccionado
                                        ) {

                                            Color(
                                                0xFF385A79
                                            )

                                        } else {

                                            Color(
                                                0xFF7A9BBF
                                            )
                                        }
                                ) {

                                    Text(
                                        text =
                                            opcion,

                                        color =
                                            Color.White,

                                        fontSize =
                                            11.sp,

                                        fontWeight =
                                            FontWeight
                                                .Bold,

                                        modifier =
                                            Modifier
                                                .padding(
                                                    horizontal =
                                                        8.dp,

                                                    vertical =
                                                        6.dp
                                                )
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier
                                    .height(
                                        16.dp
                                    )
                        )

                        // -------------------------
                        // GUARDAR CAMBIOS
                        // -------------------------

                        Button(
                            onClick = {

                                val hoy =
                                    LocalDate.now()

                                when {

                                    tit.isBlank() -> {

                                        mensajeError =
                                            "Por favor ingresa un título."
                                    }

                                    horaSeleccionada
                                        .isEmpty() -> {

                                        mensajeError =
                                            "Por favor selecciona una hora."
                                    }

                                    fechaSeleccionada ==
                                            null -> {

                                        mensajeError =
                                            "Por favor selecciona una fecha."
                                    }

                                    fechaSeleccionada
                                        ?.isBefore(
                                            hoy
                                        ) == true -> {

                                        mensajeError =
                                            "La fecha no puede ser en el pasado."
                                    }

                                    else -> {

                                        val repeticionFinal =
                                            if (
                                                repetirSeleccionados
                                                    .isEmpty()
                                            ) {

                                                "No"

                                            } else {

                                                repetirSeleccionados
                                                    .joinToString(
                                                        ", "
                                                    )
                                            }

                                        try {

                                            val diasEspanolMap =
                                                mapOf(
                                                    DayOfWeek.MONDAY to
                                                            "Lun",

                                                    DayOfWeek.TUESDAY to
                                                            "Mar",

                                                    DayOfWeek.WEDNESDAY to
                                                            "Mie",

                                                    DayOfWeek.THURSDAY to
                                                            "Jue",

                                                    DayOfWeek.FRIDAY to
                                                            "Vie",

                                                    DayOfWeek.SATURDAY to
                                                            "Sab",

                                                    DayOfWeek.SUNDAY to
                                                            "Dom"
                                                )

                                            val diaDeLaSemana =
                                                fechaSeleccionada
                                                    ?.let {

                                                        diasEspanolMap[
                                                            it.dayOfWeek
                                                        ]
                                                    }
                                                    ?: tarea.dia

                                            val tareaActualizada =
                                                Tarea(
                                                    id =
                                                        tarea.id,

                                                    titulo =
                                                        tit,

                                                    desc =
                                                        des.ifBlank {
                                                            null
                                                        },

                                                    hora =
                                                        horaSeleccionada,

                                                    dia =
                                                        diaDeLaSemana,

                                                    repetir =
                                                        repeticionFinal
                                                )

                                            viewModel
                                                .updateEvent(
                                                    tareaActualizada
                                                )

                                            onCerrar()

                                        } catch (
                                            _: Exception
                                        ) {

                                            mensajeError =
                                                "Ocurrió un error al guardar."
                                        }
                                    }
                                }
                            },

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(
                                        52.dp
                                    ),

                            shape =
                                RoundedCornerShape(
                                    26.dp
                                ),

                            colors =
                                ButtonDefaults
                                    .buttonColors(
                                        containerColor =
                                            Color(
                                                0xFF3B5E8C
                                            )
                                    )
                        ) {

                            Text(
                                text =
                                    "Guardar Cambios",

                                color =
                                    Color.White,

                                fontWeight =
                                    FontWeight.Bold,

                                fontSize =
                                    16.sp
                            )
                        }
                    }
                }

                // -------------------------
                // BOTÓN VOLVER
                // -------------------------

                IconButton(
                    onClick = {

                        onCerrar()
                    },

                    modifier =
                        Modifier
                            .align(
                                Alignment
                                    .BottomEnd
                            )
                            .padding(
                                16.dp
                            )
                            .background(
                                Color(
                                    0xFF4A6DA7
                                ),

                                CircleShape
                            )
                ) {

                    Icon(
                        painter =
                            painterResource(
                                id =
                                    android.R
                                        .drawable
                                        .ic_menu_revert
                            ),

                        contentDescription =
                            "Volver",

                        tint =
                            Color.White
                    )
                }
            }
        }
    }
}