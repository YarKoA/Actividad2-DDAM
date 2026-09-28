package com.example.actividad2_ddam.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.Entity
import androidx.room.PrimaryKey

interface GestionTarea {
    fun mostrar(): String
}

@Entity(tableName = "tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val desc: String?,
    val hora: String,
    val dia: String,
    val fecha: String = "",    // formato "2026-01-15" (ISO LocalDate)
    val repetir: String = "No",
    val esAnclada: Boolean = false
) : GestionTarea {
    override fun mostrar() = "$titulo - $dia a las $hora"
}

data class Usuario(
    var nombre: String,
    var correo: String,
    var contrasena: String,
    var telefono: String,
    var edad: Int
)

object Repo {
    var usuarioActual: Usuario? = null
    var modoOscuro by mutableStateOf(false)
    var letraGrande by mutableStateOf(false)
    var grosorGrueso by mutableStateOf(false)
    var contadorId = 3
    val tareas = mutableStateListOf(
        Tarea(1, "Salir a trotar", "Jogging por 30 minutos", "10:30 am", "Vie"),
        Tarea(2, "Junta a la 1", "Reunión de equipo en Zoom", "1:00 pm", "Vie")
    )
}