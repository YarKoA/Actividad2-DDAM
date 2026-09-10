package com.example.actividad2_ddam.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.actividad2_ddam.model.Tarea
import com.example.actividad2_ddam.model.Repo

class EventViewModel : ViewModel() {

    // Lista observable de tareas sincronizada con el repositorio
    val events: SnapshotStateList<Tarea> = Repo.tareas

    // Función para añadir una nueva tarea
    fun addEvent(event: Tarea) {
        Repo.tareas.add(event)
    }

    // Función para eliminar una tarea
    fun removeEvent(event: Tarea) {
        Repo.tareas.remove(event)
    }

    // Función para actualizar una tarea existente
    fun updateEvent(updatedEvent: Tarea) {
        val index = Repo.tareas.indexOfFirst { it.id == updatedEvent.id }
        if (index != -1) {
            Repo.tareas[index] = updatedEvent
        }
    }

    // Función para obtener una tarea por su ID
    fun getEventById(id: Int): Tarea? {
        return Repo.tareas.find { it.id == id }
    }
}
