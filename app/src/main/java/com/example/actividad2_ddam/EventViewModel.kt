package com.example.actividad2_ddam

import androidx.lifecycle.ViewModel

// ViewModel para gestionar el estado de los eventos y tareas de forma reactiva
class EventViewModel : ViewModel() {
    // Lista observable de tareas sincronizada con el repositorio
    val events = Repo.tareas

    // Función para añadir un nuevo evento/tarea
    fun addEvent(event: Tarea) {
        Repo.tareas.add(event)
    }

    // Función para eliminar un evento/tarea
    fun removeEvent(event: Tarea) {
        Repo.tareas.remove(event)
    }
}
