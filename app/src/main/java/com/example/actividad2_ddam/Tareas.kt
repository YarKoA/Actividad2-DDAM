package com.example.actividad2_ddam

import androidx.compose.runtime.mutableStateListOf

// Interfaz para definir el comportamiento base de las tareas
interface GestionTarea {
    fun mostrar(): String
}

// Modelo de datos para representar una tarea individual
// Contiene identificador unico y detalles de la actividad
class Tarea(
    val id: Int,
    var titulo: String,
    var desc: String?,
    var hora: String,
    var dia: String,
    var fecha: String = "1",
    var mes: String = "Enero",
    var prioridad: String = "Baja",
    var repetir: String = "No"
) : GestionTarea {
    // Implementacion de la interfaz para mostrar resumen
    override fun mostrar() = "$titulo - $dia a las $hora"
}

// Repositorio global para gestionar los datos en memoria
// Mantiene la lista de tareas sincronizada con la interfaz
object Repo {
    var contadorId = 3 // Contador para asignar nuevos identificadores
    
    // Lista reactiva que notifica cambios a la UI
    val tareas = mutableStateListOf(
        Tarea(1, "Salir a trotar", "Jogging por 30 minutos", "10:30 am", "Vie"),
        Tarea(2, "Junta a la 1", null, "1:00 pm", "Vie")
    )
}
