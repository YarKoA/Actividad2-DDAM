package com.example.actividad2_ddam.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface GestionTarea {
    fun mostrar(): String
}

// Clase principal para los datos de cada tarea / evento
data class Tarea(
    val id: Int,
    val titulo: String,
    val desc: String?,
    val hora: String,
    val dia: String,
    val repetir: String = "No"
) : GestionTarea {
    override fun mostrar() = "$titulo - $dia a las $hora"
}

// Modelo de datos para el usuario
data class Usuario(
    var nombre: String,
    var correo: String,
    var contrasena: String,
    var telefono: String,
    var edad: Int
)
// Repositorio global de datos
object Repo {
    var usuarioActual: Usuario? = null
    var modoOscuro by mutableStateOf(false)
    var contadorId = 3

    val tareas = mutableStateListOf(
        Tarea(1, "Salir a trotar", "Jogging por 30 minutos", "10:30 am", "Vie"),
        Tarea(2, "Junta a la 1", "Reunión de equipo en Zoom", "1:00 pm", "Vie")
    )
}
