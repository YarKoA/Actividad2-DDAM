package com.example.actividad2_ddam.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
// 1. Nuevos imports para Room
import androidx.room.Entity
import androidx.room.PrimaryKey

interface GestionTarea {
    fun mostrar(): String
}

// 2. Etiquetamos la clase como Entidad de Room
@Entity(tableName = "tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val desc: String?,
    val hora: String,
    val dia: String,
    val repetir: String = "No",
    val esAnclada: Boolean = false
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
    var letraGrande by mutableStateOf(false)
    var grosorGrueso by mutableStateOf(false)

    // NOTA: Estas dos variables pronto desaparecerán porque la base de datos
    // se encargará del ID y de guardar las tareas, pero las dejamos por ahora
    // para que tu app no marque errores de compilación durante la transición.
    var contadorId = 3
    val tareas = mutableStateListOf(
        Tarea(1, "Salir a trotar", "Jogging por 30 minutos", "10:30 am", "Vie"),
        Tarea(2, "Junta a la 1", "Reunión de equipo en Zoom", "1:00 pm", "Vie")
    )
}