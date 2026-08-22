package com.example.actividad2_ddam

import androidx.compose.runtime.mutableStateListOf

// Interfaz simple para las tareas
interface GestionTarea {
    fun mostrar(): String
}

// Clase principal para los datos de cada tarea

class Tarea(val titulo: String, val desc: String?, val hora: String, val dia: String) : GestionTarea {
    override fun mostrar() = "$titulo - $dia a las $hora"
}

// Objeto que guarda la lista de tareas (nuestra coleccion)
object Repo {
    val tareas = mutableStateListOf(
        Tarea("Salir a trotar", "Jogging por 30 minutos", "10:30 am", "Vie"),
        Tarea("Junta a la 1", null, "1:00 pm", "Vie") // Ejemplo de valor nulo
    )
}
